package com.tezos.lang.michelson.stackInfo

import com.google.common.collect.Maps
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.ShutDownTracker
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Alarm
import com.intellij.util.PathUtil
import com.intellij.util.containers.ContainerUtil
import com.tezos.client.StandaloneTezosClient
import com.tezos.client.TezosClient
import com.tezos.client.TezosClientError
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.intellij.settings.TezosSettingsListener
import com.tezos.lang.michelson.lang.MichelsonFileType
import java.nio.file.Path
import java.nio.file.Paths

open class MichelsonStackInfoManagerImpl(private val project: Project) : MichelsonStackInfoManager, ProjectComponent, DocumentListener, Disposable, TezosSettingsListener, FileEditorManagerListener {
    private companion object {
        val LOG = Logger.getInstance("#tezos.stackInfo")!!
        const val UPDATE_DELAY = 150
    }

    private class DocumentListenerDisposable(val document: Document, val listener: DocumentListener) : Disposable {
        override fun dispose() {
            document.removeDocumentListener(listener)
        }
    }

    private class UpdateRunabble(val manager: MichelsonStackInfoManagerImpl, val document: Document) : Runnable {
        override fun run() {
            manager.requests.remove(manager.documentPath(document))
            manager.updateStackInfo(document)
        }

        override fun equals(other: Any?): Boolean {
            return other is UpdateRunabble && other.document == document
        }

        override fun hashCode(): Int {
            return document.hashCode()
        }
    }

    private class StackUpdateListenerDisposable(val listeners: MutableList<StackInfoUpdateListener>, val listener: StackInfoUpdateListener) : Disposable {
        override fun dispose() {
            listeners.remove(listener)
        }
    }

    private val listeners = ContainerUtil.createLockFreeCopyOnWriteList<StackInfoUpdateListener>()

    private val alarm = Alarm(Alarm.ThreadToUse.POOLED_THREAD, this)
    private val requests: MutableMap<Path, UpdateRunabble> = Maps.newConcurrentMap()

    private val stacks: MutableMap<Path, StackInfo> = Maps.newConcurrentMap()
    @Volatile
    internal var client: TezosClient? = null

    override fun getComponentName(): String = "michelson.stackInfoManager"

    override fun initComponent() {
        val bus = ApplicationManager.getApplication().messageBus
        bus.connect(this).subscribe(TezosSettingService.TOPIC, this)
    }

    override fun projectOpened() {
        this.client = defaultTezosClient()

        val projectBus = project.messageBus
        projectBus.connect(this).subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, this)
    }

    override fun projectClosed() {
        alarm.cancelAllRequests()
    }

    override fun disposeComponent() {
        Disposer.dispose(this)
    }

    override fun dispose() {
        alarm.cancelAllRequests()
        stacks.clear()
    }

    override fun addListener(listener: StackInfoUpdateListener, parentDisposable: Disposable) {
        this.listeners.add(listener)
        Disposer.register(parentDisposable, StackUpdateListenerDisposable(listeners, listener))
    }

    override fun registerDocument(document: Document, parentDisposable: Disposable) {
        document.addDocumentListener(this)
        Disposer.register(parentDisposable, DocumentListenerDisposable(document, this))

        triggerStackUpdate(document)
    }

    override fun stackInfo(document: Document): StackInfo? {
        try {
            client ?: throw DefaultClientUnavailableException()

            val file = FileDocumentManager.getInstance().getFile(document) ?: return null
            val path = file.toJavaPath()

            return stacks[path]
        } catch (e: Exception) {
            return StackInfo(e)
        }
    }

    override fun defaultTezosClientChanged() {
        this.client = defaultTezosClient()

        if (this.client != null) {
            for (file in FileEditorManager.getInstance(project)!!.selectedFiles) {
                if (file.fileType is MichelsonFileType) {
                    val doc = FileDocumentManager.getInstance().getDocument(file)
                    if (doc != null) {
                        triggerStackUpdate(doc)
                    }
                }
            }
        }
    }

    override fun beforeDocumentChange(event: DocumentEvent) {}

    override fun documentChanged(e: DocumentEvent) {
        triggerStackUpdate(e.document)
    }

    /**
     * Debounce calls to the tezos client.
     */
    open fun triggerStackUpdate(document: Document) {
        client ?: return

        val path = documentPath(document) ?: return

        requests.get(path)?.let {
            requests.remove(path)
            alarm.cancelRequest(it)
        }

        val runnable = UpdateRunabble(this, document)
        requests[path] = runnable

        alarm.addRequest(runnable, UPDATE_DELAY)
    }

    override fun selectionChanged(event: FileEditorManagerEvent) {
        currentFileChanged(event.newFile)
    }

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        // no-op, selectionChanged is already called
    }

    override fun fileClosed(source: FileEditorManager, file: VirtualFile) {
        // no-op, selectionChanged is already called
    }

    private fun currentFileChanged(file: VirtualFile?) {
        if (file?.fileType is MichelsonFileType) {
            val doc = FileDocumentManager.getInstance().getDocument(file)
            if (doc != null) {
                triggerStackUpdate(doc)
            }
        }
    }

    protected fun updateStackInfo(document: Document) {
        val client = this.client ?: return
        val path = documentPath(document) ?: return

        try {
            val stack = client.typecheck(document.text)
            when (stack) {
                null -> stacks.remove(path)
                else -> {
                    stacks[path] = StackInfo(stack)

                    callListenersInEdt(document)
                }
            }
        } catch (e: TezosClientError) {
            LOG.debug("Error while executing tezos client. File: $path", e)
            stacks[path] = StackInfo(e)
            callListenersInEdt(document)
        }
    }

    protected fun documentPath(document: Document): Path? {
        val file = FileDocumentManager.getInstance().getFile(document)
        return file?.toJavaPath()
    }

    private fun callListenersInEdt(document: Document) {
        if (listeners.isEmpty()) {
            return
        }

        if (ShutDownTracker.isShutdownHookRunning()) {
            return
        }

        val listenersCopy = listeners.toTypedArray()
        ApplicationManager.getApplication().invokeLater {
            if (!ShutDownTracker.isShutdownHookRunning()) {
                for (l in listenersCopy) {
                    try {
                        l.stackInfoUpdated(document)
                    } catch (e: ProcessCanceledException) {
                        LOG.debug("process cancelled in listener", e)
                    }
                }
            }
        }
    }

    override fun tezosStackPositionChanged() {}

    protected open fun defaultTezosClient(): TezosClient? {
        return TezosSettingService.getInstance().state.getDefaultClient()?.let {
            StandaloneTezosClient(Paths.get(it.executablePath))
        }
    }

    private fun VirtualFile.toJavaPath() = Paths.get(PathUtil.toPresentableUrl(url))
}


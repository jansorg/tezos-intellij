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
import com.intellij.openapi.progress.ProcessCanceledException
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
import java.nio.file.Path
import java.nio.file.Paths

class MichelsonStackInfoManagerImpl : MichelsonStackInfoManager, ProjectComponent, DocumentListener, Disposable, TezosSettingsListener {
    private companion object {
        val LOG = Logger.getInstance("#tezos.stackInfo")!!
    }

    private class DocumentListenerDisposable(val document: Document, val listener: DocumentListener) : Disposable {
        override fun dispose() {
            document.removeDocumentListener(listener)
        }
    }

    private class StackUpdateListenerDisposable(val listeners: MutableList<StackInfoUpdateListener>, val listener: StackInfoUpdateListener) : Disposable {
        override fun dispose() {
            listeners.remove(listener)
        }
    }

    private val listeners = ContainerUtil.createLockFreeCopyOnWriteList<StackInfoUpdateListener>()
    private val alarm = Alarm(Alarm.ThreadToUse.POOLED_THREAD, this)
    private val stacks: MutableMap<Path, StackInfo> = Maps.newConcurrentMap()
    @Volatile
    private var client: TezosClient? = null

    override fun getComponentName(): String = "michelson.stackInfoManager"

    override fun initComponent() {
        val bus = ApplicationManager.getApplication().messageBus
        bus.connect(this).subscribe(TezosSettingService.TOPIC, this)
    }

    override fun projectOpened() {
        this.client = defaultTezosClient()
    }

    override fun projectClosed() {
    }

    override fun disposeComponent() {
        Disposer.dispose(this)
    }

    override fun dispose() {
        this.stacks.clear()
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
        client ?: throw DefaultClientUnavailableException()

        val file = FileDocumentManager.getInstance().getFile(document) ?: return null
        val path = file.toJavaPath()

        return stacks[path]
    }

    override fun defaultTezosClientChanged() {
        this.client = defaultTezosClient()
    }

    override fun beforeDocumentChange(event: DocumentEvent) {}

    override fun documentChanged(e: DocumentEvent) {
        triggerStackUpdate(e.document)
    }

    /**
     * Debounce calls to the tezos client.
     */
    private fun triggerStackUpdate(document: Document) {
        this.client ?: return

        alarm.cancelAllRequests()
        alarm.addRequest({
            updateStackInfo(document)
        }, 150)
    }

    private fun updateStackInfo(document: Document) {
        val client = this.client ?: return
        val file = FileDocumentManager.getInstance().getFile(document) ?: return
        val path = file.toJavaPath()

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
            LOG.debug("Error while executing tezos client. File: ${file.path}", e)
            stacks[path] = StackInfo(e)
        }
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

    private fun defaultTezosClient(): TezosClient? {
        return TezosSettingService.getInstance().state.getDefaultClient()?.let {
            StandaloneTezosClient(Paths.get(it.executablePath))
        }
    }

    private fun VirtualFile.toJavaPath() = Paths.get(PathUtil.toPresentableUrl(url))
}


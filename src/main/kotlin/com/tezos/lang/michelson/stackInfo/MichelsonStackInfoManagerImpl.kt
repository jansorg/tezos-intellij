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
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.PathUtil
import com.tezos.client.StandaloneTezosClient
import com.tezos.client.TezosClient
import com.tezos.client.TezosClientError
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.intellij.settings.TezosSettingsListener
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

class MichelsonStackInfoManagerImpl(private val project: Project) : MichelsonStackInfoManager, ProjectComponent, DocumentListener, Disposable, TezosSettingsListener {
    private companion object {
        val LOG = Logger.getInstance("#tezos.stackInfo")
    }

    @Volatile
    private var client: TezosClient? = null
    private val stacks: MutableMap<Path, StackInfo> = Maps.newConcurrentMap()

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

    }

    override fun registerFile(document: Document) {
        //fixme drop listener when file is deleted?
        document.addDocumentListener(this, this)

        updateStackInfo(document)
    }

    override fun unregisterFile(document: Document) {
        document.removeDocumentListener(this)
    }

    override fun stackInfo(document: Document, timeout: Long, timeoutUnit: TimeUnit): StackInfo? {
        client ?: throw DefaultClientUnavailableException()

        val file = FileDocumentManager.getInstance().getFile(document) ?: return null
        val path = file.toJavaPath()

        return stacks.get(path)
    }

    override fun defaultTezosClientChanged() {
        this.client = defaultTezosClient()
    }

    override fun beforeDocumentChange(event: DocumentEvent) {}

    override fun documentChanged(e: DocumentEvent) {
        updateStackInfo(e.document)
    }

    private fun updateStackInfo(document: Document) {
        val client = this.client ?: return
        val file = FileDocumentManager.getInstance().getFile(document) ?: return

        val path = file.toJavaPath()
        val doc = document

        ApplicationManager.getApplication().executeOnPooledThread {
            try {
                val stack = client.typecheck(doc.text)
                when (stack) {
                    null -> stacks.remove(path)
                    else -> stacks.put(path, StackInfo.createFromContent(doc.text, stack))
                }
            } catch (e: TezosClientError) {
                LOG.debug("Error while executing tezos client. File: ${file.path}", e)
                stacks.remove(path)
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


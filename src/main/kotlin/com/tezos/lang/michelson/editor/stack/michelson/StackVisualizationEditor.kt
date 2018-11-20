package com.tezos.lang.michelson.editor.stack.michelson

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.tezos.client.StandaloneTezosClient
import com.tezos.client.stack.*
import com.tezos.intellij.settings.TezosSettingService
import java.beans.PropertyChangeListener
import java.nio.file.Paths
import javax.swing.JComponent
import javax.swing.SwingWorker

/**
 * A file editor which renders a stack. It provides a "updateStack" method to be called when the content of the current file changed.
 * @author jansorg
 */
class MichelsonStackVisualizationEditor(project: Project, private val _file: VirtualFile) : UserDataHolderBase(), FileEditor {
    private companion object {
        private val LOG = Logger.getInstance("#tezos.stack")
        private val stackRenderer = StackRendering()
    }

    private val contentPane: StackHtmlPane = StackHtmlPane(project)

    @Volatile
    private var stackCache: StackInfo? = null

    override fun getName(): String = "Michelson Stack Visualization"

    override fun isValid(): Boolean = _file.isValid

    override fun isModified(): Boolean = false

    override fun getState(level: FileEditorStateLevel): FileEditorState = FileEditorState.INSTANCE

    override fun setState(state: FileEditorState) {}

    override fun getComponent(): JComponent = contentPane

    override fun getPreferredFocusedComponent(): JComponent? = null

    override fun dispose() {
        reset()
    }

    fun reset() {
        this.stackCache = null
    }

    /**
     * Updates the stack rendering.
     * If the information about "content" was already loaded, then it will be reused.
     * Otherwise the default client will be executed to retrieve the information about the current file and offset. When successful
     * this will be rendered. An error will be shown instead when unsuccessful.
     * The client is executed in the background to not block the EDT.
     */
    fun updateStack(content: String, offset: Int, renderOptions: RenderOptions) {
        val cached = stackCache
        when (cached?.matches(content)) {
            true -> render(cached.stack, offset, renderOptions)
            else -> {
                stackCache = null

                val worker = object : SwingWorker<StackInfo?, Any>() {
                    override fun doInBackground(): StackInfo? {
                        return loadClientStackInfo(content)
                    }

                    override fun done() {
                        try {
                            val value = this.get()
                            stackCache = value

                            if (value != null) {
                                render(value.stack, offset, renderOptions)
                            }
                        } catch (e: Exception) {
                            stackCache = null

                            LOG.debug("Error executing Tezos client", e)
                            when (e.cause) {
                                is DefaultClientUnavailableException -> contentPane.renderClientUnavailable()
                                is TezosClientNodeUnavailableError -> contentPane.renderError("Tezos node unavailable.", "The node of the default client is not running.")
                                is TezosClientError -> when (e.cause!!.message) {
                                    null -> contentPane.renderError("Error while executing the Tezos client command.")
                                    else -> contentPane.renderError("Client error.", e.message)
                                }
                                else -> contentPane.renderError("Error while executing the default Tezos client command.")
                            }
                        }

                    }
                }

                worker.execute()
            }
        }
    }

    private fun loadClientStackInfo(content: String): StackInfo? {
        val clientConfig = TezosSettingService.getSettings().getDefaultClient()
                ?: throw  DefaultClientUnavailableException()

        val client = StandaloneTezosClient(Paths.get(clientConfig.executablePath))
        val result = client.typecheck(content)
        return StackInfo.createFromContent(content, result)
    }

    private fun render(stackInfo: MichelsonStackTransformations, offset: Int, renderOptions: RenderOptions) {
        if (stackInfo.hasErrors) {
            contentPane.renderError("Errors detected.", "The Tezos client returned errors or warnings for the current file.")
            return
        }

        val matching = stackInfo.elementAt(offset)
        if (matching == null) {
            contentPane.renderInfo("No matching stack information found.")
            return
        }

        contentPane.renderHTML(stackRenderer.render(matching, renderOptions))
    }

    override fun selectNotify() {}

    override fun getCurrentLocation(): FileEditorLocation? = null

    override fun getStructureViewBuilder(): StructureViewBuilder? = null

    override fun deselectNotify() {}

    override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? = null

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {}

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {}
}

package com.tezos.lang.michelson.editor.stack.michelson

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.application.ApplicationManager
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
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.JComponent

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
     * Updates the stack rendering. Must not be called from the UI thread.
     *
     * If the information about "content" was already loaded, then it will be reused.
     * Otherwise the default client will be executed to retrieve the information about the current file and offset. When successful
     * this will be rendered. An error will be shown instead when unsuccessful.
     * The client is executed in the background to not block the EDT.
     */
    fun updateStackInPooledThread(content: String, offset: Int, renderOptions: RenderOptions) {
        val app = ApplicationManager.getApplication()
        if (app.isDispatchThread) {
            LOG.warn("stack updated called in ui dispatch tread")
        }

        val cached = stackCache
        when (cached?.matches(content)) {
            true -> app.invokeLater { render(cached.stack, offset, renderOptions) }
            else -> {
                val stackInfo: StackInfo?
                try {
                    stackInfo = loadClientStackInfo(content)
                    stackCache = stackInfo

                    if (stackInfo != null) {
                        app.invokeLater { render(stackInfo.stack, offset, renderOptions) }
                    }
                } catch (e: Exception) {
                    stackCache = null
                    app.invokeLater {
                        when (e) {
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
            contentPane.renderError("Errors detected.", "The Tezos client returned errors for the file.")
            return
        }

        val matching = stackInfo.elementAt(offset)
        if (matching == null) {
            contentPane.renderInfo("No matching stack information found.")
            return
        }

        val html = stackRenderer.render(matching, renderOptions)
        contentPane.renderHTML(html)
    }

    override fun selectNotify() {}

    override fun getCurrentLocation(): FileEditorLocation? = null

    override fun getStructureViewBuilder(): StructureViewBuilder? = null

    override fun deselectNotify() {}

    override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? = null

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {}

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {}
}

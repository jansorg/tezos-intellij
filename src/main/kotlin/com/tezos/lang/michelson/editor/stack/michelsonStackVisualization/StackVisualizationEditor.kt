package com.tezos.lang.michelson.editor.stack.michelsonStackVisualization

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
import com.tezos.client.TezosClientError
import com.tezos.client.TezosClientNodeUnavailableError
import com.tezos.intellij.stackRendering.RenderOptions
import com.tezos.intellij.stackRendering.StackRendering
import com.tezos.lang.michelson.stackInfo.DefaultClientUnavailableException
import com.tezos.lang.michelson.stackInfo.StackInfo
import java.beans.PropertyChangeListener
import javax.swing.JComponent

/**
 * A file editor which renders a stack. It provides a "updateStack" method to be called when the content of the current file changed.
 * @author jansorg
 */
class MichelsonStackVisualizationEditor(project: Project, private val _file: VirtualFile) : UserDataHolderBase(), FileEditor {
    private companion object {
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

    fun updateStackInfo(stack: StackInfo, offset: Int, renderOpts: RenderOptions) {
        val app = ApplicationManager.getApplication()
        app.assertIsDispatchThread()

        val (data, error) = stack
        if (error != null) {
            showError(error)
            return
        }

        if (data!!.hasErrors) {
            contentPane.renderError("Errors detected.", "The Tezos client returned errors for the file.")
            return
        }

        val matching = data.elementAt(offset)
        if (matching == null) {
            contentPane.renderInfo("No stack found.", "No matching stack information was found.")
            return
        }

        // create the HTML in a background thread, render in the EDT
        app.executeOnPooledThread {
            val html = stackRenderer.render(matching, renderOpts)
            app.invokeLater {
                contentPane.renderHTML(html)
            }
        }
    }

    fun showError(error: String) {
        contentPane.renderError(error)
    }

    private fun showError(e: Exception) {
        ApplicationManager.getApplication().assertIsDispatchThread()

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

    override fun selectNotify() {}

    override fun getCurrentLocation(): FileEditorLocation? = null

    override fun getStructureViewBuilder(): StructureViewBuilder? = null

    override fun deselectNotify() {}

    override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? = null

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {}

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {}
}

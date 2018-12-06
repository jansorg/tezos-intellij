package com.tezos.lang.michelson.editor.stack.michelsonStackVisualization

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.tezos.client.TezosClientError
import com.tezos.client.TezosClientNodeUnavailableError
import com.tezos.client.stack.MichelsonStackTransformation
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
open class MichelsonStackVisualizationEditor(val project: Project, private val _file: VirtualFile) : UserDataHolderBase(), FileEditor {
    companion object {
        internal val stackRenderer = StackRendering()
    }

    private val contentPane: StackHtmlPane = StackHtmlPane(project)

    @Volatile
    var isShowingError: Boolean = false
        private set

    @Volatile
    var isShowingInfo: Boolean = false
        private set

    @Volatile
    var isShowingHTML: Boolean = false
        private set

    override fun getName(): String = "Michelson Stack Visualization"

    override fun isValid(): Boolean = _file.isValid

    override fun isModified(): Boolean = false

    override fun getState(level: FileEditorStateLevel): FileEditorState = FileEditorState.INSTANCE

    override fun setState(state: FileEditorState) {}

    override fun getComponent(): JComponent = contentPane

    override fun getPreferredFocusedComponent(): JComponent? = null

    override fun dispose() {
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
            showError("Errors detected.", "The Tezos client returned errors for the file.")
            return
        }

        val matching = data.elementAt(offset)
        if (matching == null || data.isOnWhitespace(offset)) {
            showInfo("No stack found.", "No matching stack information was found.")
            return
        }

        // production: create the HTML in a background thread, render in the EDT
        // teset: create and render HTML in the current thread, i.e. the edt
        renderHTML(app, matching, renderOpts)
    }

    protected open fun renderHTML(app: Application, matching: MichelsonStackTransformation, renderOpts: RenderOptions) {
        if (app.isUnitTestMode) {
            val html = stackRenderer.render(matching, renderOpts)
            updateHTML(html)
        } else {
            app.executeOnPooledThread {
                val html = stackRenderer.render(matching, renderOpts)
                app.invokeLater {
                    updateHTML(html)
                }
            }
        }
    }

    protected open fun updateHTML(html: String) {
        contentPane.renderHTML(html)

        this.isShowingError = false
        this.isShowingInfo = false
        this.isShowingHTML = true
    }

    open fun showError(error: String, secondaryText: String? = null) {
        contentPane.renderError(error, secondaryText)

        this.isShowingError = true
        this.isShowingInfo = false
        this.isShowingHTML = false
    }

    open fun showInfo(info: String, secondaryText: String? = null) {
        contentPane.renderInfo(info, secondaryText)

        this.isShowingError = false
        this.isShowingInfo = true
        this.isShowingHTML = false
    }

    private fun showError(e: Exception) {
        ApplicationManager.getApplication().assertIsDispatchThread()

        when (e) {
            is DefaultClientUnavailableException -> contentPane.renderClientUnavailable()
            is TezosClientNodeUnavailableError -> contentPane.renderError("Tezos node unavailable.", "The node of the default client is not running.")
            is TezosClientError -> when (e.cause) {
                null -> contentPane.renderError("Error while executing the Tezos client command.")
                else -> contentPane.renderError("Client error.", e.cause!!.message)
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
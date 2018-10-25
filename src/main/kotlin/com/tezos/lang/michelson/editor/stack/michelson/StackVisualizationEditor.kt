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
import com.tezos.client.stack.RenderOptions
import com.tezos.client.stack.StackRendering
import com.tezos.client.stack.TezosClientError
import com.tezos.client.stack.TezosClientNodeUnavailableError
import com.tezos.intellij.settings.TezosSettingService
import java.beans.PropertyChangeListener
import java.nio.file.Paths
import javax.swing.JComponent

class MichelsonStackVisualizationEditor(project: Project, private val _file: VirtualFile) : UserDataHolderBase(), FileEditor {
    private companion object {
        val LOG = Logger.getInstance("#tezos.stack")
        private val stackRenderer = StackRendering()
    }

    private val contentPane: StackHtmlPane = StackHtmlPane(project)

    @Volatile
    private var stack: StackInfo? = null

    override fun getName(): String = "Michelson Stack Visualization"

    override fun isValid(): Boolean = _file.isValid

    override fun isModified(): Boolean = false

    override fun getState(level: FileEditorStateLevel): FileEditorState = FileEditorState.INSTANCE

    override fun setState(state: FileEditorState) {}

    override fun getComponent(): JComponent = contentPane

    override fun getPreferredFocusedComponent(): JComponent? = null

    override fun selectNotify() {}

    override fun getCurrentLocation(): FileEditorLocation? = null

    override fun getStructureViewBuilder(): StructureViewBuilder? = null

    override fun deselectNotify() {}

    override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? = null

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {}

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {}

    override fun dispose() {}

    fun updateStack(content: String, offset: Int, renderOptions: RenderOptions) {
        val cached = stack
        val stackInfo = when (cached?.matches(content)) {
            true -> cached.stack
            else -> {
                val clientConfig = TezosSettingService.getSettings().getDefaultClient()
                if (clientConfig == null) {
                    contentPane.renderClientUnavailable()
                    return
                }

                //fixme push into background
                stack = null
                val client = StandaloneTezosClient(Paths.get(clientConfig.executablePath))
                try {
                    val result = client.typecheck(content)
                    stack = StackInfo.createFromContent(content, result)
                    result
                } catch (e: TezosClientNodeUnavailableError) {
                    LOG.debug("Error executing Tezos client", e)
                    contentPane.renderError("Tezos node unavailable.", "It seems that the node of the default client is not running.")
                    return
                } catch (e: TezosClientError) {
                    LOG.debug("Error executing Tezos client", e)
                    when (e.message) {
                        null -> contentPane.renderError("Error while executing the Tezos client command.")
                        else -> contentPane.renderError("Client error.", e.message)
                    }
                    return
                } catch (e: Exception) {
                    LOG.debug("Error executing Tezos client", e)
                    contentPane.renderError("Error while executing the default Tezos client command.")
                    return
                }
            }
        }

        if (stackInfo.hasErrors) {
            // fixme show errors?
            contentPane.renderError("Unable to display because the Tezos client returned errors or warnings for the current file.")
            return
        }

        val matching = stackInfo.elementAt(offset)
        if (matching == null) {
            contentPane.renderInfo("No matching stack information found.")
            return
        }

        contentPane.renderHTML(stackRenderer.render(matching, renderOptions))
    }
}

package com.tezos.lang.michelson.editor.stack.michelson

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.JBColor
import com.intellij.ui.ScrollPaneFactory
import com.intellij.util.ui.UIUtil
import com.tezos.client.StandaloneTezosClient
import com.tezos.client.stack.*
import com.tezos.intellij.settings.TezosSettingService
import org.apache.commons.codec.digest.DigestUtils
import java.awt.BorderLayout
import java.beans.PropertyChangeListener
import java.io.StringReader
import java.nio.file.Paths
import javax.swing.JComponent
import javax.swing.JEditorPane
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

private data class StackInfo(val contentMD5: String, val stack: MichelsonStackTransformations) {
    fun matches(content: String): Boolean {
        return contentMD5.equals(md5(content))
    }

    companion object {
        private fun md5(content: String): String {
            return DigestUtils.md5Hex(content)
        }

        fun createFromContent(content: String, stack: MichelsonStackTransformations): StackInfo {
            return StackInfo(md5(content), stack)
        }
    }
}

class MichelsonStackVisualizationEditor(private val file: VirtualFile) : UserDataHolderBase(), FileEditor {
    private companion object {
        val LOG = Logger.getInstance("#tezos.stack")

        private val stackRenderer = StackRendering()
    }

    private val rootComponent: JPanel
    private val htmlPanel: JEditorPane

    @Volatile
    private var stack: StackInfo? = null

    init {
        htmlPanel = JEditorPane(UIUtil.HTML_MIME, "")
        htmlPanel.isEditable = false
        htmlPanel.border = EmptyBorder(7, 7, 7, 7)

        rootComponent = JPanel(BorderLayout())
        rootComponent.add(ScrollPaneFactory.createScrollPane(htmlPanel), BorderLayout.CENTER)
    }

    override fun getName(): String = "Michelson Stack Visualization"

    override fun isValid(): Boolean = file.isValid

    override fun isModified(): Boolean = false

    override fun getState(level: FileEditorStateLevel): FileEditorState = FileEditorState.INSTANCE

    override fun setState(state: FileEditorState) {}

    override fun getComponent(): JComponent = rootComponent

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
                    showError("Default Tezos client isn't configured.")
                    return
                }

                //fixme push into background
                val client = StandaloneTezosClient(Paths.get(clientConfig.executablePath))
                try {
                    val result = client.typecheck(content)
                    stack = StackInfo.createFromContent(content, result)
                    result
                } catch (e: MichelsonClientError) {
                    stack = null
                    LOG.warn("Error executing Tezos client", e)
                    showError(e.message ?: "Error while executing the Tezos client command.")
                    return
                } catch (e: Exception) {
                    stack = null
                    LOG.warn("Error executing Tezos client", e)
                    showError("Error while executing the default Tezos client command.")
                    return
                }
            }
        }

        if (stackInfo.hasErrors) {
            // fixme show errors?
            showError("Unable to display because the Tezos client returned errors or warnings for the current file.")
            return
        }

        val matching = stackInfo.elementAt(offset)
        if (matching == null) {
            showError("No matching stack information found.")
            return
        }

        updateText(stackRenderer.render(matching, renderOptions))
    }

    private fun updateText(html: String) {
        // we update the editor kit every time because it depends on the current IDE's theme
        val htmlKit = UIUtil.getHTMLEditorKit()
        htmlPanel.editorKit = htmlKit

        val doc = htmlKit.createDefaultDocument()
        htmlKit.read(StringReader(html), doc, 0)

        htmlPanel.document = doc
        htmlPanel.caretPosition = 0
    }

    private fun showError(message: String) {
        updateText("<html><div style=\"color:${JBColor.red.asHexString()}; font-weight:bold; font-size:1.1em; padding: 10px;\">$message</div></html>")
    }
}

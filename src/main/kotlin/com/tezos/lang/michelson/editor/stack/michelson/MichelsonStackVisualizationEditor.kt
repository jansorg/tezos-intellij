package com.tezos.lang.michelson.editor.stack.michelson

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.ui.UISettings
import com.intellij.ide.ui.UISettingsListener
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.ScrollPaneFactory
import com.intellij.util.ui.UIUtil
import com.tezos.client.StandaloneTezosClient
import com.tezos.client.stack.MichelsonClientError
import com.tezos.client.stack.MichelsonStackTransformations
import com.tezos.client.stack.RenderOptions
import com.tezos.client.stack.StackRendering
import com.tezos.intellij.settings.TezosSettingService
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.html
import kotlinx.html.stream.appendHTML
import org.apache.commons.codec.digest.DigestUtils
import java.awt.BorderLayout
import java.beans.PropertyChangeListener
import java.io.StringWriter
import java.nio.file.Paths
import javax.swing.JComponent
import javax.swing.JEditorPane
import javax.swing.JPanel
import javax.swing.text.html.HTMLEditorKit
import javax.swing.text.html.StyleSheet

data class StackInfo(val contentMD5: String, val stack: MichelsonStackTransformations) {
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

class CustomStylSsheet : StyleSheet()

class MichelsonStackVisualizationEditor(private val file: VirtualFile) : UserDataHolderBase(), FileEditor, UISettingsListener {
    private companion object {
        val LOG = Logger.getInstance("#tezos.stack")

        private val stackRenderer = StackRendering()

        private fun applyCustomStyles(kit: HTMLEditorKit): HTMLEditorKit {
            kit.styleSheet.styleSheets.forEach {
                if (it is CustomStylSsheet) {
                    kit.styleSheet.removeStyleSheet(it)
                }
            }

            val newStyle = CustomStylSsheet()
            newStyle.addRule(stackRenderer.defaultStyles())

            kit.styleSheet.addStyleSheet(newStyle)

            return kit
        }

        /**
         * Applies the styling to the HTML renderer. This can be called at the time of init and when the the UI theme changed
         * to update the rendering of the current content to the new styling.
         */
        private fun applyStyling(editorPane: JEditorPane) {
            // editorPane.background = HintUtil.INFORMATION_COLOR

            val html = editorPane.text

            val kit = UIUtil.getHTMLEditorKit(true)
            editorPane.editorKit = applyCustomStyles(kit)
            editorPane.document = kit.createDefaultDocument()
            editorPane.text = html
        }
    }

    private lateinit var rootComponent: JPanel
    private lateinit var editorPane: JEditorPane
    @Volatile
    private var customStyles: StyleSheet? = null

    @Volatile
    private var stack: StackInfo? = null

    override fun getName(): String {
        return "Michelson Stack Visualization"
    }

    override fun isValid(): Boolean = file.isValid

    override fun isModified(): Boolean = false

    override fun getState(level: FileEditorStateLevel): FileEditorState {
        return FileEditorState.INSTANCE
    }

    override fun setState(state: FileEditorState) {
    }

    override fun uiSettingsChanged(source: UISettings) {
        LOG.debug("UI settings changed")
        applyStyling(editorPane)
    }

    override fun getComponent(): JComponent {
        UISettings.getInstance().addUISettingsListener(this, this)

        rootComponent = JPanel(BorderLayout())

        editorPane = JEditorPane(UIUtil.HTML_MIME, "")
        editorPane.setEditable(false)
        applyStyling(editorPane)

        rootComponent.add(ScrollPaneFactory.createScrollPane(editorPane))
        return rootComponent
    }

    override fun getPreferredFocusedComponent(): JComponent? = null

    override fun selectNotify() {
    }

    override fun getCurrentLocation(): FileEditorLocation? = null

    override fun getStructureViewBuilder(): StructureViewBuilder? = null

    override fun deselectNotify() {
    }

    override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? = null

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
    }

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
    }

    override fun dispose() {
    }

    fun updateStack(settings: EditorColorsScheme, content: String, offset: Int) {
        val cached = stack
        val stackInfo = when (cached?.matches(content)) {
            true -> cached.stack
            else -> {
                val clientConfig = TezosSettingService.getSettings().getDefaultClient()
                if (clientConfig == null) {
                    showError("Please set a default Tezos client configured to see the stack visualization.")
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
            showError("Errors found")
            return
        }

        val matching = stackInfo.elementAt(offset) ?: return

        val html = stackRenderer.render(matching, RenderOptions(codeFont = settings.editorFontName, codeFontSizePt = settings.editorFontSize))
        editorPane.text = html

        rootComponent.updateUI()
    }

    private fun showError(message: String) {
        editorPane.text = StringWriter().appendHTML().html {
            body {
                div("error") {
                    +message
                }
            }
        }.toString()
    }
}

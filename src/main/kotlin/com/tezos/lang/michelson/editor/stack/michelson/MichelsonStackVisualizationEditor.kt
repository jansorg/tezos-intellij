package com.tezos.lang.michelson.editor.stack.michelson

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.ui.UISettings
import com.intellij.ide.ui.UISettingsListener
import com.intellij.ide.ui.laf.darcula.DarculaLaf
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.util.io.StreamUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.ScrollPaneFactory
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.tezos.client.StandaloneTezosClient
import com.tezos.client.stack.MichelsonClientError
import com.tezos.client.stack.MichelsonStackTransformations
import com.tezos.client.stack.RenderOptions
import com.tezos.client.stack.StackRendering
import com.tezos.intellij.settings.TezosSettingService
import org.apache.commons.codec.digest.DigestUtils
import org.xhtmlrenderer.simple.XHTMLPanel
import java.awt.BorderLayout
import java.beans.PropertyChangeListener
import java.nio.file.Paths
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

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

class MichelsonStackVisualizationEditor(private val file: VirtualFile) : UserDataHolderBase(), FileEditor, UISettingsListener {
    private companion object {
        val LOG = Logger.getInstance("#tezos.stack")

        private val stackRenderer = StackRendering()
    }

    private lateinit var rootComponent: JPanel

    private lateinit var htmlPanel: XHTMLPanel

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

    }

    override fun getComponent(): JComponent {
        UISettings.getInstance().addUISettingsListener(this, this)

        htmlPanel = CustomXHTMLPanel()
        htmlPanel.border = EmptyBorder(5, 5, 5, 5)

        rootComponent = JPanel(BorderLayout())
        rootComponent.add(ScrollPaneFactory.createScrollPane(htmlPanel), BorderLayout.CENTER)
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

        val defaultCSS = when (UIUtil.isUnderDarcula()) {
            true -> {
                val cssStream = DarculaLaf::class.java.getResourceAsStream("darcula" + if (JBUI.isHiDPI()) "@2x.css" else ".css")
                StreamUtil.readText(cssStream, "UTF-8")
            }
            false -> ""
        }

        val opts = RenderOptions(
                highlightUnchanged = true,
                highlightChanges = true,
                codeFont = settings.editorFontName,
                codeFontSizePt = settings.editorFontSize * 1.1,
                defaultCSS = defaultCSS
        )

        htmlPanel.setDocument(stackRenderer.render(matching, opts))
        rootComponent.updateUI()
    }

    private fun showError(message: String) {
//        editorPane?.text = StringWriter().appendHTML().html {
//            body {
//                div("error") { +message }
//            }
//        }.toString()
    }
}

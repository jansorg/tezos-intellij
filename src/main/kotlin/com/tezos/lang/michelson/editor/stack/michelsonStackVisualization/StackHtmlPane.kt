package com.tezos.lang.michelson.editor.stack.michelsonStackVisualization

import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.tezos.intellij.settings.ui.TezosConfigurable
import com.tezos.intellij.ui.JBStatusText
import java.awt.BorderLayout
import java.awt.Graphics
import java.io.StringReader
import javax.swing.JEditorPane

/**
 * A panel which either displays HTML or an message.
 * The HTML is to render the information about the current stack.
 * The message is displayed when no stack is available or when an error ocurred.
 *
 * @author jansorg
 */
internal class StackHtmlPane(private val project: Project) : JBPanel<StackHtmlPane>(BorderLayout()) {
    private val htmlPanel: JEditorPane = JEditorPane(UIUtil.HTML_MIME, "")

    private val emptyText = object : JBStatusText(this) {
        override fun isStatusVisible(): Boolean {
            // show empty text when no child component is visible
            return components.none { it.isVisible }
        }
    }

    init {
        htmlPanel.isEditable = false
        htmlPanel.border = JBUI.Borders.empty(5)

        add(ScrollPaneFactory.createScrollPane(htmlPanel), BorderLayout.CENTER)
    }

    fun renderHTML(html: String) {
        showChildren(true)

        // we update the editor kit every time because it depends on the current IDE's theme
        val htmlKit = UIUtil.getHTMLEditorKit()

        val doc = htmlKit.createDefaultDocument()
        htmlKit.read(StringReader(html), doc, 0)

        htmlPanel.editorKit = htmlKit
        htmlPanel.document = doc
        htmlPanel.caretPosition = 0
    }

    fun renderError(primary: String, secondaryText: String? = null) {
        showChildren(false)

        emptyText.setText(primary, SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES)
        if (secondaryText != null) {
            emptyText.appendSecondaryText(secondaryText, SimpleTextAttributes.REGULAR_ATTRIBUTES, null)
        }
    }

    fun renderInfo(msg: String, secondaryText:String? = null) {
        showChildren(false)
        emptyText.setText(msg, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        if (secondaryText != null) {
            emptyText.appendSecondaryText(secondaryText, SimpleTextAttributes.REGULAR_ATTRIBUTES, null)
        }
    }

    fun renderClientUnavailable() {
        showChildren(false)

        emptyText.text = "No default Tezos client configured."
        emptyText.appendSecondaryText("Settings...", SimpleTextAttributes.LINK_ATTRIBUTES) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, TezosConfigurable::class.java)
        }
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        emptyText.paint(this, g)
    }

    private fun showChildren(visible: Boolean) {
        for (c in components) {
            c.isVisible = visible
        }
    }
}
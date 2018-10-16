package com.tezos.lang.michelson.editor.stack.michelson

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBInsets
import com.intellij.util.ui.UIUtil
import com.tezos.client.StandaloneTezosClient
import com.tezos.client.stack.MichelsonStack
import com.tezos.client.stack.MichelsonStackTransformations
import com.tezos.client.stack.MichelsonStackType
import com.tezos.intellij.settings.TezosSettingService
import org.apache.commons.codec.digest.DigestUtils
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.beans.PropertyChangeListener
import java.nio.file.Paths
import javax.swing.JComponent
import javax.swing.JPanel

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

class MichelsonStackVisualizationEditor(private val file: VirtualFile) : UserDataHolderBase(), FileEditor {
    private companion object {
        val LOG = Logger.getInstance("#tezos.stack")
    }

    private lateinit var rootComponent: JPanel
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

    override fun getComponent(): JComponent {
        rootComponent = JPanel(BorderLayout())
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

    fun updateStack(content: String, offset: Int) {
        rootComponent.removeAll()

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
                } catch (e: Exception) {
                    LOG.warn("Error executing Tezos client", e)
                    showError("Error while executing default Tezos client.")
                    return
                }
            }
        }

        if (stackInfo.hasErrors) {
            showError("Errors found")
            return
        }

        val matching = stackInfo.elementAt(offset) ?: return
        val top = JPanel(GridBagLayout())

        val c = GridBagConstraints()
        c.fill = GridBagConstraints.VERTICAL
        c.anchor = GridBagConstraints.LINE_START
        c.gridheight = 1
        c.gridwidth = 1
        c.weightx = 0.5
        c.insets = JBInsets(5, 5, 5, 5)

        c.gridx = 0
        c.gridy = 0
        top.add(heading("Before"), c)

        c.gridx = 1
        c.gridy = 0
        top.add(heading("After"), c)

        c.gridx = 0
        c.gridy = 1
        addStackTo(matching.before, c, top)

        c.gridx = 1
        c.gridy = 1
        addStackTo(matching.after, c, top)

        // add spacer in last row
        c.gridx = 0
        c.weighty = 1.0
        top.add(JPanel(), c)

        rootComponent.add(top, BorderLayout.CENTER)
        rootComponent.updateUI()
    }

    private fun showError(message: String) {
        rootComponent.add(JBLabel(message))
    }

    private fun heading(title: String): JComponent {
        return JBLabel("<html><b>$title</b></html>", UIUtil.ComponentStyle.LARGE)
    }

    private fun addStackTo(stack: MichelsonStack, c: GridBagConstraints, parent: JComponent) {
        for (f in stack.frames) {
            val html = typeToString(f.type)
            val label = JBLabel("<html>$html</html>")
            parent.add(label, c)
            c.gridy++
        }
    }

    private fun typeToString(type: MichelsonStackType, wrap: Boolean = false): String {
        if (type.arguments.isEmpty()) {
            return type.name
        }

        var s = if (wrap) "(" else ""

        s += type.name
        for (a in type.arguments) {
            s += " " + typeToString(a, true)
        }

        s += if (wrap) ")" else ""

        return s
    }
}
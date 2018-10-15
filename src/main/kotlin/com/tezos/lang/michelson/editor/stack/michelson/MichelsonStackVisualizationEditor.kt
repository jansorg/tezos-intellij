package com.tezos.lang.michelson.editor.stack.michelson

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBInsets
import com.intellij.util.ui.UIUtil
import com.tezos.client.AlphanetTezosClient
import com.tezos.client.StandaloneTezosClient
import com.tezos.client.stack.MichelsonStack
import com.tezos.client.stack.MichelsonStackType
import com.tezos.intellij.settings.TezosSettingService
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.beans.PropertyChangeListener
import java.nio.file.Paths
import javax.swing.JComponent
import javax.swing.JPanel

class MichelsonStackVisualizationEditor(private val project: Project, private val file: VirtualFile) : UserDataHolderBase(), FileEditor {
    private lateinit var rootComponent: JPanel

    override fun isModified(): Boolean = false

    override fun getState(level: FileEditorStateLevel): FileEditorState {
        return FileEditorState.INSTANCE
    }

    override fun getName(): String {
        return "Michelson Stack Visualization"
    }

    override fun isValid(): Boolean = file.isValid

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

        val clientConfig = TezosSettingService.getSettings().getDefaultClient()
        if (clientConfig == null) {
            rootComponent.add(JBLabel("Please set a default Tezos client configured to see the stack visualization."))
            return
        }

        val client = when (clientConfig.isScriptClient) {
            true -> AlphanetTezosClient(Paths.get(clientConfig.executablePath))
            false -> StandaloneTezosClient(Paths.get(clientConfig.executablePath))
        }

        //fixme push into background
        val result = client.typecheck(content)
        if (result == null) {
            rootComponent.add(JBLabel("Error while executing default Tezos client."))
            return
        }

        if (result.hasErrors) {
            rootComponent.add(JBLabel("Errors found"))
            return
        }

        val matching = result.elementAt(offset) ?: return
        val maxStackSize = Math.max(matching.before.size, matching.after.size)

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
        c.gridy = 1 + maxStackSize - matching.before.size
        addStackTo(matching.before, c, top)

        c.gridx = 1
        c.gridy = 1 + maxStackSize - matching.after.size
        addStackTo(matching.after, c, top)

        // add spacer in last row
        c.gridx = 0
        c.weighty = 1.0
        top.add(JPanel(), c)

        rootComponent.add(top, BorderLayout.CENTER)
        rootComponent.updateUI()
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

package com.tezos.lang.michelson.editor.stack

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.fileEditor.*
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.ex.FileEditorProviderManager
import com.intellij.openapi.fileEditor.impl.text.TextEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class MichelsonStackEditor(private val project: Project, private val file: VirtualFile) : UserDataHolderBase(), FileEditor {
    override fun isModified(): Boolean = false

    override fun getState(level: FileEditorStateLevel): FileEditorState {
        return TextEditorState()
    }

    override fun getName(): String {
        return "Michelson Stack"
    }

    override fun isValid(): Boolean {
        return file.isValid
    }

    override fun setState(state: FileEditorState) {
    }

    override fun getComponent(): JComponent {
        val jPanel = JPanel()
        jPanel.add(JLabel("Hello"))

        return jPanel
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return null
    }

    override fun selectNotify() {
    }

    override fun getCurrentLocation(): FileEditorLocation? {
        return null
    }

    override fun getStructureViewBuilder(): StructureViewBuilder? = null

    override fun deselectNotify() {
    }

    override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? {
        return null
    }

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
    }

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
    }

    override fun dispose() {
    }
}

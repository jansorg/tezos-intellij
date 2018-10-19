package com.tezos.lang.michelson.editor.stack.michelson

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.tezos.lang.michelson.lang.MichelsonFileType
import org.jdom.Element

/**
 * @author jansorg
 */
class StackVisualizationEditorProvider : FileEditorProvider {
    override fun getEditorTypeId(): String = "tezos.stack"

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.NONE

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return file.fileType is MichelsonFileType
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        return MichelsonStackVisualizationEditor(file)
    }

    override fun readState(sourceElement: Element, project: Project, file: VirtualFile): FileEditorState {
        return FileEditorState.INSTANCE
    }

    override fun writeState(state: FileEditorState, project: Project, targetElement: Element) {
    }

    override fun disposeEditor(editor: FileEditor) {
        Disposer.dispose(editor)
    }
}
package com.tezos.lang.michelson.editor.stack

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.Disposer
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.lang.michelson.editor.stack.michelson.MichelsonStackVisualizationEditor
import com.tezos.lang.michelson.editor.stack.michelson.StackVisualizationEditorProvider
import com.tezos.lang.michelson.editor.stack.split.SplitTextEditorProvider

/**
 * @author jansorg
 */
class MichelsonSplitEditorProvider : SplitTextEditorProvider(PsiAwareTextEditorProvider(), StackVisualizationEditorProvider()), DumbAware {
    override fun createSplitEditor(firstEditor: FileEditor, secondEditor: FileEditor): FileEditor {
        if (firstEditor !is TextEditor || secondEditor !is MichelsonStackVisualizationEditor) {
            throw IllegalStateException("editors not of execpted types")
        }

        return MichelsonSplitEditor(firstEditor, secondEditor)
    }

    override fun disposeEditor(editor: FileEditor) {
        Disposer.dispose(editor)
    }
}

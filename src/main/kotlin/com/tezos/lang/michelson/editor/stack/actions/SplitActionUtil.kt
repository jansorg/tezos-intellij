package com.tezos.lang.michelson.editor.stack.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.tezos.lang.michelson.editor.stack.MichelsonSplitEditor

/**
 * @author jansorg
 */
object SplitActionUtil {
    fun findStackEditor(e: AnActionEvent): MichelsonSplitEditor {
        return e.getData(PlatformDataKeys.FILE_EDITOR) as MichelsonSplitEditor
    }
}
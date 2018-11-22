package com.tezos.lang.michelson.editor.stack.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.RightAlignedToolbarAction
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.project.DumbAware
import com.tezos.intellij.ui.Icons

/**
 * @author jansorg
 */
class MultilineTypesAction : ToggleAction("Indent nested types", "Wrap and indent nested types lines", Icons.StackIndentation), DumbAware, RightAlignedToolbarAction {
    override fun setSelected(e: AnActionEvent, state: Boolean) {
        val editor = SplitActionUtil.findStackEditor(e)
        editor.nestedBlocks = state
        editor.triggerStackUpdate()
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        return SplitActionUtil.findStackEditor(e).nestedBlocks
    }
}
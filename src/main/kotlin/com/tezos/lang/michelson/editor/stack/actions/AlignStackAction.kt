package com.tezos.lang.michelson.editor.stack.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.project.DumbAware
import com.tezos.intellij.ui.Icons

/**
 * @author jansorg
 */
class AlignStackAction : ToggleAction("Align stacks", "Aligns both stack to start at the bottom.", Icons.StackAlignment), DumbAware {
    override fun setSelected(e: AnActionEvent, state: Boolean) {
        val editor = SplitActionUtil.findStackEditor(e)
        editor.stackAlignStacks = state
        editor.refreshRendering()
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        return SplitActionUtil.findStackEditor(e).stackAlignStacks
    }
}
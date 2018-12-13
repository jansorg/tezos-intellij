package com.tezos.lang.michelson.editor.stack.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.RightAlignedToolbarAction
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.project.DumbAware
import com.tezos.intellij.ui.Icons

/**
 * @author jansorg
 */
class ShowAnnotationsAction : ToggleAction("Show annotations", "Show annotations", Icons.StackAnnotations), DumbAware, RightAlignedToolbarAction {
    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabledAndVisible = SplitActionUtil.findStackEditor(e) != null
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        val editor = SplitActionUtil.findStackEditor(e) ?: return
        editor.stackShowAnnotations = state
        editor.triggerStackUpdate()
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        return SplitActionUtil.findStackEditor(e)?.stackShowAnnotations ?: false
    }
}
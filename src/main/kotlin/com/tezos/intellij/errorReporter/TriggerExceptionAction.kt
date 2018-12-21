package com.tezos.intellij.errorReporter

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ex.ApplicationManagerEx
import com.tezos.intellij.ui.Icons

/**
 * @author jansorg
 */
class TriggerExceptionAction : AnAction("Tezos: Trigger test exception", "Triggers an exception to test the error reporting", Icons.Tezos) {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = ApplicationManagerEx.getApplication().isInternal
    }

    override fun actionPerformed(e: AnActionEvent) {
        throw IllegalStateException("Tezos test exception")
    }
}
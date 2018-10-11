package com.tezos.intellij.settings.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.tezos.intellij.settings.TezosClientConfig
import javax.swing.JComponent

/**
 * @author jansorg
 */
class EditTezosClientDialog(project: Project?, private val config: TezosClientConfig) : DialogWrapper(project, false, IdeModalityType.PROJECT) {
    override fun createCenterPanel(): JComponent? {
        val form = TezosClientEditForm()
        form.applyFrom(config)

        return form.mainPanel
    }
}
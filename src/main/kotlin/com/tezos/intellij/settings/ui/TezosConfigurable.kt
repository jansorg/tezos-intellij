package com.tezos.intellij.settings.ui

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.intellij.settings.TezosSettings
import javax.swing.JComponent

/**
 * @author jansorg
 */
class TezosConfigurable() : Configurable, SearchableConfigurable {
    private val settingsForm = TezosSettingsForm()

    override fun getId(): String = "tezos.configurable"
    override fun getDisplayName(): String = "Tezos"

    override fun createComponent(): JComponent? {
        settingsForm.init()
        return settingsForm.mainPanel
    }

    override fun apply() {
        settingsForm.applyTo(TezosSettingService.getSettings())
    }

    override fun reset() {
        settingsForm.resetTo(TezosSettingService.getSettings().copy())
    }

    override fun isModified(): Boolean {
        val current = TezosSettings()
        settingsForm.applyTo(current)

        return current != TezosSettingService.getSettings()
    }

    override fun getHelpTopic(): String? = null

    override fun enableSearch(option: String?): Runnable? = null

    override fun disposeUIResources() {
    }
}

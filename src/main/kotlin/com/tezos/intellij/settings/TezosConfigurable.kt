package com.tezos.intellij.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent

/**
 * @author jansorg
 */
class TezosConfigurable() : Configurable, SearchableConfigurable {
    override fun getId(): String = "tezos.configurable"

    override fun getDisplayName(): String = "Tezos"

    override fun createComponent(): JComponent? {
        return TezosConfigurableForm().mainPanel
    }

    override fun disposeUIResources() {

    }

    override fun apply() {

    }

    override fun reset() {
    }

    override fun isModified(): Boolean {
        return false
    }

    override fun getHelpTopic(): String? = null

    override fun enableSearch(option: String?): Runnable? {
        return null
    }
}

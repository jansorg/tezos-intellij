package com.tezos.lang.michelson.editor.formatter

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.openapi.options.Configurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
    override fun createSettingsPage(settings: CodeStyleSettings, originalSettings: CodeStyleSettings): Configurable {
        return SettingsConfigurable(settings, originalSettings)
    }

    override fun createCustomSettings(settings: CodeStyleSettings) = MichelsonCodeStyleSettings(settings)

    override fun getLanguage() = MichelsonLanguage
}

private class SettingsConfigurable(settings: CodeStyleSettings, originalSettings: CodeStyleSettings) : CodeStyleAbstractConfigurable(settings, originalSettings, MichelsonLanguage.displayName) {
    override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
        return TabbedCodeStylePanel(currentSettings, settings)
    }

    override fun getHelpTopic(): String? = null
}

private class TabbedCodeStylePanel(currentSettings: CodeStyleSettings, settings: CodeStyleSettings) : TabbedLanguageCodeStylePanel(MichelsonLanguage, currentSettings, settings) {
    override fun initTabs(settings: CodeStyleSettings) {
        addIndentOptionsTab(settings)
        addSpacesTab(settings)
        addBlankLinesTab(settings)
        addWrappingAndBracesTab(settings)

        addTab(MichelsonCodeStylePanel(settings))
    }
}

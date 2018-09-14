package com.tezos.lang.michelson.formatter

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import com.tezos.lang.michelson.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonLangCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        if (settingsType == LanguageCodeStyleSettingsProvider.SettingsType.BLANK_LINES_SETTINGS) {
            consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE")
        }
    }

    override fun getDefaultCommonSettings(): CommonCodeStyleSettings? {
        val settings = CommonCodeStyleSettings(MichelsonLanguage)
        settings.KEEP_LINE_BREAKS = true; // already default, but to document our default style
        settings.KEEP_BLANK_LINES_IN_CODE = 1

        val indentOptions = settings.initIndentOptions()
        indentOptions.INDENT_SIZE = 2

        return settings
    }

    override fun getLanguage(): Language = MichelsonLanguage

    override fun getIndentOptionsEditor(): IndentOptionsEditor? {
        return SmartIndentOptionsEditor()
    }

    override fun getCodeSample(settingsType: SettingsType): String {
        return """
            parameter (pair (int (pair int string)));
            storage unit;
            code {} ;
        """.trimIndent()
    }
}

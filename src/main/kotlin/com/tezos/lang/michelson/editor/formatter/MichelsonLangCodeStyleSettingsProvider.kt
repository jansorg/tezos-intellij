package com.tezos.lang.michelson.editor.formatter

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable.BlankLinesOption
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable.SpacingOption
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider.SettingsType.BLANK_LINES_SETTINGS
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider.SettingsType.WRAPPING_AND_BRACES_SETTINGS
import com.tezos.lang.michelson.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonLangCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        when (settingsType) {
            BLANK_LINES_SETTINGS -> {
                consumer.showStandardOptions(
                        BlankLinesOption.KEEP_BLANK_LINES_IN_CODE.name,
                        BlankLinesOption.KEEP_BLANK_LINES_BEFORE_RBRACE.name)
            }

            SettingsType.SPACING_SETTINGS -> {
                consumer.showStandardOptions(
                        SpacingOption.SPACE_WITHIN_PARENTHESES.name,
                        SpacingOption.SPACE_BEFORE_SEMICOLON.name,
                        SpacingOption.SPACE_AFTER_SEMICOLON.name)
            }

            WRAPPING_AND_BRACES_SETTINGS -> {
            }

            SettingsType.INDENT_SETTINGS -> {
            }

            SettingsType.COMMENTER_SETTINGS -> {
            }
        }
    }

    override fun getDefaultCommonSettings(): CommonCodeStyleSettings {
        val settings = CommonCodeStyleSettings(MichelsonLanguage)
        settings.KEEP_LINE_BREAKS = true; // default, but we do want to document our default style
        settings.KEEP_BLANK_LINES_IN_CODE = 1
        settings.KEEP_BLANK_LINES_BEFORE_RBRACE = 0

        val indentOptions = settings.initIndentOptions()
        indentOptions.INDENT_SIZE = 2
        indentOptions.USE_TAB_CHARACTER = false //default

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

package com.tezos.lang.michelson.formatter

import com.intellij.application.options.codeStyle.OptionTreeWithPreviewPanel
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import com.tezos.lang.michelson.MichelsonFileType
import com.tezos.lang.michelson.MichelsonLanguage

/**
 */
class MichelsonCodeStylePanel(settings: CodeStyleSettings) : OptionTreeWithPreviewPanel(settings) {
    companion object {
        private val COMPLEX_TYPE_GROUP = "Compex types"
        private val STATEMENT_GROUP = "Statements"
    }

    init {
        init()
    }

    override fun getDefaultLanguage(): Language = MichelsonLanguage
    override fun getFileType(): FileType = MichelsonFileType
    override fun getTabTitle(): String = "Michelson"
    override fun getRightMargin(): Int = 80

    override fun initTables() {
        val c = MichelsonCodeStyleSettings::class.java

        showCustomOption(c, MichelsonCodeStyleSettings::COMPLEX_TYPE_WRAP_FIRST.name, "Wrap first type", COMPLEX_TYPE_GROUP)
        showCustomOption(c, MichelsonCodeStyleSettings::COMPLEX_TYPE_ALIGN.name, "Align types", COMPLEX_TYPE_GROUP)

        showCustomOption(c, MichelsonCodeStyleSettings::WRAP_FIRST_BLOCK.name, "Wrap first block", STATEMENT_GROUP)
        showCustomOption(c, MichelsonCodeStyleSettings::ALIGN_BLOCKS.name, "Align blocks", STATEMENT_GROUP)

        initCustomOptions(STATEMENT_GROUP)
        initCustomOptions(COMPLEX_TYPE_GROUP)
    }


    override fun getPreviewText(): String = """
        parameter (pair (or string int) (pair (pair string int) int));
        storage unit;
        code
          {
            IF { DROP; } { IF { CDR; } { DROP; } };
            NIL operation;
            PAIR;
          }
    """.trimIndent()

    override fun getSettingsType(): LanguageCodeStyleSettingsProvider.SettingsType {
        return LanguageCodeStyleSettingsProvider.SettingsType.LANGUAGE_SPECIFIC
    }
}

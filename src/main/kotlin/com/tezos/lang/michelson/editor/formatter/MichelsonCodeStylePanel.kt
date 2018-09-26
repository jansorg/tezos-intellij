package com.tezos.lang.michelson.editor.formatter

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
        private val COMMENT_GROUP = "Comments"
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
        showCustomOption(c, MichelsonCodeStyleSettings::COMPLEX_TYPE_ALIGN_ANNOTATIONS.name, "Align annotations", COMPLEX_TYPE_GROUP)

        showCustomOption(c, MichelsonCodeStyleSettings::WRAP_FIRST_BLOCK.name, "Wrap first block", STATEMENT_GROUP)
        showCustomOption(c, MichelsonCodeStyleSettings::ALIGN_BLOCKS.name, "Align blocks", STATEMENT_GROUP)

        showCustomOption(c, MichelsonCodeStyleSettings::LINE_COMMENT_LEADING_SPACE.name, "Leading space in line comment", COMMENT_GROUP)
        showCustomOption(c, MichelsonCodeStyleSettings::LINE_COMMENT_ALIGN.name, "Align line comments in blocks", COMMENT_GROUP)

        initCustomOptions(STATEMENT_GROUP)
        initCustomOptions(COMPLEX_TYPE_GROUP)
        initCustomOptions(COMMENT_GROUP)
    }


    override fun getPreviewText(): String = """
        parameter (or (key_hash %Initialize)
              (pair     %Withdraw :var1
               (key  %from :type)
               (pair %other :var2
                (mutez     %amount :var3)
                (signature %sig :var))));
        storage unit;
        code
          {
            IF { DROP; } { IF { CDR; } { DROP; } };
            IF {
                  CDR;#comment
                  DROP;#comment
                }
                { DROP; }
            NIL operation;
            PAIR;
          }
    """.trimIndent()

    override fun getSettingsType(): LanguageCodeStyleSettingsProvider.SettingsType {
        return LanguageCodeStyleSettingsProvider.SettingsType.LANGUAGE_SPECIFIC
    }
}

package com.tezos.lang.michelson.lang

import com.intellij.openapi.fileTypes.LanguageFileType
import com.tezos.intellij.ui.Icons
import javax.swing.Icon

/**
 * @author jansorg
 */
object MichelsonFileType : LanguageFileType(MichelsonLanguage) {
    override fun getDefaultExtension(): String = "tz"

    override fun getIcon(): Icon = Icons.Tezos

    override fun getName(): String = "Michelson"

    override fun getDescription(): String = "Michelson language for the Tezos platform"
}
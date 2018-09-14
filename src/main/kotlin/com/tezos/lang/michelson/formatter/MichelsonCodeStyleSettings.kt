package com.tezos.lang.michelson.formatter

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.tezos.lang.michelson.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonCodeStyleSettings(container: CodeStyleSettings) : CustomCodeStyleSettings(MichelsonLanguage.id, container) {
    var WRAP_FIRST_BLOCK = false
    var ALIGN_BLOCKS = true

    var COMPLEX_TYPE_WRAP_FIRST = false
    var COMPLEX_TYPE_ALIGN = false
}
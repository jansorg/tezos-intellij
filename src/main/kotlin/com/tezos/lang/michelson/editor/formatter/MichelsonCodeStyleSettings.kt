package com.tezos.lang.michelson.editor.formatter

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonCodeStyleSettings(container: CodeStyleSettings) : CustomCodeStyleSettings(MichelsonLanguage.id, container) {
    @JvmField
    var LINE_COMMENT_LEADING_SPACE = false
    @JvmField
    var LINE_COMMENT_ALIGN = false

    @JvmField
    var WRAP_FIRST_BLOCK = false
    @JvmField
    var ALIGN_BLOCKS = true

    @JvmField
    var COMPLEX_TYPE_WRAP_FIRST = false
    @JvmField
    var COMPLEX_TYPE_ALIGN = false
    @JvmField
    var COMPLEX_TYPE_ALIGN_ANNOTATIONS = false
}
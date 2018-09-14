package com.tezos.lang.michelson.formatter

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.tezos.lang.michelson.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonCodeStyleSettings(container: CodeStyleSettings) : CustomCodeStyleSettings(MichelsonLanguage.id, container) {

}
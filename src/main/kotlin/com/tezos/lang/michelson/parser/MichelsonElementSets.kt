package com.tezos.lang.michelson.parser

import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonTypes

/**
 * Shared TokenSets of PSI element types of the Michelson language.
 * @author jansorg
 */
object MichelsonElementSets {
    val INSTRUCTIONS = TokenSet.create(MichelsonTypes.GENERIC_INSTRUCTION, MichelsonTypes.MACRO_INSTRUCTION)
}
package com.tezos.lang.michelson.lexer

import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonTypes

/**
 * @author jansorg
 */
object MichelsonElementTypes {
    val MACROS = TokenSet.create(
            MichelsonTypes.MACRO_DIIP_TOKEN,
            MichelsonTypes.MACRO_DUUP_TOKEN,
            MichelsonTypes.MACRO_MAP_CADR_TOKEN,
            MichelsonTypes.MACRO_NESTED_TOKEN,
            MichelsonTypes.MACRO_PAIRS_TOKEN,
            MichelsonTypes.MACRO_PAIR_ACCESS_TOKEN,
            MichelsonTypes.MACRO_SET_CADR_TOKEN,
            MichelsonTypes.MACRO_TOKEN)

    val BOOLEAN = TokenSet.create(
            MichelsonTypes.TRUE,
            MichelsonTypes.FALSE)

    val PARENTHESES = TokenSet.create(
            MichelsonTypes.LEFT_PAREN,
            MichelsonTypes.RIGHT_PAREN)

    val BRACES = TokenSet.create(
            MichelsonTypes.LEFT_CURLY,
            MichelsonTypes.RIGHT_CURLY)

}
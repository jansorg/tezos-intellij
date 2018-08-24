package com.tezos.lang.michelson.lexer

import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonTypes

/**
 * @author jansorg
 */
object MichelsonElementTypes {
    val MACROS = TokenSet.create(MichelsonTypes.MACRO_TOKEN)

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
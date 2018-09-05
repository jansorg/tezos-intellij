package com.tezos.lang.michelson.lexer

import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonTypes

/**
 * Shared TokenSets of the Michelson language.
 * @author jansorg
 */
object MichelsonElementTokenSets {
    val WHITESPACE_TOKENS = TokenSet.create(TokenType.WHITE_SPACE)

    val STRING_TOKENS = TokenSet.create(MichelsonTypes.STRING)

    val COMMENT_TOKENS = TokenSet.create(
            MichelsonTypes.COMMENT_LINE,
            MichelsonTypes.COMMENT_MULTI_LINE)

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
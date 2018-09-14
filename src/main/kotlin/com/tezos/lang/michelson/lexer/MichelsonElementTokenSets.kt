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

    val STRING_TOKENS = TokenSet.create(MichelsonTypes.STRING_CONTENT, MichelsonTypes.STRING_ESCAPE, MichelsonTypes.STRING_ESCAPE_INVALID)

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

    val ANNOTATIONS = TokenSet.create(
            MichelsonTypes.TYPE_ANNOTATION_TOKEN,
            MichelsonTypes.VAR_ANNOTATION_TOKEN,
            MichelsonTypes.FIELD_ANNOTATION_TOKEN)

    val TYPE_NAMES = TokenSet.create(
            MichelsonTypes.TYPE_NAME,
            MichelsonTypes.TYPE_NAME_COMPARABLE)

    val INTRUCTIONS_TOKENS = TokenSet.create(MichelsonTypes.INSTRUCTION_TOKEN, MichelsonTypes.MACRO_TOKEN)

    val LITERAL_TOKENS = TokenSet.create(
            MichelsonTypes.NONE,
            MichelsonTypes.TRUE,
            MichelsonTypes.FALSE,
            MichelsonTypes.UNIT,
            MichelsonTypes.INT)
}
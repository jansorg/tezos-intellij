package com.tezos.lang.michelson.parser

import com.intellij.lang.PsiBuilder
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonTypes.*

/**
 * Parser util referenced by /grammars/michelson.bnf.
 * The methods have to be @JvmStatic because they're referenced from Java code.
 *
 * @author jansorg
 */
object MichelsonParserUtil : GeneratedParserUtilBase() {
    private val STOP_TOKENS_INSTRUCTION = TokenSet.create(SEMI, LEFT_CURLY, RIGHT_CURLY, MACRO_TOKEN)
    private val STOP_TOKENS_NESTED_TAG = TokenSet.create(RIGHT_PAREN, SEMI, LEFT_CURLY)
    private val STOP_TOKENS_TAG = TokenSet.create(LEFT_PAREN, SEMI, INSTRUCTION_TOKEN, LEFT_CURLY, RIGHT_CURLY)
    private val BEFORE_TAG = TokenSet.create(LEFT_CURLY, RIGHT_CURLY, SEMI, LEFT_PAREN)

    @JvmStatic
    fun parse_instruction_block(builder: PsiBuilder, level: Int, instructionParser: GeneratedParserUtilBase.Parser): Boolean {
        if (!consumeToken(builder, LEFT_CURLY)) {
            // safe guard: this is not a block, return
            return false
        }

        // handle special case of {} and {;}
        val current = builder.tokenType
        if (current === RIGHT_CURLY) {
            builder.advanceLexer()
            return true
        } else if (current === SEMI && builder.lookAhead(1) === RIGHT_CURLY) {
            builder.advanceLexer()
            builder.advanceLexer()
            return true
        }

        var error = false
        while (!GeneratedParserUtilBase.eof(builder, level)) {
            error = !instructionParser.parse(builder, level + 1)
            if (error) {
                break
            }

            val consumedSemi = consumeToken(builder, SEMI)
            if (consumeToken(builder, RIGHT_CURLY)) {
                break
            }

            if (!consumedSemi) {
                builder.error("expected ';'")
            }
        }

        // do recovery only when the instruction parsing returned an error
        if (error) {
            var shownError = false
            while (!GeneratedParserUtilBase.eof(builder, level) && instruction_block_recover_while(builder)) {
                if (shownError) {
                    builder.advanceLexer()
                } else {
                    val marker = builder.mark()
                    builder.advanceLexer()
                    marker.error("Unexpected token")

                    shownError = true
                }
            }
        }

        return true
    }

    /**
     * Called in a recover_while rule.
     * A recoverWhile is even called when a rule wasn't matching at all.
     *
     * @param builder the PSI builder
     * @param level   the current nesting level
     * @return `false` when the recoverWhile should stop to consume tokens, `true` to proceed.
     */
    @JvmStatic
    fun instruction_recover_while(builder: PsiBuilder, level: Int): Boolean {
        val current = builder.lookAhead(0)
        if (STOP_TOKENS_INSTRUCTION.contains(current)) {
            //builder.error("Unexpected token (instruction)")
            return false
        }

        val next = builder.lookAhead(1)
        return !STOP_TOKENS_INSTRUCTION.contains(next)
    }

    private fun instruction_block_recover_while(builder: PsiBuilder): Boolean {
        val current = builder.lookAhead(0)
        val next = builder.lookAhead(1)
        return (current != SEMI && current != LEFT_CURLY) || (current == SEMI && next == RIGHT_CURLY)
    }

    private fun rawBackwardsSkippingWhitespace(builder: PsiBuilder, steps: Int): IElementType? {
        var i = steps
        var type = builder.rawLookup(i)
        while (type == WHITE_SPACE) {
            i--
            type = builder.rawLookup(i)
        }
        return type
    }

    private fun rawBackwardsSkippingFirstBefore(builder: PsiBuilder, stopBefore: TokenSet): IElementType? {
        var i = 0

        var current = builder.rawLookup(i)
        while (current != null) {
            val prev = builder.rawLookup(i - 1)
            if (stopBefore.contains(prev)) {
                return current
            }

            i--
            current = builder.rawLookup(i)
        }

        return null
    }

    @JvmStatic
    fun tag_data_recover_while(builder: PsiBuilder, level: Int): Boolean {
        if (builder.eof()) {
            return false
        }

        val current = builder.tokenType
        // we have to stop recovering after the left paren in "Pair ("
        if (current == LEFT_PAREN) {
            return false
        }

        val isParsingTag = rawBackwardsSkippingFirstBefore(builder, BEFORE_TAG) == TAG
        if (!isParsingTag) {
            return false
        }

        val isNested = rawBackwardsSkippingFirstBefore(builder, TokenSet.create(LEFT_PAREN)) == TAG
        val stop = when (isNested) {
            true -> STOP_TOKENS_NESTED_TAG.contains(current)
            false -> STOP_TOKENS_TAG.contains(current)
        }

        if (!stop && current != WHITE_SPACE) {
            val m = builder.mark()
            builder.advanceLexer()
            m.error("Unexpected token")
        }
        return !stop
    }
}

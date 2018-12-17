@file:Suppress("FunctionName")

package com.tezos.lang.michelson.parser

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.MichelsonTypes.*
import com.tezos.lang.michelson.psi.isWhitespace

/**
 * Parser util referenced by /grammars/michelson.bnf.
 * The methods have to be @JvmStatic because they're referenced from Java code.
 *
 * @author jansorg
 */
object MichelsonParserUtil : GeneratedParserUtilBase() {
    private val STOP_TOKENS_SECTION = TokenSet.create(SECTION_NAME, LEFT_CURLY, RIGHT_CURLY, SEMI)

    private val STOP_TOKENS_INSTRUCTION = TokenSet.create(SEMI, LEFT_CURLY, RIGHT_CURLY, MACRO_TOKEN)
    private val STOP_TOKENS_NESTED_TAG = TokenSet.create(RIGHT_PAREN, SEMI, LEFT_CURLY)
    private val STOP_TOKENS_TAG = TokenSet.create(LEFT_PAREN, SEMI, INSTRUCTION_TOKEN, LEFT_CURLY, RIGHT_CURLY)
    private val BEFORE_TAG = TokenSet.create(LEFT_CURLY, RIGHT_CURLY, SEMI, LEFT_PAREN)

    /**
     * Called in a recover_while rule.
     * A recoverWhile is even called when a rule wasn't matching at all.
     *
     * @param builder the PSI builder
     * @param level   the current nesting level
     * @return `false` when the recoverWhile should stop to consume tokens, `true` to proceed.
     */
    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun parse_section_error_aware(builder: PsiBuilder, level: Int, sectionParser: GeneratedParserUtilBase.Parser, unknownSectionParser: GeneratedParserUtilBase.Parser): Boolean {
        val ok = sectionParser.parse(builder, level)
        if (ok) {
            return true
        }

        // stop at end of file or when probably parsing at the end of a nested contract
        if (builder.eof()|| STOP_TOKENS_SECTION.contains(builder.tokenType)) {
            return false
        }

        val offset = builder.currentOffset
        val marker = builder.mark()

        // don't raise an error at the end of a nested contract
        // atm we can't handle that in a better way (missing context information)
        // we mark a token because without the error marker was placed before whitespace preceeding the current token
        val error = builder.mark()
        builder.advanceLexer()
        error.error("<section> expected")

        var next = builder.tokenType
        while (next != null && !STOP_TOKENS_SECTION.contains(next)) {
            builder.advanceLexer()
            next = builder.tokenType
        }

        if (builder.currentOffset > offset) {
            marker.done(MichelsonTypes.UNKNOWN_SECTION)
            return true
        }
        return false
    }

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

    @JvmStatic
    fun parseNestedData(builder: PsiBuilder, level: Int, nestedParser: GeneratedParserUtilBase.Parser): Boolean {
        val current = builder.tokenType
        val next = builder.lookAhead(1)
        if (current != LEFT_PAREN || next != TAG_TOKEN) {
            return false
        }

        var result = consumeToken(builder, LEFT_PAREN)
        if (!result) {
            return false
        }

        result = nestedParser.parse(builder, level + 1)
        if (!result) {
            builder.error("Error parsing nested data")
            // recovery: read up to the closing parent or until a newline is found
            while (builder.tokenType != RIGHT_PAREN && builder.rawLookup(0) != EOL) {
                builder.advanceLexer()
            }
        }

        result = consumeToken(builder, RIGHT_PAREN)
        if (!result) {
            // probably invalid tokens after the initial tag, recover until the next right paren or newline
            builder.error("Expected )")

            while (!builder.eof() && builder.tokenType != RIGHT_PAREN && builder.rawLookup(0) != EOL) {
                builder.advanceLexer()
            }
            // optional read
            consumeToken(builder, RIGHT_PAREN)
        }

        // the rule matched, even with recovery
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
    @Suppress("UNUSED_PARAMETER")
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

    @Suppress("UNUSED_PARAMETER")
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

        if (!stop && !current.isWhitespace()) {
            val m = builder.mark()
            builder.advanceLexer()
            m.error("Unexpected token")
        }
        return !stop
    }
}

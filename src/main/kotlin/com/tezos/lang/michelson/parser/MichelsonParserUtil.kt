package com.tezos.lang.michelson.parser

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonTypes.*

/**
 * Parser util referenced by /grammars/michelson.bnf.
 * The methods have to be @JvmStatic because they're referenced from Java code.
 *
 * @author jansorg
 */
object MichelsonParserUtil : GeneratedParserUtilBase() {
    private val STOP_TOKENS = TokenSet.create(SEMI, LEFT_CURLY, RIGHT_CURLY, MACRO_TOKEN)

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
        while (error && !GeneratedParserUtilBase.eof(builder, level) && instruction_block_recover_while(builder)) {
            builder.advanceLexer()
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
        val next = builder.lookAhead(1)

        val currentStops = STOP_TOKENS.contains(current)
        if (currentStops) {
            return false
        }

        return !STOP_TOKENS.contains(next)
    }

    private fun instruction_block_recover_while(builder: PsiBuilder): Boolean {
        val current = builder.lookAhead(0)
        return current !== SEMI && current !== LEFT_CURLY
    }
}

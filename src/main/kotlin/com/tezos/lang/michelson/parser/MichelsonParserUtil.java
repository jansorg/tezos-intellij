package com.tezos.lang.michelson.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import static com.tezos.lang.michelson.MichelsonTypes.*;

/**
 * @author jansorg
 */
public class MichelsonParserUtil extends GeneratedParserUtilBase {
    private static TokenSet STOP_TOKENS = TokenSet.create(SEMI, LEFT_CURLY, RIGHT_CURLY, MACRO_TOKEN);

    protected static boolean parse_instruction_block(PsiBuilder builder, int level, Parser instructionParser) {
        if (!consumeToken(builder, LEFT_CURLY)) {
            // this is not block, return
            return false;
        }

        // handle special case of {} and {;}
        IElementType current = builder.getTokenType();
        if (current == RIGHT_CURLY) {
            builder.advanceLexer();
            return true;
        } else if (current == SEMI && builder.lookAhead(1) == RIGHT_CURLY) {
            builder.advanceLexer();
            builder.advanceLexer();
            return true;
        }

        boolean error = false;
        while (!eof(builder, level)) {
            error = !instructionParser.parse(builder, level + 1);
            if (error) {
                break;
            }

            boolean consumedSemi = consumeToken(builder, SEMI);
            if (consumeToken(builder, RIGHT_CURLY)) {
                break;
            }

            if (!consumedSemi) {
                builder.error("expected ';'");
            }
        }

        // do recovery only when the instruction parsing returned an error
        if (error) {
            while (!eof(builder, level) && instruction_block_recover_while(builder, level)) {
                builder.advanceLexer();
            }
        }

        return true;
    }

    /**
     * Called in a recover_while rule.
     * A recoverWhile is even called when a rule wasn't matching at all.
     *
     * @param builder the PSI builder
     * @param level   the current nesting level
     * @return {@code false} when the recoverWhile should stop to consume tokens, {@code true} to proceed.
     */
    protected static boolean instruction_recover_while(PsiBuilder builder, int level) {
        IElementType current = builder.lookAhead(0);
        IElementType next = builder.lookAhead(1);

        boolean currentStops = STOP_TOKENS.contains(current);
        boolean nextStops = STOP_TOKENS.contains(next);

//        if (hasCurrent && hasNext) {
//            return true;
//        }

        if (currentStops) {
            return false;
        }

        if (nextStops) {
            return false;
        }

        return true;
    }

    protected static boolean instruction_block_recover_while(PsiBuilder builder, int level) {
        IElementType current = builder.lookAhead(0);
        IElementType next = builder.lookAhead(1);

        boolean stop = current == SEMI || current == LEFT_CURLY;
        return !stop;
    }
}

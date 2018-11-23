package com.tezos.lang.michelson.editor.completion

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.StandardPatterns
import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.MichelsonTypes.*
import com.tezos.lang.michelson.editor.completion.provider.*

/**
 * @author jansorg
 */
class MichelsonCompletionContributor : AbstractOriginalPosCompletionContributor() {
    private companion object {
        val AFTER_INSTRUCTION_TOKEN_LEAF = PlatformPatterns.psiElement().afterLeaf(PATTERN_INSTRUCTION_TOKEN)!!
        val AFTER_INSTRUCTION_ELEMENT = PlatformPatterns.psiElement().afterSibling(INSTRUCTION_ELEMENT_PATTERN)!!
        val SIMPLE_TYPE_PATTERN = PlatformPatterns.or(
                PlatformPatterns.psiElement().inside(INSTRUCTION_ELEMENT_PATTERN)
                        .andNot(PATTERN_INSTRUCTION_TOKEN)
                        .andNot(PATTERN_INSIDE_DATA_ELEMENT)
                        .andNot(PlatformPatterns.psiElement().afterLeaf(PATTERN_TAG_TOKEN)),
                AFTER_INSTRUCTION_TOKEN_LEAF.andNot(PATTERN_INSIDE_DATA_ELEMENT)
        )!!

        val TAG_AFTER_INSTRUCTION_TOKEN = PlatformPatterns.psiElement()
                .withElementType(TokenSet.create(TokenType.WHITE_SPACE, TAG_TOKEN, MichelsonTypes.TAG))
                .and(AFTER_INSTRUCTION_TOKEN_LEAF)!!

        val TAG = StandardPatterns.or(
                // e.g. "PUSH |"
                TAG_AFTER_INSTRUCTION_TOKEN,
                // e.g. "PUSH | ..." where the parser did error recovery
                AFTER_INSTRUCTION_ELEMENT,
                // inside a tag, e.g. "PUSH Tr|ue"
                PATTERN_TAG_TOKEN,
                // e.g. PUSH (|)
                PlatformPatterns.psiElement(LEFT_PAREN).inside(INSTRUCTION_ELEMENT_PATTERN),
                // e.g. "PUSH int T|" where the parser added an error token before T after error recovery (T is an instruction token here)
                PATTERN_INSTRUCTION_OR_DATA_TOKEN.and(AFTER_ERROR_LEAF_SKIPPING_WS),
                // e.g. "PUSH int |T" (T is an instruction token here)
                AFTER_ERROR_LEAF_SKIPPING_WS.beforeLeaf(PATTERN_INSTRUCTION_TOKEN),
                // e.g. "PUSH int |Tr" (Tr is a tag here)
                WHITESPACE_PATTERN.beforeLeaf(PATTERN_TAG_TOKEN)
        )!!

        val COMPLEX_TAG = PlatformPatterns.or(LEFT_PAREN_PATTERN, PlatformPatterns.psiElement(TAG_TOKEN).afterLeafSkipping(WHITESPACE_PATTERN, LEFT_PAREN_PATTERN))!!
    }

    init {
        // sections
        extend(null, TOPLEVEL_PATTERN, SectionCompletion())

        // instructions
        val commandEndPattern = PlatformPatterns.psiElement().withElementType(TokenSet.create(LEFT_CURLY, SEMI))
        val instructionPlace = StandardPatterns.or(
                commandEndPattern,
                PlatformPatterns.psiElement().afterLeaf(commandEndPattern),
                PlatformPatterns.psiElement(INSTRUCTION_TOKEN).andNot(AFTER_ERROR_LEAF_SKIPPING_WS)
        )
        extendOriginal(null, instructionPlace, InstructionNameCompletion())
        extendOriginal(null, instructionPlace, MacroNameCompletion())

        // types
        extendOriginal(null, SIMPLE_TYPE_PATTERN, SimpleTypeCompletion())
        extendOriginal(null, LEFT_PAREN_PATTERN.and(SIMPLE_TYPE_PATTERN), NestedTypeCompletion())

        // tags
        extendOriginal(null, TAG, SimpleTagCompletion())
        extendOriginal(null, COMPLEX_TAG, NestedTagCompletion())
    }
}


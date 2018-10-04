package com.tezos.lang.michelson.editor.completion

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.StandardPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonTypes.*
import com.tezos.lang.michelson.editor.completion.provider.*

/**
 * @author jansorg
 */
class MichelsonCompletionContributor : AbstractOriginalPosCompletionContributor() {
    init {
        // sections
        extend(null, TOPLEVEL_PATTERN, SectionCompletion())

        // instructions
        val instructionPlace = StandardPatterns.or(
                WHITESPACE_PATTERN.afterLeaf(PATTERN_LEFT_CURLY),
                PlatformPatterns.psiElement().withElementType(TokenSet.create(LEFT_CURLY, INSTRUCTION_TOKEN, SEMI)))
        extendOriginal(null, instructionPlace, InstructionNameCompletion())

        // types
        val AFTER_INSTRUCTION_TOKEN_LEAF = PlatformPatterns.psiElement().afterLeaf(PATTERN_INSTRUCTION_TOKEN)
        val AFTER_INSTRUCTION_ELEMENT = PlatformPatterns.psiElement().afterSibling(INSTRUCTION_ELEMENT_PATTERN)
        val SIMPLE_TYPE_PATTERN = PlatformPatterns.or(
                PlatformPatterns.psiElement().inside(INSTRUCTION_ELEMENT_PATTERN)
                        .andNot(PATTERN_INSTRUCTION_TOKEN)
                        .andNot(PATTERN_INSIDE_DATA_ELEMENT),
                AFTER_INSTRUCTION_TOKEN_LEAF.andNot(PATTERN_INSIDE_DATA_ELEMENT)
        )
        extendOriginal(null, SIMPLE_TYPE_PATTERN, SimpleTypeCompletion())
        extendOriginal(null, LEFT_PAREN_PATTERN.and(SIMPLE_TYPE_PATTERN), NestedTypeCompletion())

        // tags
        val TAG_AFTER_INSTRUCTION_TOKEN = PlatformPatterns.psiElement().withElementType(TokenSet.create(TokenType.WHITE_SPACE, TAG_DATA, TAG))
                .and(AFTER_INSTRUCTION_TOKEN_LEAF)

        val tag = StandardPatterns.or(TAG_AFTER_INSTRUCTION_TOKEN,
                AFTER_INSTRUCTION_ELEMENT,
                PlatformPatterns.psiElement(TAG),
                PlatformPatterns.psiElement(LEFT_PAREN).inside(INSTRUCTION_ELEMENT_PATTERN))
        extendOriginal(null, tag, SimpleTagCompletion())

        val complexTag = PlatformPatterns.or(LEFT_PAREN_PATTERN, PlatformPatterns.psiElement(TAG).afterLeafSkipping(WHITESPACE_PATTERN, LEFT_PAREN_PATTERN))
        extendOriginal(null, complexTag, NestedTagCompletion())
    }
}


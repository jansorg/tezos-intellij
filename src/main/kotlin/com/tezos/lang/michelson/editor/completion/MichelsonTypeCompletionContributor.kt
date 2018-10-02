package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import com.tezos.lang.michelson.psi.PsiInstruction

/**
 * @author jansorg
 */
class MichelsonTypeCompletionContributor : AbstractInstructionCompletionContributor(atInstructionStart = false) {
    init {
        val AFTER_LEFT_PAREN_PATTERN = PlatformPatterns.psiElement().afterLeaf("(")

        val SIMPLE_TYPE_PATTERN = PlatformPatterns.psiElement()
                .inside(PlatformPatterns.psiElement(PsiInstruction::class.java))
                .andNot(PATTERN_INSTRUCTION_TOKEN)
                .andNot(PATTERN_WITH_BLOCK_PARENT)
        val COMPLEX_TYPE_PATTERN = PATTERN_IN_MICHELSON_FILE.and(AFTER_LEFT_PAREN_PATTERN).and(SIMPLE_TYPE_PATTERN)

        extend(null, SIMPLE_TYPE_PATTERN, MichelsonSimpleTypeCompletion())
        extend(null, COMPLEX_TYPE_PATTERN, MichelsonNestedTypeCompletion())

        val SIMPLE_TAG_PATTERN = PlatformPatterns.psiElement()
                .inside(PlatformPatterns.psiElement(PsiInstruction::class.java))
                .with(object : PatternCondition<PsiElement?>("debug") {
                    override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
                        return true
                    }
                })
                .andNot(PATTERN_INSTRUCTION_TOKEN)
                .andNot(PATTERN_WITH_BLOCK_PARENT)

        val COMPLEX_TAG_PATTERN = PATTERN_IN_MICHELSON_FILE.and(AFTER_LEFT_PAREN_PATTERN).and(SIMPLE_TAG_PATTERN)

        extend(null, SIMPLE_TAG_PATTERN, MichelsonSimpleTagCompletion())
        extend(null, COMPLEX_TAG_PATTERN, MichelsonNestedTagCompletion())
    }

    override fun beforeCompletion(context: CompletionInitializationContext) {
        context.dummyIdentifier = "int" //use a lowercase dummy which won't break the parser
    }
}


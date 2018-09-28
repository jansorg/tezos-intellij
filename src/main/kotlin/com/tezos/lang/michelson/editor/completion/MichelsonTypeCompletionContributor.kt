package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.patterns.PlatformPatterns
import com.tezos.lang.michelson.psi.PsiInstruction

/**
 * @author jansorg
 */
class MichelsonTypeCompletionContributor : CompletionContributor() {
    init {
        val AFTER_LEFT_PAREN_PATTERN = PlatformPatterns.psiElement().afterLeaf("(")

        val SIMPLE_TYPE_PATTERN = PlatformPatterns.psiElement()
                .inside(PlatformPatterns.psiElement(PsiInstruction::class.java))
                .andNot(PATTERN_INSTRUCTION_TOKEN)
                .andNot(PATTERN_WITH_BLOCK_PARENT)

        val COMPLEX_TYPE_PATTERN = PATTERN_IN_MICHELSON_FILE.and(AFTER_LEFT_PAREN_PATTERN).and(SIMPLE_TYPE_PATTERN)

        extend(null, SIMPLE_TYPE_PATTERN, MichelsonSimpleTypeNameCompletion())
        extend(null, COMPLEX_TYPE_PATTERN, MichelsonComplexTypeNameCompletion())
    }

    override fun beforeCompletion(context: CompletionInitializationContext) {
        context.dummyIdentifier = "int" //use a lowercase dummy which won't break the parser
    }
}


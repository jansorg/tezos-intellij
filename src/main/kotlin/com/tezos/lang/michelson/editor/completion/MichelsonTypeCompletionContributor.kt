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
class MichelsonTypeCompletionContributor : CompletionContributor() {
    init {
        extend(null, PlatformPatterns.psiElement()
                .inside(PlatformPatterns.psiElement(PsiInstruction::class.java))
                .andNot(PATTERN_INSTRUCTION_TOKEN)
                .andNot(PATTERN_WITH_BLOCK_PARENT)
                .with(object : PatternCondition<PsiElement?>("debug") {
                    override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
                        return true
                    }
                }), MichelsonSimpleTypeNameCompletion())
//        extend(null, PlatformPatterns.psiElement(), MichelsonComplexTypeNameCompletion())
    }

    override fun beforeCompletion(context: CompletionInitializationContext) {
        context.dummyIdentifier = "int" //use a lowercase dummy which won't break the parser
    }
}


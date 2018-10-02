package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.util.PsiTreeUtil
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
abstract class AbstractInstructionCompletionContributor(val atInstructionStart: Boolean) : CompletionContributor() {
    /**
     * If there's an instruction token as previous leaf and no instruction delimiter was found then we skip completions
     * It's probably something invalid like "PUSH int D<caret>"
     */
    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (parameters.position.language != MichelsonLanguage) {
            return
        }

        // we have to use the original position because an extra token can mess up the PSI structure because of the instruction parsing recovery
        var leaf = parameters.originalPosition
        while (leaf != null) {
            leaf = PsiTreeUtil.prevVisibleLeaf(leaf)

            val leafType = leaf?.node?.elementType
            if (leafType == MichelsonTypes.INSTRUCTION_TOKEN) {
                if (!atInstructionStart) {
                    super.fillCompletionVariants(parameters, result)
                }
                return
            }

            if (leafType == MichelsonTypes.SEMI || leafType == MichelsonTypes.LEFT_CURLY) {
                if (atInstructionStart) {
                    super.fillCompletionVariants(parameters, result)
                }
                return
            }
        }
    }
}


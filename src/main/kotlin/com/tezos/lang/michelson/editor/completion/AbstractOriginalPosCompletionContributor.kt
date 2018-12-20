package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.*
import com.intellij.openapi.util.Pair
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.intellij.util.containers.MultiMap
import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
abstract class AbstractOriginalPosCompletionContributor : CompletionContributor() {
    private val originalPatternMap = MultiMap<CompletionType, Pair<ElementPattern<out PsiElement>, CompletionProvider<CompletionParameters>>>()
    protected fun extendOriginal(type: CompletionType?, place: ElementPattern<out PsiElement>, provider: CompletionProvider<CompletionParameters>) {
        originalPatternMap.putValue(type, Pair(place, provider))
    }

    protected fun fillOriginalCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        // we prefer the left token at the caret position in a case of Token1<cursor>Token2, including whitespace token
        var pos = parameters.originalPosition ?: return
        if (parameters.offset == pos.textOffset) {
            val prevPos = PsiTreeUtil.prevLeaf(pos, true)
            if (prevPos != null && parameters.offset == prevPos.textRange.endOffset) {
                pos = prevPos
            }
        }

        // check for completion type first, then for null, i.e. all types allowed
        for (type in listOf(parameters.completionType, null)) {
            for (pair in originalPatternMap.get(type)) {
                val context = ProcessingContext()

                if (pair.first.accepts(pos, context)) {
                    pair.second.addCompletionVariants(parameters, context, result)
                    if (result.isStopped) {
                        return
                    }
                }
            }
        }
    }

    /**
     * If there's an instruction token as previous leaf and no instruction delimiter was found then we skip completions
     * It's probably something invalid like "PUSH int D<caret>"
     */
    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (parameters.position.language != MichelsonLanguage) {
            return
        }

        fillOriginalCompletionVariants(parameters, result)
        super.fillCompletionVariants(parameters, result)
    }
}


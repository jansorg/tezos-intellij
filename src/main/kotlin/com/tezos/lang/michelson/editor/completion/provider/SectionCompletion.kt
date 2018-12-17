package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.tezos.lang.michelson.psi.PsiContract
import com.tezos.lang.michelson.psi.PsiSectionType

internal class SectionCompletion : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val skippedSections: Set<PsiSectionType> = when (parameters.completionType) {
            CompletionType.SMART -> {
                val contract = PsiTreeUtil.findFirstParent(parameters.position) { it is PsiContract } as? PsiContract
                contract?.sections?.map { it.sectionType }?.toSet() ?: emptySet()
            }
            else -> emptySet()
        }

        PsiSectionType.completionValues.filter { it !in skippedSections }.forEach {
            val name = it.codeName()
            val item = LookupElementBuilder.create("$name ")
                    .withPresentableText(name)
                    .withTypeText("section", true)
            result.addElement(item)
        }
    }
}

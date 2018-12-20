package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.lang.ParameterType
import com.tezos.lang.michelson.psi.*

internal class TagCompletion(val completeSimple: Boolean, val completeComplex: Boolean) : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        when {
            parameters.completionType == CompletionType.BASIC -> basicCompletion(result)
            parameters.completionType == CompletionType.SMART -> smartCompletion(parameters, result)
        }
    }

    private fun basicCompletion(result: CompletionResultSet) {
        MichelsonLanguage.TAGS_METAS.filter { completeSimple != it.isComplex() && completeComplex == it.isComplex() }.flatMap { it.names() }.forEach { name ->
            val item = LookupElementBuilder.create(name).withTypeText("tag", true)
            result.addElement(item)
        }
    }

    private fun smartCompletion(params: CompletionParameters, result: CompletionResultSet) {
        val position = params.originalPosition ?: return

        val type = findExpectedParameterType(position)
        if (type != ParameterType.DATA) return

        MichelsonLanguage.TAGS_METAS.filter { completeSimple != it.isComplex() && completeComplex == it.isComplex() }.flatMap { it.names() }.forEach { name ->
            val item = LookupElementBuilder.create(name).withTypeText("tag", true)
            result.addElement(item)
        }
    }
}
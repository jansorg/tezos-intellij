package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.tezos.lang.michelson.lang.MichelsonLanguage

internal class SimpleTagCompletion : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        MichelsonLanguage.TAGS_METAS.filterNot { it.isComplex() }.flatMap { it.names() }.forEach { name ->
            val item = LookupElementBuilder.create(name).withTypeText("tag", true)
            result.addElement(item)
        }
    }
}

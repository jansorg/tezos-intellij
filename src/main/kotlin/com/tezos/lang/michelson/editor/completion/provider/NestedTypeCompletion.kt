package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.tezos.lang.michelson.lang.MichelsonLanguage

class NestedTypeCompletion : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        if (parameters.completionType == CompletionType.BASIC) {
            for (name in MichelsonLanguage.TYPES_NESTED) {
                val item = LookupElementBuilder.create(name).withTypeText("type", true);
                result.addElement(item)
            }
        }
    }
}

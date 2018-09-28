package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.tezos.lang.michelson.lang.MichelsonLanguage

class MichelsonComplexTypeNameCompletion : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        for (name in MichelsonLanguage.COMPARABLE_TYPES) {
            val item = LookupElementBuilder.create("$name ")
                    .withPresentableText(name)
                    .withTypeText("comparable type")
            result.addElement(item)
        }

        for (name in MichelsonLanguage.TYPES) {
            val item = LookupElementBuilder.create("$name ")
                    .withPresentableText(name)
                    .withTypeText("type")
            result.addElement(item)
        }
    }
}

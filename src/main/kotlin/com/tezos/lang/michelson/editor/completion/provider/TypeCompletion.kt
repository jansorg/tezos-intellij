package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.tezos.lang.michelson.lang.MichelsonLanguage

class TypeCompletion(val insertSimple : Boolean , val insertNesting: Boolean) : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        when {
            parameters.completionType == CompletionType.BASIC -> basicCompletion(result)
        }
    }

    private fun basicCompletion(result: CompletionResultSet) {
        for (type in MichelsonLanguage.TYPES) {
            if (type.isSimple == insertSimple || type.isNesting == insertNesting) {
                val typeName = if (type.isComparable) "comparable type" else "type"
                val item = LookupElementBuilder.create(type.name).withTypeText(typeName, true)
                result.addElement(item)
            }
        }
    }
}

package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.lang.ParameterType
import com.tezos.lang.michelson.lang.type.TypeMetadata

class TypeCompletion(private val insertSimple: Boolean, private val insertNesting: Boolean) : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        when {
            parameters.completionType == CompletionType.BASIC -> basicCompletion(result)
            parameters.completionType == CompletionType.SMART -> smartCompletion(parameters, result)
        }
    }

    private fun basicCompletion(result: CompletionResultSet) {
        for (type in MichelsonLanguage.TYPES) {
            addLookupItem(type, result)
        }
    }

    private fun smartCompletion(params: CompletionParameters, result: CompletionResultSet) {
        val position = params.originalPosition ?: return
        val type = findExpectedParameterType(position)
        if (type != ParameterType.TYPE) {
            return
        }

        for (it in MichelsonLanguage.TYPES) {
            addLookupItem(it, result)
        }
    }

    private fun addLookupItem(type: TypeMetadata, result: CompletionResultSet) {
        if (type.isSimple == insertSimple || type.isNesting == insertNesting) {
            val typeName = if (type.isComparable) "comparable type" else "type"
            val item = LookupElementBuilder.create(type.name).withTypeText(typeName, true)
            result.addElement(item)
        }
    }
}

package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.tezos.client.stack.MichelsonStack
import com.tezos.client.stack.MichelsonStackType
import com.tezos.lang.michelson.lang.LangTypes
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.lang.instruction.InstructionMetadata

internal class InstructionNameCompletion : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        when {
            parameters.completionType == CompletionType.BASIC -> addBasicCompletions(result)
            parameters.completionType == CompletionType.SMART -> addSmartCompletions(result, parameters)
        }
    }

    private fun addBasicCompletions(result: CompletionResultSet) {
        for (meta in MichelsonLanguage.INSTRUCTIONS) {
            add(meta, result)
        }
    }

    private fun addSmartCompletions(result: CompletionResultSet, parameters: CompletionParameters) {
        val matching = locateStackTransformation(parameters.position, parameters.editor.document)
        if (matching != null) {
            addMatchingInstructions(result, matching.before)
        }
    }

    private fun addMatchingInstructions(result: CompletionResultSet, stack: MichelsonStack) {
        for (instr in MichelsonLanguage.INSTRUCTIONS) {
            if (!instr.isAvailable(stack)) {
                continue
            }

            try {
                // pass dummy args for parameters expected by the instruction
                val args = repeated(instr.parameters.size, LangTypes.ANY)

                val newStack = instr.transformStack(stack, args)
                val item = LookupElementBuilder.create(instr.name).withTypeText(newStack.top?.type?.asString(true)
                        ?: "<empty stack>", true)
                result.addElement(item)
            } catch (e: UnsupportedOperationException) {
                // skip this instruction
            }
        }
    }

    private fun add(meta: InstructionMetadata, result: CompletionResultSet) {
        val item = LookupElementBuilder.create(meta.name).withTypeText("instruction", true);
        result.addElement(item)
    }
}

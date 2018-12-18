package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.tezos.client.stack.MichelsonStack
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.psi.MichelsonPsiUtil
import com.tezos.lang.michelson.stackInfo.MichelsonStackInfoManager

internal class InstructionNameCompletion : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        when {
            parameters.completionType == CompletionType.BASIC -> addBasicCompletions(result)
            parameters.completionType == CompletionType.SMART -> addSmartCompletions(result, parameters)
        }
    }

    private fun addBasicCompletions(result: CompletionResultSet) {
        for (meta in MichelsonLanguage.INSTRUCTIONS) {
            val item = LookupElementBuilder.create(meta.name).withTypeText("instruction", true);
            result.addElement(item)
        }
    }

    private fun addSmartCompletions(result: CompletionResultSet, parameters: CompletionParameters) {
        val doc = parameters.editor.document
        val stackInfo = MichelsonStackInfoManager.getInstance(parameters.editor.project).stackInfo(doc)
        if (stackInfo == null || !stackInfo.isStack) {
            for (instr in MichelsonLanguage.INSTRUCTIONS) {
                addTypedNames(result, instr.dynamicNames(MichelsonStack.EMPTY), null)
            }
            return
        }

        val stack = stackInfo.getStackOrThrow()

        // find the previous instruction, i.e. the previous sibling at the current offset
        // the stack produced by that instruction is the input for our macro completion
        val offset = when {
            MichelsonPsiUtil.isFirstCodeChild(parameters.originalPosition, parameters.offset) -> parameters.originalPosition?.textOffset
            else -> MichelsonPsiUtil.findPrevInstruction(parameters.position)?.textOffset
        }

        if (offset != null) {
            val matching = stack.elementAt(offset)
            if (matching != null) {
                val inputStack = matching.after

                for (instr in MichelsonLanguage.INSTRUCTIONS.ACROS) {
                    addTypedNames(result, instr.dynamicNames(inputStack), inputStack.top)
                }
            }
        }
}

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
            add(meta, result)
        }
    }

    private fun addSmartCompletions(result: CompletionResultSet, parameters: CompletionParameters) {
        val doc = parameters.editor.document
        val stackInfo = MichelsonStackInfoManager.getInstance(parameters.editor.project).stackInfo(doc)
        if (stackInfo == null || !stackInfo.isStack) {
            addSmartInstructions(result, MichelsonStack.EMPTY)
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
                addSmartInstructions(result, matching.before)
            }
        }
    }

    private fun addSmartInstructions(result: CompletionResultSet, stack: MichelsonStack) {
        for (instr in MichelsonLanguage.INSTRUCTIONS) {
            if (instr.isAvailable(stack)) {
                try {
                    val args = mutableListOf<MichelsonStackType>()
                    for (i in 0 until instr.parameters.size) {
                        args.add(LangTypes.ANY)
                    }

                    val newStack = instr.transformStack(stack, args)
                    val item = LookupElementBuilder.create(instr.name).withTypeText(newStack.top?.type?.asString(true)
                            ?: "<empty stack>", true)
                    result.addElement(item)
                } catch (e: UnsupportedOperationException) {
                    // skip this instruction
                }
            }
        }
    }

    private fun add(meta: InstructionMetadata, result: CompletionResultSet) {
        val item = LookupElementBuilder.create(meta.name).withTypeText("instruction", true);
        result.addElement(item)
    }
}

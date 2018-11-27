package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.tezos.client.stack.MichelsonStack
import com.tezos.client.stack.MichelsonStackFrame
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.lang.macro.DynamicMacroName
import com.tezos.lang.michelson.psi.MichelsonPsiUtil
import com.tezos.lang.michelson.stackInfo.MichelsonStackInfoManager

internal class MacroNameCompletion : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        when (parameters.completionType) {
            CompletionType.BASIC -> basic(result)
            CompletionType.SMART -> smart(parameters, result)
            else -> {
            }
        }
    }

    /**
     * Suggest statis macro names for basic completion
     */
    private fun basic(result: CompletionResultSet) {
        for (macro in MichelsonLanguage.MACROS) {
            addNames(result, macro.staticNames())
        }
    }

    /**
     * Suggest dynamic macro names, which are based on the current stack, for smart completion.
     */
    private fun smart(parameters: CompletionParameters, result: CompletionResultSet) {
        val doc = parameters.editor.document
        val stackInfo = MichelsonStackInfoManager.getInstance(parameters.editor.project).stackInfo(doc)
        if (stackInfo == null || !stackInfo.isStack) {
            for (macro in MichelsonLanguage.MACROS) {
                addTypedNames(result, macro.dynamicNames(MichelsonStack.EMPTY), null)
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

                for (macro in MichelsonLanguage.MACROS) {
                    addTypedNames(result, macro.dynamicNames(inputStack), inputStack.top)
                }
            }
        }
    }

    private fun addNames(result: CompletionResultSet, collection: Collection<String>) {
        for (name in collection) {
            val item = LookupElementBuilder.create(name).withTypeText("macro", true);
            result.addElement(item)
        }
    }

    private fun addTypedNames(result: CompletionResultSet, collection: Collection<DynamicMacroName>, top: MichelsonStackFrame?) {
        for (name in collection) {
            var item = LookupElementBuilder.create(name.name)
            //fixme add custom renderer?

            // renders the result stack type, it's grayed if it's the same as the current stack type
            if (name.stackType != null) {
                item = item.withTypeText(name.stackType.asString(true), name.stackType == top?.type)
            }

/*
            if (name.accessedType != null) {
                item = item.appendTailText(" on ", true).appendTailText(name.accessedType.asString(true), true)
            }
*/

            result.addElement(item)
        }
    }
}

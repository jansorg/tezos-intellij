package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.intellij.util.ui.tree.TreeUtil
import com.tezos.lang.michelson.MichelsonTypes
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

        val type = findExpectedParameterType(position, params.offset)
        if (type != ParameterType.DATA) return

        MichelsonLanguage.TAGS_METAS.filter { completeSimple != it.isComplex() && completeComplex == it.isComplex() }.flatMap { it.names() }.forEach { name ->
            val item = LookupElementBuilder.create(name).withTypeText("tag", true)
            result.addElement(item)
        }
    }
}

/**
 * Finds out which parameter type is allowed at the given offset and PSI element.
 * E.g. for "PUSH <caret>" a type is expected and for "PUSH int <caret>" a literal value is expected.
 * @return The type of the expected parameter, if available. null is returned if not instruction could be found or if the instruction is not supporting a parameter at this position
 */
fun findExpectedParameterType(position: PsiElement, offset: Int): ParameterType? {
    // locate the instruction name token
    // we can't use the PSI hierarchy here because incomplete instructions won't have a complete parent hierarchy
    var name: PsiElement? = when {
        // whitespace might be after an instruction, as in "PUSH <caret>"
        // but for "PUSH <caret> int" the prev sibling should be used
        position is PsiWhiteSpace && (position.parent !is PsiGenericInstruction && position.parent !is PsiMacroInstruction) -> PsiTreeUtil.prevLeaf(position)
        else -> position.prevSibling
    }

    val parent = PsiTreeUtil.findFirstParent(name) { it is PsiGenericInstruction || it is PsiMacroInstruction }
    while (name != null && name.node.elementType != MichelsonTypes.INSTRUCTION_TOKEN) {
        name = PsiTreeUtil.prevLeaf(name, true)
        if (PsiTreeUtil.findFirstParent(name) { it == parent } == null) {
            name = null
        }
    }

    if (name == null) {
        return null
    }

    val meta = MichelsonLanguage.INSTRUCTIONS.firstOrNull { it.name == name.text } ?: return null

    var pos = 0
    var psi: PsiElement? = when (PsiTreeUtil.findFirstParent(position) { it == parent } != null) {
        true -> position
        false -> PsiTreeUtil.prevVisibleLeaf(position)
    }

    if (psi == null) {
        return null
    }

    psi = PsiTreeUtil.findFirstParent(psi, ::isParamElement) ?: psi
    while (psi != null) {
        if (isParamElement(psi)) {
            pos++
        }

        psi = psi.prevSibling
    }

    return meta.parameters.getOrNull(pos)
}

fun isParamElement(psi: PsiElement): Boolean {
    return psi is PsiType || psi is PsiTag || psi is PsiData
}

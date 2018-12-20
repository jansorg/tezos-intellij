package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
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
    val start: PsiElement? = when {
        // whitespace might be after an instruction, as in "PUSH <caret>"
        // but for "PUSH <caret> int" the prev sibling should be used
        position is PsiWhiteSpace && !isMacroOrGenericInstruction(position.parent) -> PsiTreeUtil.prevLeaf(position)
        else -> PsiTreeUtil.prevVisibleLeaf(position)
    }

    val parent = findParentInstruction(start) ?: return null
//    val instructionNameToken = findPrevLeafInParent(start, parent, MichelsonTypes.INSTRUCTION_TOKEN) ?: return null
//    val meta = MichelsonLanguage.INSTRUCTIONS.firstOrNull { it.name == instructionNameToken.text } ?: return null
    val meta = (parent as? PsiInstructionWithMeta)?.getInstructionMetadata() ?: return null

//    var psi: PsiElement? = when (hasParent(position, parent)) {
//        true -> position
//        false -> PsiTreeUtil.prevVisibleLeaf(position)
//    } ?: return null

    var psi = PsiTreeUtil.prevVisibleLeaf(position)
    psi = PsiTreeUtil.findFirstParent(psi, ::isParamElement) ?: psi

    var pos = 0
    while (psi != null) {
        if (isParamElement(psi)) {
            pos++
        }
        psi = psi.prevSibling
    }

    return meta.parameters.getOrNull(pos)
}

fun findPrevLeafInParent(psi: PsiElement?, parent: PsiElement, type: IElementType?): PsiElement? {
    if (psi == null) {
        return null
    }

    var current: PsiElement? = psi
    while (current != null && current.node.elementType != type) {
        current = PsiTreeUtil.prevLeaf(current, true)
        if (current == null || !hasParent(current, parent)) {
            return null
        }
    }
    return current
}

fun hasParent(psi: PsiElement?, parent: PsiElement?): Boolean {
    return psi != null && parent != null && PsiTreeUtil.findFirstParent(psi) { it == parent } != null
}

fun findParentInstruction(psi: PsiElement?): PsiInstruction? {
    return PsiTreeUtil.findFirstParent(psi, ::isMacroOrGenericInstruction) as? PsiInstruction
}

fun isMacroOrGenericInstruction(psi: PsiElement): Boolean {
    return psi is PsiGenericInstruction || psi is PsiMacroInstruction
}

fun isParamElement(psi: PsiElement): Boolean {
    return psi is PsiType || psi is PsiTag || psi is PsiData
}

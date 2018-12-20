package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.tezos.client.stack.MichelsonStackTransformation
import com.tezos.lang.michelson.lang.ParameterType
import com.tezos.lang.michelson.psi.*
import com.tezos.lang.michelson.psi.MichelsonPsiTreeUtil.findParentInstruction
import com.tezos.lang.michelson.psi.MichelsonPsiTreeUtil.isMacroOrGenericInstruction
import com.tezos.lang.michelson.psi.MichelsonPsiTreeUtil.isParamElement
import com.tezos.lang.michelson.stackInfo.MichelsonStackInfoManager

fun locateStackTransformation(element: PsiElement, doc: Document): MichelsonStackTransformation? {
    val offset = element.textOffset
    return locateStackTransformation(element.project, doc, offset)
}

/**
 * Locate the stack transformation at the given offset in the file.
 * If offset is on an instruction token, then the stack transformation by that instruction is returned.
 * If there`s no instruction at the offset, then null is returned.
 * @author jansorg
 */
fun locateStackTransformation(project: Project, doc: Document, offset: Int): MichelsonStackTransformation? {
    val stackInfo = MichelsonStackInfoManager.getInstance(project).stackInfo(doc)
    if (stackInfo == null || stackInfo.isError) {
        return null
    }

    val stack = stackInfo.getStackOrThrow()
    return stack.elementAt(offset)
}

internal fun <T> repeated(count: Int, item: T): List<T> {
    if (count == 0) {
        return emptyList()
    }

    if (count == 1) {
        return listOf(item)
    }

    val result = mutableListOf<T>()
    for (i in 0 until count) {
        result.add(item)
    }
    return result.toList()
}

/**
 * Finds out which parameter type is allowed at the given offset and PSI element.
 * E.g. for "PUSH <caret>" a type is expected and for "PUSH int <caret>" a literal value is expected.
 * @return The type of the expected parameter, if available. null is returned if not instruction could be found or if the instruction is not supporting a parameter at this position
 */
fun findExpectedParameterType(position: PsiElement): ParameterType? {
    val start: PsiElement? = when {
        position is PsiWhiteSpace && !isMacroOrGenericInstruction(position.parent) -> PsiTreeUtil.prevLeaf(position)
        else -> PsiTreeUtil.prevVisibleLeaf(position)
    }

    val parent = findParentInstruction(start) ?: return null
    val meta = (parent as? PsiInstructionWithMeta)?.getInstructionMetadata() ?: return null

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

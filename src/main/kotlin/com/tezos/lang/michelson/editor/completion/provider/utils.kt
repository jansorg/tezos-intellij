package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.tezos.client.stack.MichelsonStackTransformation
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
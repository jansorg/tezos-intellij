package com.tezos.lang.michelson.editor.completion

import com.intellij.psi.PsiElement
import com.tezos.client.stack.*

/**
 * @author jansorg
 */
fun stackOf(element: PsiElement?, vararg stackTypes: MichelsonStackType): MichelsonStackTransformations {
    val range = element!!.textRange
    val stack = MichelsonStack(stackTypes.map { MichelsonStackFrame(it) })
    return MichelsonStackTransformations(listOf(MichelsonStackTransformation(range.startOffset, range.endOffset, stack, stack)), emptyList())
}

fun stackOf(vararg types: MichelsonStackType): MichelsonStack {
    return MichelsonStack(types.map { MichelsonStackFrame(it) })
}

fun MichelsonStackType.frame() = MichelsonStackFrame(this)

fun String.type(): MichelsonStackType = MichelsonStackType(this)

fun nested(name: String, a: String, b: String): MichelsonStackType = MichelsonStackType(name, listOf(a.type(), b.type()))
fun nested(name: String, a: MichelsonStackType, b: MichelsonStackType): MichelsonStackType = MichelsonStackType(name, listOf(a, b))

fun pair(a: String, b: String) = nested("pair", a, b)
fun pair(a: MichelsonStackType, b: MichelsonStackType) = nested("pair", a, b)

fun option(a: String, b: String) = nested("option", a, b)
fun option(a: MichelsonStackType, b: MichelsonStackType) = nested("option", a, b)

fun or(a: String, b: String) = nested("or", a, b)
fun or(a: MichelsonStackType, b: MichelsonStackType) = nested("or", a, b)
package com.tezos.lang.michelson.psi

/**
 * @author jansorg
 */
import com.intellij.psi.PsiElement
import com.tezos.lang.michelson.parser.PsiVisitor

interface MichelsonComposite : PsiElement {
    fun <R> accept(visitor: PsiVisitor<R>): R
}
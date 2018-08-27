package com.tezos.lang.michelson.psi

/**
 * @author jansorg
 */
import com.intellij.psi.PsiElement

interface MichelsonComposite : PsiElement {
    fun <R> accept(visitor: PsiVisitor<R>): R
}
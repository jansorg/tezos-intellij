package com.tezos.lang.michelson.psi.impl

import com.intellij.psi.impl.source.tree.CompositePsiElement
import com.intellij.psi.tree.IElementType
import com.tezos.lang.michelson.parser.PsiVisitor
import com.tezos.lang.michelson.psi.MichelsonComposite

/**
 * @author jansorg
 */
open class MichelsonCompositeImpl(type: IElementType) : CompositePsiElement(type), MichelsonComposite {
    override fun <R> accept(visitor: PsiVisitor<R>): R {
        return visitor.visitMichelsonComposite(this)
    }
}
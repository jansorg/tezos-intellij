package com.tezos.lang.michelson.parser

import com.intellij.lang.ASTFactory
import com.intellij.psi.impl.source.tree.CompositeElement
import com.intellij.psi.tree.IElementType
import com.tezos.lang.michelson.MichelsonTypes

/**
 * @author jansorg
 */
class MichelsonAstFactory : ASTFactory() {
    override fun createComposite(type: IElementType): CompositeElement {
        return MichelsonTypes.Factory.createElement(type)
    }
}
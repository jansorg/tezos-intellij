package com.tezos.lang.michelson.parser

import com.intellij.lang.ASTFactory
import com.intellij.psi.impl.source.tree.CompositeElement
import com.intellij.psi.tree.IElementType
import com.tezos.lang.michelson.MichelsonTypes

/**
 * @author jansorg
 */
//class MichelsonAstFactory : ASTFactory(){
//    override fun createComposite(type: IElementType): CompositeElement {
//        //custom elements
//        return when {
//            type === MichelsonElementTypes.TYPE_NAME -> PsiTypeNameImpl(type)
//            else -> MichelsonTypes.Factory.createElement(type)
//        }
//    }
//}
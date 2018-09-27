package com.tezos.lang.michelson.editor.completion

import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.tree.TokenSet
import com.intellij.util.ProcessingContext

/**
 * @author jansorg
 */
class PrevSiblingTextCondition(val nodeText:String): PatternCondition<PsiElement>("prev sibling $nodeText") {
    companion object {
        private val whitespace = TokenSet.create(TokenType.WHITE_SPACE)
    }
    override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
        val nonWhitespace = TreeUtil.skipElementsBack(t.node.treePrev, whitespace)?.psi

        return nonWhitespace?.textMatches(nodeText) ?: false
    }
}
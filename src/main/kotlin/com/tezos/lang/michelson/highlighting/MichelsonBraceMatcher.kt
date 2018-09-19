package com.tezos.lang.michelson.highlighting

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.tezos.lang.michelson.MichelsonTypes.*

/**
 * @author jansorg
 */
class MichelsonBraceMatcher : PairedBraceMatcher {
    companion object {
        val allPairs = arrayOf(
                BracePair(LEFT_PAREN, RIGHT_PAREN, true),
                BracePair(LEFT_CURLY, RIGHT_CURLY, true))
    }

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int {
        return openingBraceOffset
        //fixme
//        val psi = file.findElementAt(openingBraceOffset)
//        if (psi == null || psi is PsiFile) {
//            return openingBraceOffset
//
//        }
//
//        if (psi.parent is PsiGenericInstruction) {
//            return psi.parent.textOffset
//        } else {
//            return openingBraceOffset
//        }
    }

    override fun getPairs(): Array<BracePair> = allPairs

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return true
    }
}
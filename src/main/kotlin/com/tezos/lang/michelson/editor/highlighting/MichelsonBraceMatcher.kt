package com.tezos.lang.michelson.editor.highlighting

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.MichelsonTypes.*
import com.tezos.lang.michelson.lexer.MichelsonTokenSets
import com.tezos.lang.michelson.psi.PsiComplexType
import com.tezos.lang.michelson.psi.PsiGenericInstruction
import com.tezos.lang.michelson.psi.PsiInstruction
import com.tezos.lang.michelson.psi.PsiMacroInstruction

/**
 * @author jansorg
 */
class MichelsonBraceMatcher : PairedBraceMatcher {
    companion object {
        val allPairs = arrayOf(
                BracePair(LEFT_PAREN, RIGHT_PAREN, false),
                BracePair(LEFT_CURLY, RIGHT_CURLY, true))
    }

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int {
        val psi = file.findElementAt(openingBraceOffset)
        if (psi == null || psi is PsiFile) {
            return openingBraceOffset

        }

        // ignore the instruction block which contains a curly brace
        val startElement = when(MichelsonTokenSets.BRACES.contains(psi.node.elementType)){
            true -> psi.parent?.parent
            false -> psi.parent
        }

        val parentInstruction = PsiTreeUtil.findFirstParent(startElement, false) { it is PsiInstruction }
        if (parentInstruction is PsiGenericInstruction || parentInstruction is PsiMacroInstruction) {
            return parentInstruction.textOffset
        }

        return openingBraceOffset
    }

    override fun getPairs(): Array<BracePair> = allPairs

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return true
    }
}
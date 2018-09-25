package com.tezos.lang.michelson.highlighting

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.tezos.lang.michelson.MichelsonTypes.*
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

        // instruction > block > {
        var instruction = psi.parent?.parent as? PsiInstruction
        if (instruction is PsiGenericInstruction || instruction is PsiMacroInstruction) {
            return instruction.textOffset
        }

        instruction = psi.parent as? PsiInstruction
        if (instruction is PsiGenericInstruction || instruction is PsiMacroInstruction) {
            return instruction.textOffset
        }

        return openingBraceOffset
    }

    override fun getPairs(): Array<BracePair> = allPairs

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return true
    }
}
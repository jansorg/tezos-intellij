package com.tezos.lang.michelson.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil

object MichelsonPsiTreeUtil {
    fun findPrevLeafInParent(psi: PsiElement?, parent: PsiElement, type: IElementType?): PsiElement? {
        if (psi == null) {
            return null
        }

        var current: PsiElement? = psi
        while (current != null && current.node.elementType != type) {
            current = PsiTreeUtil.prevLeaf(current, true)
            if (current == null || !hasParent(current, parent)) {
                return null
            }
        }
        return current
    }


    fun hasParent(psi: PsiElement?, parent: PsiElement?): Boolean {
        return psi != null && parent != null && PsiTreeUtil.findFirstParent(psi) { it == parent } != null
    }

    fun findParentInstruction(psi: PsiElement?): PsiInstruction? {
        return PsiTreeUtil.findFirstParent(psi, ::isMacroOrGenericInstruction) as? PsiInstruction
    }

    fun isMacroOrGenericInstruction(psi: PsiElement): Boolean {
        return psi is PsiGenericInstruction || psi is PsiMacroInstruction
    }

    fun isParamElement(psi: PsiElement): Boolean {
        return psi is PsiType || psi is PsiTag || psi is PsiData
    }
}
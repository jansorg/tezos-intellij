package com.tezos.lang.michelson.psi

import com.intellij.psi.PsiElement

/**
 * @author jansorg
 */
object MichelsonPsiUtil {
    @JvmStatic
    fun getTypeNameString(type: PsiType): String {
        return when (type) {
            is PsiComparableType -> type.typeToken.text
            is PsiGenericType -> type.typeToken.text
            is PsiNestedType -> type.type.typeNameString
            is PsiComplexType -> type.typeToken.text
            else -> throw IllegalStateException("unsupported PSI element type ${type.javaClass.name}")
        }
    }

    @JvmStatic
    fun isComparable(type: PsiType) = type is PsiComparableType

    /**
     * Returns the PSIElement which contains the type's name token, which is a leaf in the PSI tree.
     */
    @JvmStatic
    fun getTypeToken(type: PsiComplexType): PsiElement {
        return type.firstChild
    }

    /**
     * Returns the name of the instruction.
     * Instuction blocks don't have a unique instruction name. 'null' is returned in this case.
     */
    @JvmStatic
    fun getInstructionName(psi: PsiInstruction): String? {
        return psi.instructionToken?.text
    }

    /**
     * Returns the token which wraps the name, if there's any.
     */
    @JvmStatic
    fun getInstructionToken(psi: PsiInstruction): PsiElement? {
        return when (psi) {
            is PsiBlockInstruction -> null
            is PsiGenericInstruction -> psi.instructionToken
            else -> psi.firstChild
        }
    }
}

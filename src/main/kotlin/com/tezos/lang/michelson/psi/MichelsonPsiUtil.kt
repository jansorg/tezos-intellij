package com.tezos.lang.michelson.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.lexer.MichelsonTokenSets

/**
 * @author jansorg
 */
object MichelsonPsiUtil {
    /**
     * Returns if this contract is the main contract in a michelson file.
     */
    @JvmStatic
    fun isMainContract(contract: PsiContract): Boolean {
        return contract.parent is PsiFile
    }

    @JvmStatic
    fun getSectionType(type: PsiSection): PsiSectionType {
        val token = type.firstChild?.text
        return when (token) {
            "parameter" -> PsiSectionType.PARAMETER
            "storage" -> PsiSectionType.STORAGE
            "code" -> PsiSectionType.CODE
            else -> throw IllegalStateException("unsupported section type $token")
        }
    }

    @JvmStatic
    fun getTypeNameString(type: PsiType): String {
        return when (type) {
            is PsiGenericType -> type.typeToken.text
            is PsiComplexType -> type.typeToken.text
            else -> throw IllegalStateException("unsupported PSI element type ${type.javaClass.name}")
        }
    }

    @JvmStatic
    fun isComparable(type: PsiType) = MichelsonLanguage.TYPES_COMPARABLE.contains(type.typeNameString)

    @JvmStatic
    fun findComposedParentType(type: PsiType): PsiType? {
        return type.parent as? PsiType
    }

    @JvmStatic
    fun hasComposedParentType(type: PsiType): Boolean {
        return findComposedParentType(type) != null
    }

    /**
     * Returns the children types of a composed type. Returns an empty list when no types were found.
     */
    @JvmStatic
    fun findChildrenTypes(type: PsiType): List<PsiType> = type.children.mapNotNull { it as? PsiType }

    /**
     * Returns the PSIElement which contains the type's name token, which is a leaf in the PSI tree.
     */
    @JvmStatic
    fun getTypeToken(type: PsiComplexType): PsiElement {
        return type.firstChild
    }

    @JvmStatic
    fun hasSimpleTypes(type: PsiComplexType): Boolean {
        return type.typeList.filter { it is PsiGenericType }.isNotEmpty()
    }

    @JvmStatic
    fun hasComplexTypes(type: PsiComplexType): Boolean {
        return type.typeList.filter { it is PsiComplexType }.isNotEmpty()
    }

    /**
     * Returns the name of the instruction.
     * Instruction blocks don't have a unique instruction name. 'null' is returned in this case.
     */
    @JvmStatic
    fun getInstructionName(psi: PsiInstruction): String? {
        return psi.instructionToken?.text
    }

    /**
     * Returns the previous instruction, if available.  The start element must not be an instruction itself
     * In this case first parent of the start element which is an instruction is used to locate the prev sibling.
     */
    @JvmStatic
    fun findPrevInstruction(startElement: PsiElement): PsiInstruction? {
        val instruction = when (startElement) {
            is PsiInstruction -> startElement
            else -> PsiTreeUtil.findFirstParent(startElement, false) { it is PsiInstruction }
        }

        var prev: PsiElement? = instruction?.prevSibling
        while (prev != null) {
            val nodeType = prev.node.elementType
            if (nodeType != MichelsonTypes.SEMI && !nodeType.isWhitespace()) {
                break
            }
            prev = prev.prevSibling
        }

        return prev as? PsiInstruction
    }

    /**
     * Returns if element is the first child of a code section in the main contract
     */
    @JvmStatic
    fun isFirstCodeChild(element: PsiElement?): Boolean {
        if (element == null) {
            return false
        }

        // find first non-whitespace previous sibling, it's either null if inside of a block or a section name, e.g. 'code'
        var e = element.prevSibling
        while (e != null && (e.isWhitespace() || e.node.elementType == MichelsonTypes.LEFT_CURLY)) {
            e = e.prevSibling
        }

        if (e == null) {
            return isFirstCodeChild(element.parent)
        }

        val section = when {
            e is PsiBlockInstruction -> e.parent
            e is PsiSection -> e
            e.node.elementType == MichelsonTypes.SECTION_NAME -> e.parent
            else -> null
        }

        return (section as? PsiSection)?.sectionType == PsiSectionType.CODE && section.parent is PsiContract
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

    @JvmStatic
    fun getAnnotationType(psi: PsiAnnotation): PsiAnnotationType {
        return when (psi) {
            is PsiTypeAnnotation -> PsiAnnotationType.TYPE
            is PsiVariableAnnotation -> PsiAnnotationType.VARIABLE
            is PsiFieldAnnotation -> PsiAnnotationType.FIELD
            else -> throw IllegalStateException("unsupported annotation type ${psi.javaClass.name}")
        }
    }

    @JvmStatic
    fun isTypeAnnotation(psi: PsiAnnotation): Boolean = psi is PsiTypeAnnotation

    @JvmStatic
    fun isVariableAnnotation(psi: PsiAnnotation): Boolean = psi is PsiVariableAnnotation

    @JvmStatic
    fun isFieldAnnotation(psi: PsiAnnotation): Boolean = psi is PsiFieldAnnotation

    @JvmStatic
    fun findParentType(psi: PsiAnnotation): PsiType? = psi.parent as? PsiType

    @JvmStatic
    fun findParentInstruction(psi: PsiAnnotation): PsiInstruction? = psi.parent as? PsiInstruction

    @JvmStatic
    fun findParentData(psi: PsiAnnotation): PsiData? = psi.parent as? PsiData

    @JvmStatic
    fun isWhitespaceOnly(psi: PsiBlockInstruction): Boolean {
        val next = TreeUtil.skipElements(psi.firstChild.node.treeNext, MichelsonTokenSets.WHITESPACE_TOKENS)
        return next == psi.lastChild.node
    }

    @JvmStatic
    fun findParentContract(psi: PsiElement): PsiContract? = PsiTreeUtil.findFirstParent(psi) {
        it is PsiContract
    } as? PsiContract
}

fun PsiElement?.isWhitespace(): Boolean {
    return when (this) {
        null -> false
        else -> MichelsonTokenSets.WHITESPACE_TOKENS.contains(this.node.elementType)
    }
}

fun IElementType?.isWhitespace(): Boolean {
    return when (this) {
        null -> false
        else -> MichelsonTokenSets.WHITESPACE_TOKENS.contains(this)
    }
}
package com.tezos.lang.michelson.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.tezos.client.stack.MichelsonStackAnnotation
import com.tezos.client.stack.MichelsonStackType
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.lang.AnnotationType
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.lang.instruction.InstructionMetadata
import com.tezos.lang.michelson.lang.macro.MacroMetadata
import com.tezos.lang.michelson.lang.tag.TagMetadata
import com.tezos.lang.michelson.lang.type.TypeMetadata
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
    fun findSectionByType(contract: PsiContract, type: PsiSectionType): PsiSection? {
        return contract.sections.firstOrNull { it.sectionType == type }
    }

    @JvmStatic
    fun findParameterSection(contract: PsiContract): PsiTypeSection? {
        return contract.sections.firstOrNull { it.sectionType == PsiSectionType.PARAMETER } as? PsiTypeSection
    }

    @JvmStatic
    fun findStorageSection(contract: PsiContract): PsiTypeSection? {
        return contract.sections.firstOrNull { it.sectionType == PsiSectionType.STORAGE } as? PsiTypeSection
    }

    @JvmStatic
    fun findCodeSection(contract: PsiContract): PsiCodeSection? {
        return contract.sections.firstOrNull { it.sectionType == PsiSectionType.CODE } as? PsiCodeSection
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
            is PsiSimpleType -> type.typeToken.text
            is PsiComplexType -> type.typeToken!!.text
            else -> throw IllegalStateException("unsupported PSI element type ${type.javaClass.name}")
        }
    }

    @JvmStatic
    fun asStackType(type: PsiType): MichelsonStackType {
        val children = type.children
        val types = children.mapNotNull { it as? PsiType }
        val annotations = when (type is PsiComplexType) {
            true -> (type as PsiComplexType).annotationList?.annotations
            false -> null
        } ?: emptyList<PsiAnnotation>()

        return MichelsonStackType(type.typeNameString, types.map { it.asStackType() }, annotations.map { it.asStackAnnotation() })
    }

    @JvmStatic
    fun asStackAnnotation(annotation: PsiAnnotation): MichelsonStackAnnotation {
        return MichelsonStackAnnotation(annotation.text)
    }

    @JvmStatic
    fun isComparable(type: PsiType): Boolean {
        val meta = type.typeMetadata
        return meta != null && meta.isComparable
    }

    @JvmStatic
    fun findParentType(type: PsiType): PsiType? {
        return type.parent as? PsiType
    }

    @JvmStatic
    fun hasParentType(type: PsiType): Boolean {
        return findParentType(type) != null
    }

    @JvmStatic
    fun hasSimpleTypes(type: PsiComplexType): Boolean {
        return type.typeArguments.any { it is PsiSimpleType }
    }

    @JvmStatic
    fun getTypeToken(type: PsiComplexType): PsiElement? {
        return type.node.findChildByType(MichelsonTokenSets.TYPE_NAMES)?.psi
    }

    @JvmStatic
    fun hasComplexTypes(type: PsiComplexType): Boolean {
        return type.typeArguments.any { it is PsiComplexType }
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
     * Returns the name of the instruction.
     * Instruction blocks don't have a unique instruction name. 'null' is returned in this case.
     */
    @JvmStatic
    fun getTagName(psi: PsiTag): String {
        return psi.firstChild.text
    }

    /**
     * Returns the name of the instruction.
     * Instruction blocks don't have a unique instruction name. 'null' is returned in this case.
     */
    @JvmStatic
    fun getTagMetadata(psi: PsiTag): TagMetadata? {
        val name = psi.tagName
        return MichelsonLanguage.TAGS_METAS.firstOrNull { name in it.names() }
    }

    /**
     * Returns the metadata for the given type.
     * Instruction blocks don't have a unique instruction name. 'null' is returned in this case.
     */
    @JvmStatic
    fun getTypeMetadata(psi: PsiType): TypeMetadata? {
        val name = psi.typeNameString
        return MichelsonLanguage.TYPES.firstOrNull { name == it.name }
    }

    /**
     * Returns the metadata for the given type.
     * Instruction blocks don't have a unique instruction name. 'null' is returned in this case.
     */
    @JvmStatic
    fun getTypeToken(psi: PsiSimpleType): PsiElement {
        return psi.firstChild
    }

    /**
     * Returns if element is the first child of a code section in the main contract
     */
    @JvmStatic
    fun isFirstCodeChild(element: PsiElement?, caretOffset: Int): Boolean {
        if (element == null) {
            return false
        }

        // find first non-whitespace previous sibling, it's either null if inside of a block or a section name, e.g. 'code'
        // prefer previous element if the element after caretOffset was passed
        var e = if (caretOffset == element.textOffset) element.prevSibling else element
        while (e != null && (e.isWhitespace() || e.node.elementType == MichelsonTypes.LEFT_CURLY)) {
            e = e.prevSibling
        }

        if (e == null) {
            return isFirstCodeChild(element.parent, caretOffset)
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

    /**
     * Returns the metadata for this instruction, if available
     */
    @JvmStatic
    fun getInstructionMetadata(psi: PsiGenericInstruction): InstructionMetadata? {
        val name = psi.instructionName
        return name.let {
            MichelsonLanguage.INSTRUCTIONS.find { it.name == name }
        }
    }

    /**
     * Returns the metadata for this instruction, if available
     */
    @JvmStatic
    fun getMacroMetadata(psi: PsiMacroInstruction): MacroMetadata? {
        return psi.instructionName.let { name ->
            MichelsonLanguage.MACROS.find { it.validate(name!!) == null }
        }
    }

    @JvmStatic
    fun getAnnotationType(psi: PsiAnnotation): AnnotationType {
        return when (psi) {
            is PsiTypeAnnotation -> AnnotationType.TYPE
            is PsiVariableAnnotation -> AnnotationType.VARIABLE
            is PsiFieldAnnotation -> AnnotationType.FIELD
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
    fun findParentType(psi: PsiAnnotation): PsiType? {
        val parent = psi.parent
        return when (parent) {
            is PsiAnnotationList -> parent.parent as? PsiType
            else -> parent as? PsiType
        }
    }

    @JvmStatic
    fun findParentInstruction(psi: PsiAnnotationList): PsiInstruction? = psi.parent as? PsiInstruction

    @JvmStatic
    fun findParentInstruction(psi: PsiAnnotation): PsiInstruction? = psi.parent as? PsiInstruction

    @JvmStatic
    fun findParentAnnotationList(psi: PsiAnnotation): PsiAnnotationList? = psi.parent as? PsiAnnotationList

    @JvmStatic
    fun isWhitespaceOnly(psi: PsiBlockInstruction): Boolean {
        val next = TreeUtil.skipElements(psi.firstChild.node.treeNext, MichelsonTokenSets.WHITESPACE_TOKENS)
        return next == psi.lastChild.node
    }
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
package com.tezos.lang.michelson.editor.parameterInfo

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.lang.parameterInfo.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.lang.ParameterType
import com.tezos.lang.michelson.psi.*

/**
 * @author jansorg
 */
class MichelsonParameterInfoHandler : ParameterInfoHandler<PsiElement, PsiElement>, DumbAware {
    companion object {
        val LOG = Logger.getInstance("#tezos.paramInfo")

        fun findTarget(file: PsiFile, offset: Int, inUpdate: Boolean): PsiElement? {
            LOG.debug("findTarget @ $offset")

            var start = file.findElementAt(offset)
            if (start != null && (start.isWhitespace() || start.node.elementType == MichelsonTypes.SEMI || start.node.elementType == MichelsonTypes.RIGHT_PAREN)) {
                val prevSibling = start.prevSibling
                if (prevSibling != null && prevSibling.textRange.endOffset == offset) {
                    start = prevSibling
                }
            }

            val parent = PsiTreeUtil.findFirstParent(start, false) {
                if (it is PsiTag) {
                    val meta = it.tagMetadata
                    meta != null && meta.isComplex()
                } else if (it is PsiType) {
                    val meta = it.typeMetadata
                    meta != null && meta.subtypes.isNotEmpty()
                } else {
                    it is PsiInstruction
                }
            }

            // IntelliJ calls with offset-1 when the caret is at the start of an instruction after a whitespace, we have to workaround that
            if (inUpdate && (parent == null || parent is PsiBlockInstruction)) {
                return findTarget(file, offset + 1, false)
            }

            return when (parent) {
                is PsiType -> parent
                is PsiTag -> parent
                is PsiBlockInstruction -> null
                is PsiInstruction -> parent
                else -> null
            }
        }
    }

    override fun tracksParameterIndex(): Boolean {
        return false
    }

    override fun showParameterInfo(element: PsiElement, context: CreateParameterInfoContext) {
        context.showHint(element, element.textRange.startOffset + 1, this)
    }

    override fun updateParameterInfo(parameterOwner: PsiElement, context: UpdateParameterInfoContext) {
        LOG.info("updateParameterInfo")
    }

    override fun updateUI(psi: PsiElement?, context: ParameterInfoUIContext) {
        val buffer = StringBuilder()
        var endOffset = 0
        if (psi is PsiGenericInstruction) {
            buffer.append(psi.instructionName)
            endOffset = buffer.length

            val meta = psi.instructionMetadata
            if (meta != null) {
                for (p in meta.parameters) {
                    buffer.append(" ").append(p.asPlaceholder())
                }
            }
        } else if (psi is PsiMacroInstruction) {
            buffer.append(psi.instructionName)
            endOffset = buffer.length

            val blocks = psi.macroMetadata?.requiredBlocks()
            if (blocks != null && blocks > 0)
                for (i in 0 until blocks) {
                    buffer.append(" ").append(ParameterType.INSTRUCTION_BLOCK.asPlaceholder())
                }
        } else if (psi is PsiTag) {
            buffer.append(psi.tagName)
            endOffset = buffer.length

            val expected = psi.tagMetadata?.supportedValues()
            if (expected != null) {
                for (i in 0 until expected) {
                    buffer.append(" <data>")
                }
            }
        } else if (psi is PsiType) {
            buffer.append(psi.typeNameString)
            endOffset = buffer.length

            val expected = psi.typeMetadata?.subtypes
            if (expected != null) {
                for (subType in expected) {
                    buffer.append(" ").append(subType.asPlaceholder())
                }
            }
        }

        context.setupUIComponentPresentation(buffer.toString(), 0, endOffset, false, false, false, context.defaultParameterColor)
    }

    override fun getParametersForDocumentation(psi: PsiElement, context: ParameterInfoContext): Array<Any>? {
        return arrayOf(psi)
    }

    override fun getParametersForLookup(item: LookupElement, context: ParameterInfoContext): Array<Any>? = null

    override fun getParameterCloseChars(): String? = null

    override fun couldShowInLookup(): Boolean = false

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext): PsiElement? {
        val target = findTarget(context.file, context.offset, true)
        if (context.parameterOwner == null || context.parameterOwner == target) {
            context.parameterOwner = target
            return target
        }

        context.removeHint()
        return null
    }

    override fun findElementForParameterInfo(context: CreateParameterInfoContext): PsiElement? {
        val target = findTarget(context.file, context.offset, false)
        if (target != null) {
            context.itemsToShow = arrayOf(target)
            context.highlightedElement = target
            return target
        }

        return null
    }
}

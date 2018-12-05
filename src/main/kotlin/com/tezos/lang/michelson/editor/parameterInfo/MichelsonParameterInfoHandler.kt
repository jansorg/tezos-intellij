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
class MichelsonParameterInfoHandler : ParameterInfoHandler<PsiInstruction, PsiElement>, DumbAware {
    companion object {
        val LOG = Logger.getInstance("#tezos.paramInfo")

        fun findInstruction(file: PsiFile, offset: Int, inUpdate: Boolean): PsiInstruction? {
            LOG.debug("findInstruction @ $offset")

            var start = file.findElementAt(offset)
            if (start != null && (start.isWhitespace() || start.node.elementType == MichelsonTypes.SEMI)) {
                val prevSibling = start.prevSibling
                if (prevSibling != null && prevSibling.textRange.endOffset == offset) {
                    start = prevSibling
                }
            }

            val parent = PsiTreeUtil.findFirstParent(start, false) {
                it is PsiInstruction
            }

            // IntelliJ calls with offset-1 when the caret is at the start of an instruction after a whitespace, we have to workaround that
            if (inUpdate && (parent == null || parent is PsiBlockInstruction)) {
                return findInstruction(file, offset + 1, false)
            }

            return when (parent) {
                is PsiBlockInstruction -> null
                is PsiInstruction -> parent
                else -> null
            }
        }
    }

    override fun tracksParameterIndex(): Boolean {
        return false
    }

    override fun showParameterInfo(element: PsiInstruction, context: CreateParameterInfoContext) {
        context.showHint(element, element.textRange.startOffset + 1, this)
    }

    override fun updateParameterInfo(parameterOwner: PsiInstruction, context: UpdateParameterInfoContext) {
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
        }

        context.setupUIComponentPresentation(buffer.toString(), 0, endOffset, false, false, false, context.defaultParameterColor)
    }

    override fun getParametersForDocumentation(psi: PsiElement, context: ParameterInfoContext): Array<Any>? {
        return arrayOf(psi)
    }

    override fun getParametersForLookup(item: LookupElement, context: ParameterInfoContext): Array<Any>? = null

    override fun getParameterCloseChars(): String? = null

    override fun couldShowInLookup(): Boolean = false

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext): PsiInstruction? {
        val instruction = findInstruction(context.file, context.offset, true)
        if (context.parameterOwner == null || context.parameterOwner == instruction) {
            context.parameterOwner = instruction
            return instruction
        }

        context.removeHint()
        return null
    }

    override fun findElementForParameterInfo(context: CreateParameterInfoContext): PsiInstruction? {
        val instruction = findInstruction(context.file, context.offset, false)
        if (instruction != null) {
            context.itemsToShow = arrayOf(instruction)
            context.highlightedElement = instruction
            return instruction
        }

        return null
    }
}

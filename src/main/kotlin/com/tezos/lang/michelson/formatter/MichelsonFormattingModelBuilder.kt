package com.tezos.lang.michelson.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.tezos.lang.michelson.MichelsonLanguage
import com.tezos.lang.michelson.MichelsonTypes.SECTION

/**
 * Formatter for the Michelson language.
 * @author jansorg
 */
class MichelsonFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
        val wrap = Wrap.createWrap(WrapType.NONE, false)
        val alignment = Alignment.createAlignment()
        val block = SimpleMichelsonBlock(element.node, wrap, alignment, createSpacingBuilder(settings), Indent.getNoneIndent())

        return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, block, settings)
    }

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val builder = SpacingBuilder(settings, MichelsonLanguage)

        builder.between(SECTION, SECTION).none()

        return builder
    }

    override fun getRangeAffectingIndent(file: PsiFile, offset: Int, elementAtOffset: ASTNode): TextRange? {
        return null
    }
}
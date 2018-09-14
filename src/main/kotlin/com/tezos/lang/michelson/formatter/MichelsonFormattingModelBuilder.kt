package com.tezos.lang.michelson.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonLanguage
import com.tezos.lang.michelson.MichelsonTypes.*
import com.tezos.lang.michelson.lexer.MichelsonElementTokenSets

/**
 * Formatter for the Michelson language.
 * @author jansorg
 */
class MichelsonFormattingModelBuilder : FormattingModelBuilder {
    private companion object {
        val literals = TokenSet.create(LITERAL_DATA, STRING_LITERAL)
        val types = TokenSet.create(TYPE, COMPARABLE_TYPE)
        val instructions = TokenSet.create(GENERIC_INSTRUCTION, MACRO_INSTRUCTION)
        val annotations = TokenSet.create(VARIABLE_ANNOTATION, FIELD_ANNOTATION, TYPE_ANNOTATION)
        val allToplevel = TokenSet.orSet(TokenSet.create(INSTRUCTION_TOKEN, MACRO_TOKEN, TAG, COMPLEX_TYPE), MichelsonElementTokenSets.TYPE_NAMES)
        val allArguments = TokenSet.orSet(types, literals, MichelsonElementTokenSets.TYPE_NAMES, MichelsonElementTokenSets.LITERAL_TOKENS, annotations)
    }

    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
        val block = MichelsonBlock(
                element.node,
                Wrap.createWrap(WrapType.NONE, false),
                Alignment.createAlignment(), createSpacingBuilder(settings),
                Indent.getNoneIndent())

        return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, block, settings)
    }

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val commonSettings = settings.getCommonSettings(MichelsonLanguage)
        val michelsonSettings = settings.getCustomSettings(MichelsonCodeStyleSettings::class.java)

        val builder = SpacingBuilder(settings, MichelsonLanguage)

        // section
        builder.after(SECTION_NAME).lineBreakOrForceSpace(false, true)
        builder.beforeInside(SEMI, SECTION).lineBreakOrForceSpace(false, false)
        builder.between(SECTION, SECTION).lineBreakInCode()

        // generic spacing
        builder.between(allToplevel, allArguments).lineBreakOrForceSpace(false, true)
        builder.between(allArguments, allArguments).lineBreakOrForceSpace(false, true)
        builder.between(instructions, SEMI).lineBreakOrForceSpace(false, false)

        builder.withinPair(LEFT_PAREN, RIGHT_PAREN).lineBreakOrForceSpace(false, false)

        builder.between(LEFT_CURLY, RIGHT_CURLY).none()
        builder.before(RIGHT_CURLY).parentDependentLFSpacing(1, 1, commonSettings.KEEP_LINE_BREAKS, commonSettings.KEEP_BLANK_LINES_BEFORE_RBRACE);
        builder.betweenInside(LEFT_CURLY, RIGHT_CURLY, BLOCK_INSTRUCTION).none()
        builder.after(LEFT_CURLY).spaces(1, true)

        builder.before(SEMI).spaceIf(commonSettings.SPACE_BEFORE_SEMICOLON)
        builder.after(SEMI).spaceIf(commonSettings.SPACE_AFTER_SEMICOLON)

        // wrapping between IF {
        builder.betweenInside(MichelsonElementTokenSets.INTRUCTIONS_TOKENS, TokenSet.create(BLOCK_INSTRUCTION), GENERIC_INSTRUCTION).lineBreakOrForceSpace(michelsonSettings.WRAP_FIRST_BLOCK, true)
        // wrapping between blocks to enable alignment, e.g. in IF {} {}
        builder.betweenInside(BLOCK_INSTRUCTION, BLOCK_INSTRUCTION, GENERIC_INSTRUCTION).lineBreakOrForceSpace(michelsonSettings.ALIGN_BLOCKS, true)

        return builder
    }

    override fun getRangeAffectingIndent(file: PsiFile, offset: Int, elementAtOffset: ASTNode): TextRange? {
        return null
    }
}
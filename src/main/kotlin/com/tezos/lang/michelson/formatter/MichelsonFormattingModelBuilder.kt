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
import com.tezos.lang.michelson.lexer.MichelsonTokenSets

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
        val allToplevel = TokenSet.orSet(TokenSet.create(INSTRUCTION_TOKEN, MACRO_TOKEN, TAG, COMPLEX_TYPE), MichelsonTokenSets.TYPE_NAMES)
        val allArguments = TokenSet.orSet(types, literals, MichelsonTokenSets.TYPE_NAMES, MichelsonTokenSets.LITERAL_TOKENS, annotations)
        val blockInstructionSet = TokenSet.create(BLOCK_INSTRUCTION)
    }


    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
        val block = MichelsonBlock(
                element.node,
                Wrap.createWrap(WrapType.ALWAYS, true),
                Alignment.createAlignment(),
                createSpacingBuilder(settings),
                Indent.getAbsoluteNoneIndent(),
                settings)

        return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, block, settings)
    }

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val michelsonSettings = settings.getCustomSettings(MichelsonCodeStyleSettings::class.java)
        val commonSettings = settings.getCommonSettings(MichelsonLanguage)
        val builder = SpacingBuilder(commonSettings)

        // section
        builder.after(SECTION_NAME).lineBreakOrForceSpace(false, true)
        builder.beforeInside(SEMI, SECTION).lineBreakOrForceSpace(false, false)
        builder.between(SECTION, SECTION).lineBreakInCode()

        // generic: no space in '(...)'
        builder.withinPair(LEFT_PAREN, RIGHT_PAREN).lineBreakOrForceSpace(false, false)

        // first element of a complex type is only wrapped when both wrap_first and align_compex_types are enabled
        val nestedTypes = TokenSet.create(GENERIC_TYPE, LEFT_PAREN)
        builder.betweenInside(TokenSet.create(TYPE_NAME), nestedTypes, COMPLEX_TYPE).lineBreakOrForceSpace(michelsonSettings.COMPLEX_TYPE_WRAP_FIRST && michelsonSettings.COMPLEX_TYPE_ALIGN, true, true)
        builder.betweenInside(nestedTypes, nestedTypes, COMPLEX_TYPE).lineBreakOrForceSpace(michelsonSettings.COMPLEX_TYPE_ALIGN, true)
        builder.beforeInside(nestedTypes, COMPLEX_TYPE).lineBreakOrForceSpace(michelsonSettings.COMPLEX_TYPE_ALIGN, true, commonSettings.KEEP_LINE_BREAKS)

        // comments
        // split token prefix + content inside of a line comment still have the COMMENT_LINE token type
        builder.betweenInside(COMMENT_LINE, COMMENT_LINE, COMMENT_LINE).spacing(if (michelsonSettings.LINE_COMMENT_LEADING_SPACE) 1 else 0, Int.MAX_VALUE, 0, true, 0)
        builder.between(COMMENT_LINE, COMMENT_LINE).lineBreakInCode()
        builder.between(MichelsonTokenSets.COMMENT_TOKENS, MichelsonTokenSets.COMMENT_TOKENS).lineBreakInCode()
        builder.after(MichelsonTokenSets.COMMENT_TOKENS).lineBreakInCode()

        // generic spacing
        builder.between(allToplevel, allArguments).lineBreakOrForceSpace(false, true)
        builder.between(allArguments, allArguments).lineBreakOrForceSpace(false, true)
        builder.between(instructions, SEMI).lineBreakOrForceSpace(false, false)

        // blocks
        builder.between(LEFT_CURLY, RIGHT_CURLY).none() // {}
        builder.before(RIGHT_CURLY).parentDependentLFSpacing(1, 1, commonSettings.KEEP_LINE_BREAKS, commonSettings.KEEP_BLANK_LINES_BEFORE_RBRACE);
        builder.betweenInside(LEFT_CURLY, COMMENT_LINE, BLOCK_INSTRUCTION).spaces(1) // IF { # comment
        builder.betweenInside(LEFT_CURLY, RIGHT_CURLY, BLOCK_INSTRUCTION).none()  // IF {...}
        builder.after(LEFT_CURLY).spaces(1, true)

        builder.before(SEMI).spaceIf(commonSettings.SPACE_BEFORE_SEMICOLON)
        builder.after(SEMI).spaceIf(commonSettings.SPACE_AFTER_SEMICOLON)

        // wrapping before initial block in instructions
        builder.betweenInside(MichelsonTokenSets.INTRUCTIONS_TOKENS, blockInstructionSet, GENERIC_INSTRUCTION).lineBreakOrForceSpace(michelsonSettings.WRAP_FIRST_BLOCK, true)
        // wrapping between blocks to enable alignment, e.g. in IF {} {}
        builder.betweenInside(BLOCK_INSTRUCTION, BLOCK_INSTRUCTION, GENERIC_INSTRUCTION).lineBreakOrForceSpace(michelsonSettings.ALIGN_BLOCKS, true)

        return builder
    }

    override fun getRangeAffectingIndent(file: PsiFile, offset: Int, elementAtOffset: ASTNode): TextRange? {
        return null
    }

    private fun SpacingBuilder.RuleBuilder.lineBreakOrForceSpace(lbOption: Boolean, spaceOption: Boolean, keepLineBreaks: Boolean): SpacingBuilder {
        if (lbOption) {
            return lineBreakInCode()
        }

        val count = if (spaceOption) 1 else 0
        return this.spacing(count, count, 0, keepLineBreaks, 0)
    }
}
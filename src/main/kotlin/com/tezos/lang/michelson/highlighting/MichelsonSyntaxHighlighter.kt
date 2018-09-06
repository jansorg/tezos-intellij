package com.tezos.lang.michelson.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.lexer.MichelsonElementTokenSets
import com.tezos.lang.michelson.lexer.MichelsonLexer
import java.util.*

/**
 * @author jansorg
 */
class MichelsonSyntaxHighlighter : SyntaxHighlighterBase() {
    internal companion object {
        val LINE_COMMENT = TextAttributesKey.createTextAttributesKey("MI.LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey("MI.BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)

        val INSTRUCTION = TextAttributesKey.createTextAttributesKey("MI.INSTRUCTION", DefaultLanguageHighlighterColors.FUNCTION_CALL)
        val MACRO = TextAttributesKey.createTextAttributesKey("MI.MACRO", INSTRUCTION)

        val TAG = TextAttributesKey.createTextAttributesKey("MI.TAG", DefaultLanguageHighlighterColors.CLASS_REFERENCE)

        val TYPE_NAME = TextAttributesKey.createTextAttributesKey("MI.TYPE_NAME", DefaultLanguageHighlighterColors.CLASS_REFERENCE)
        val TYPE_NAME_COMPARABLE = TextAttributesKey.createTextAttributesKey("MI.TYPE_NAME_COMPARABLE", TYPE_NAME)

        val ANNOTATION = TextAttributesKey.createTextAttributesKey("MI.ANNOTATION", DefaultLanguageHighlighterColors.METADATA)
        val TYPE_ANNOTATION = TextAttributesKey.createTextAttributesKey("MI.ANNOTATION_TYPE", ANNOTATION)
        val VARIABLE_ANNOTATION = TextAttributesKey.createTextAttributesKey("MI.ANNOTATION_VAR", ANNOTATION)
        val FIELD_ANNOTATION = TextAttributesKey.createTextAttributesKey("MI.ANNOTATION_FIELD", ANNOTATION)

        // literal
        val INT_LITERAL = TextAttributesKey.createTextAttributesKey("MI.INT", DefaultLanguageHighlighterColors.NUMBER)
        val STRING_LITERAL = TextAttributesKey.createTextAttributesKey("MI.STRING", DefaultLanguageHighlighterColors.STRING)
        val VALID_STRING_ESCAPE = TextAttributesKey.createTextAttributesKey("MI.STRING_ESCAPE_VALID", DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE)
        val ILLEGAL_STRING_ESCAPE = TextAttributesKey.createTextAttributesKey("MI.STRING_ESCAPE_INVALID", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE)
        val BYTE_LITERAL = TextAttributesKey.createTextAttributesKey("MI.BYTE", DefaultLanguageHighlighterColors.CONSTANT)
        val BOOLEAN_LITERAL = TextAttributesKey.createTextAttributesKey("MI.BOOL_LITERAL", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL)
        val UNIT_LITERAL = TextAttributesKey.createTextAttributesKey("MI.UNIT_LITERAL", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL)

        // generic symbols
        val SECTION_NAME = TextAttributesKey.createTextAttributesKey("MI.SECTION_NAME", DefaultLanguageHighlighterColors.KEYWORD)
        val SEMI = TextAttributesKey.createTextAttributesKey("MI.SEMI", DefaultLanguageHighlighterColors.SEMICOLON)
        val PAREN = TextAttributesKey.createTextAttributesKey("MI.PAREN", DefaultLanguageHighlighterColors.PARENTHESES)
        val BRACES = TextAttributesKey.createTextAttributesKey("MI.PAREN", DefaultLanguageHighlighterColors.BRACES)

        // keys
        private val keys = HashMap<IElementType, TextAttributesKey>()
        init {
            keys.put(TokenType.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER)

            keys.put(MichelsonTypes.COMMENT_LINE, LINE_COMMENT)
            keys.put(MichelsonTypes.COMMENT_MULTI_LINE, BLOCK_COMMENT)

            keys.put(MichelsonTypes.SECTION_NAME, SECTION_NAME)

            keys.put(MichelsonTypes.TYPE_NAME, TYPE_NAME)
            keys.put(MichelsonTypes.TYPE_NAME_COMPARABLE, TYPE_NAME_COMPARABLE)
            keys.put(MichelsonTypes.TAG, TAG)
            keys.put(MichelsonTypes.INSTRUCTION_TOKEN, INSTRUCTION)
            fillMap(keys, MichelsonElementTokenSets.MACROS, MACRO)

            keys.put(MichelsonTypes.ANNOTATION_TOKEN, ANNOTATION)
            //fixme better handle different types of annotations in annotator

            keys.put(MichelsonTypes.INT, INT_LITERAL)
            keys.put(MichelsonTypes.BYTE, BYTE_LITERAL)
            keys.put(MichelsonTypes.UNIT, UNIT_LITERAL)
            keys.put(MichelsonTypes.QUOTE, STRING_LITERAL)
            keys.put(MichelsonTypes.STRING_CONTENT, STRING_LITERAL)
            keys.put(MichelsonTypes.STRING_ESCAPE, VALID_STRING_ESCAPE)
            keys.put(MichelsonTypes.STRING_ESCAPE_INVALID, ILLEGAL_STRING_ESCAPE)
            fillMap(keys, MichelsonElementTokenSets.BOOLEAN, BOOLEAN_LITERAL)

            keys.put(MichelsonTypes.SEMI, SEMI)
            fillMap(keys, MichelsonElementTokenSets.PARENTHESES, PAREN)
            fillMap(keys, MichelsonElementTokenSets.BRACES, BRACES)
        }
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> = pack(keys.get(tokenType))

    override fun getHighlightingLexer(): Lexer = MichelsonLexer()
}
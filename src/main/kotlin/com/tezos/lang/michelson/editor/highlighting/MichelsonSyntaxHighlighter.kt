package com.tezos.lang.michelson.editor.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.lexer.MichelsonTokenSets
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
        val BOOLEAN_TAG = TextAttributesKey.createTextAttributesKey("MI.BOOL_TAG", TAG)
        val UNIT_TAG = TextAttributesKey.createTextAttributesKey("MI.UNIT_TAG", TAG)
        val OPTION_TAG = TextAttributesKey.createTextAttributesKey("MI.OPTION_TAG", TAG)
        val OR_TAG = TextAttributesKey.createTextAttributesKey("MI.OPTION_OR", TAG)

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

        // generic symbols
        val SECTION_NAME = TextAttributesKey.createTextAttributesKey("MI.SECTION_NAME", DefaultLanguageHighlighterColors.KEYWORD)
        val SEMI = TextAttributesKey.createTextAttributesKey("MI.SEMI", DefaultLanguageHighlighterColors.SEMICOLON)
        val PAREN = TextAttributesKey.createTextAttributesKey("MI.PAREN", DefaultLanguageHighlighterColors.PARENTHESES)
        val BRACES = TextAttributesKey.createTextAttributesKey("MI.BRACES", DefaultLanguageHighlighterColors.BRACES)

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
            fillMap(keys, MichelsonTokenSets.MACROS, MACRO)

            keys.put(MichelsonTypes.TYPE_ANNOTATION_TOKEN, TYPE_ANNOTATION)
            keys.put(MichelsonTypes.VAR_ANNOTATION_TOKEN, VARIABLE_ANNOTATION)
            keys.put(MichelsonTypes.FIELD_ANNOTATION_TOKEN, FIELD_ANNOTATION)

            keys.put(MichelsonTypes.INT, INT_LITERAL)
            keys.put(MichelsonTypes.BYTE, BYTE_LITERAL)
            keys.put(MichelsonTypes.QUOTE, STRING_LITERAL)
            keys.put(MichelsonTypes.STRING_CONTENT, STRING_LITERAL)
            keys.put(MichelsonTypes.STRING_ESCAPE, VALID_STRING_ESCAPE)
            keys.put(MichelsonTypes.STRING_ESCAPE_INVALID, ILLEGAL_STRING_ESCAPE)
            keys.put(MichelsonTypes.TAG, TAG)

            keys.put(MichelsonTypes.SEMI, SEMI)
            fillMap(keys, MichelsonTokenSets.PARENTHESES, PAREN)
            fillMap(keys, MichelsonTokenSets.BRACES, BRACES)
        }
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> = pack(keys.get(tokenType))

    override fun getHighlightingLexer(): Lexer = MichelsonLexer()
}
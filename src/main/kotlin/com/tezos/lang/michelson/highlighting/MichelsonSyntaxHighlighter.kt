package com.tezos.lang.michelson.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.lexer.MichelsonElementTypes
import com.tezos.lang.michelson.lexer.MichelsonLexer
import java.util.*

/**
 * @author jansorg
 */
class MichelsonSyntaxHighlighter : SyntaxHighlighterBase() {
    private companion object {
        private val LINE_COMMENT = TextAttributesKey.createTextAttributesKey("MI.LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        private val BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey("MI.BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)

        private val SECTION_NAME = TextAttributesKey.createTextAttributesKey("MI.SECTION_NAME", DefaultLanguageHighlighterColors.KEYWORD)
        private val TYPE_NAME = TextAttributesKey.createTextAttributesKey("MI.TYPE_NAME", DefaultLanguageHighlighterColors.CLASS_REFERENCE)
        private val TYPE_NAME_COMPARABLE = TextAttributesKey.createTextAttributesKey("MI.TYPE_NAME_COMPARABLE", TYPE_NAME)
        private val TAG = TextAttributesKey.createTextAttributesKey("MI.TAG", DefaultLanguageHighlighterColors.STATIC_METHOD)
        private val INSTRUCTION = TextAttributesKey.createTextAttributesKey("MI.INSTRUCTION", DefaultLanguageHighlighterColors.KEYWORD)
        private val MACRO = TextAttributesKey.createTextAttributesKey("MI.MACRO", DefaultLanguageHighlighterColors.KEYWORD)

        // literal
        private val INT = TextAttributesKey.createTextAttributesKey("MI.INT", DefaultLanguageHighlighterColors.NUMBER)
        private val STRING = TextAttributesKey.createTextAttributesKey("MI.STRING", DefaultLanguageHighlighterColors.STRING)
        private val BYTE = TextAttributesKey.createTextAttributesKey("MI.BYTE", DefaultLanguageHighlighterColors.CONSTANT)
        private val OTHER_LITERAL = TextAttributesKey.createTextAttributesKey("MI.OTHER_LITERAL", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL)
        private val BOOLEAN_LITERAL = TextAttributesKey.createTextAttributesKey("MI.BOOL_LITERAL", OTHER_LITERAL)
        private val UNIT_LITERAL = TextAttributesKey.createTextAttributesKey("MI.UNIT_LITERAL", OTHER_LITERAL)

        // generic symbols
        private val SEMI = TextAttributesKey.createTextAttributesKey("MI.SEMI", DefaultLanguageHighlighterColors.SEMICOLON)
        private val PAREN = TextAttributesKey.createTextAttributesKey("MI.PAREN", DefaultLanguageHighlighterColors.PARENTHESES)
        private val BRACES = TextAttributesKey.createTextAttributesKey("MI.PAREN", DefaultLanguageHighlighterColors.BRACES)
    }

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
        fillMap(keys, MichelsonElementTypes.MACROS, MACRO)

        keys.put(MichelsonTypes.INT, INT)
        keys.put(MichelsonTypes.BYTE, BYTE)
        keys.put(MichelsonTypes.STRING, STRING)
        keys.put(MichelsonTypes.UNIT, UNIT_LITERAL)
        fillMap(keys, MichelsonElementTypes.BOOLEAN, BOOLEAN_LITERAL)

        keys.put(MichelsonTypes.SEMI, SEMI)
        fillMap(keys, MichelsonElementTypes.PARENTHESES, PAREN)
        fillMap(keys, MichelsonElementTypes.BRACES, BRACES)
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> = pack(keys.get(tokenType))

    override fun getHighlightingLexer(): Lexer = MichelsonLexer()
}
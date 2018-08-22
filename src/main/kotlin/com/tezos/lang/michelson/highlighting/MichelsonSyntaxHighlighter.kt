package com.tezos.lang.michelson.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.lexer.MichelsonLexer
import java.util.HashMap

/**
 * @author jansorg
 */
class MichelsonSyntaxHighlighter : SyntaxHighlighterBase() {
    private val LINE_COMMENT = TextAttributesKey.createTextAttributesKey("MI.LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
    private val BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey("MI.BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)

    private val KEYWORD = TextAttributesKey.createTextAttributesKey("MI.KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
    private val TAG = TextAttributesKey.createTextAttributesKey("MI.TAG", KEYWORD)

    private val INT = TextAttributesKey.createTextAttributesKey("MI.INT", DefaultLanguageHighlighterColors.NUMBER)
    private val STRING = TextAttributesKey.createTextAttributesKey("MI.STRING", DefaultLanguageHighlighterColors.STRING)
    private val BYTE = TextAttributesKey.createTextAttributesKey("MI.BYTE", DefaultLanguageHighlighterColors.CONSTANT)

    private val keys = HashMap<IElementType, TextAttributesKey>()

    init {
        keys.put(MichelsonTypes.COMMENT_LINE, LINE_COMMENT)
        keys.put(MichelsonTypes.COMMENT_MULTI_LINE, BLOCK_COMMENT)

        keys.put(MichelsonTypes.INSTRUCTION_TOKEN, KEYWORD)
        keys.put(MichelsonTypes.TAG, TAG)

        keys.put(MichelsonTypes.INT, INT)
        keys.put(MichelsonTypes.BYTE, BYTE)
        keys.put(MichelsonTypes.STRING, STRING)
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> = pack(keys.get(tokenType))

    override fun getHighlightingLexer(): Lexer = MichelsonLexer()
}
package com.tezos.lang.michelson.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonLanguage
import com.tezos.lang.michelson.MichelsonTypes.*
import com.tezos.lang.michelson.lexer.MichelsonLexer
import com.tezos.lang.michelson.psi.impl.MichelsonPsiFileImpl

/**
 * @author jansorg
 */
class MichelsonParserDefinition : ParserDefinition {
    val MICHELSON_FILE_ELEMENT_TYPE = IFileElementType("MICHELSON_FILE", MichelsonLanguage);

    private val STRING_TOKENS = TokenSet.create(STRING)
    private val WHITESPACE_TOKENS = TokenSet.create(TokenType.WHITE_SPACE)
    private val COMMENT_TOKENS = TokenSet.create(COMMENT_LINE, COMMENT_MULTI_LINE)

    override fun createLexer(project: Project): Lexer = MichelsonLexer()

    override fun createParser(project: Project): PsiParser = MichelsonParser()

    override fun createFile(viewProvider: FileViewProvider): PsiFile = MichelsonPsiFileImpl(viewProvider)

    override fun spaceExistanceTypeBetweenTokens(first: ASTNode, second: ASTNode): ParserDefinition.SpaceRequirements = ParserDefinition.SpaceRequirements.MAY

    override fun getStringLiteralElements(): TokenSet = STRING_TOKENS

    override fun getFileNodeType(): IFileElementType = MICHELSON_FILE_ELEMENT_TYPE

    override fun getWhitespaceTokens(): TokenSet = WHITESPACE_TOKENS

    override fun getCommentTokens(): TokenSet = COMMENT_TOKENS

    override fun createElement(node: ASTNode): PsiElement = throw UnsupportedOperationException()
}
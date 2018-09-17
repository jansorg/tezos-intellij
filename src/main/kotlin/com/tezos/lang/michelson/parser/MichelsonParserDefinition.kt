package com.tezos.lang.michelson.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonLanguage
import com.tezos.lang.michelson.lexer.MichelsonTokenSets
import com.tezos.lang.michelson.lexer.MichelsonLexer
import com.tezos.lang.michelson.psi.impl.MichelsonPsiFileImpl

/**
 * Parser definition for the Michelson file type.
 *
 * @author jansorg
 */
class MichelsonParserDefinition : ParserDefinition {
    private companion object {
        private val MICHELSON_FILE_ELEMENT_TYPE = IFileElementType("MICHELSON_FILE", MichelsonLanguage);
    }

    override fun createLexer(project: Project): Lexer = MichelsonLexer()

    override fun createParser(project: Project): PsiParser = MichelsonParser()

    override fun createFile(viewProvider: FileViewProvider): PsiFile = MichelsonPsiFileImpl(viewProvider)

    override fun spaceExistanceTypeBetweenTokens(first: ASTNode, second: ASTNode): ParserDefinition.SpaceRequirements = ParserDefinition.SpaceRequirements.MAY

    override fun getFileNodeType(): IFileElementType = MICHELSON_FILE_ELEMENT_TYPE

    override fun getStringLiteralElements(): TokenSet = MichelsonTokenSets.STRING_TOKENS

    override fun getWhitespaceTokens(): TokenSet = MichelsonTokenSets.WHITESPACE_TOKENS

    override fun getCommentTokens(): TokenSet = MichelsonTokenSets.COMMENT_TOKENS

    override fun createElement(node: ASTNode): PsiElement = throw UnsupportedOperationException()
}
package com.tezos.lang.michelson.editor.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonTypes.*
import com.tezos.lang.michelson.lexer.MichelsonTokenSets
import com.tezos.lang.michelson.parser.MichelsonElementSets
import com.tezos.lang.michelson.psi.PsiAnnotation
import com.tezos.lang.michelson.psi.PsiComplexType
import com.tezos.lang.michelson.psi.PsiContract


/**
 * @author jansorg
 */
class MichelsonBlock(node: ASTNode, wrap: Wrap, alignment: Alignment?, private val spacing: SpacingBuilder, private val _indent: Indent? = null, private val codeStyle: CodeStyleSettings, val parent: MichelsonBlock? = null) : AbstractBlock(node, wrap, alignment) {
    private val blockChildAlign = Alignment.createAlignment(true, Alignment.Anchor.LEFT)
    private val lineCommentAlign = Alignment.createAlignment(true, Alignment.Anchor.LEFT)
    private val annotationAlign = Alignment.createAlignment(true, Alignment.Anchor.LEFT)

    companion object {
        val ALWAYS_WRAPPED_BLOCKS = TokenSet.create(COMMENT_MULTI_LINE, SECTION)

        /**
         * @return true if this comment covers the full line and doesn't have instructions, ... in front
         */
        private fun ASTNode.isCompleteLineComment(): Boolean {
            return this.elementType == COMMENT_LINE && this.treePrev?.textContains('\n') ?: false
        }

        private fun ASTNode.nextNonWhitespace(): ASTNode? {
            var n = this.treeNext
            while (n != null) {
                if (n.elementType != WHITE_SPACE) {
                    return n
                }
                n = n.treeNext
            }
            return null
        }
    }

    override fun buildChildren(): MutableList<Block> {
        val michelsonSettings = codeStyle.getCustomSettings(MichelsonCodeStyleSettings::class.java)
        val blocks = ArrayList<Block>()
        val nodePsi = node.psi

        var child = myNode.firstChildNode
        while (child != null) {
            val childType = child.elementType
            val block = when {
                // special block for line comments
                childType == COMMENT_LINE -> {
                    // align comments in instruction blocks
                    val indent = when (node.elementType.isMichelsonBlock()) {
                        true -> Indent.getNormalIndent()
                        false -> Indent.getNoneIndent()
                    }

                    val commentCoversLine = child.isCompleteLineComment()
                    val nextIsBlock = child.nextNonWhitespace()?.elementType.isMichelsonBlock()

                    val alignment = when {
                        !commentCoversLine && michelsonSettings.LINE_COMMENT_ALIGN -> {
                            // align end-of-line comments with other eol-style comments of the same block
                            findNextHierarchyParent(MichelsonElementSets.BLOCKS)?.lineCommentAlign
                        }
                        commentCoversLine && nextIsBlock -> {
                            // align comments with the blocks if a comment directly precedes the block, i.e. is a note on that particular block
                            blockChildAlign
                        }
                        else -> null
                    }

                    MichelsonLineCommentBlock(child, Wrap.createWrap(WrapType.NONE, false), alignment, spacing, indent, codeStyle, parent = this)
                }

                ALWAYS_WRAPPED_BLOCKS.contains(childType) -> {
                    val indent = if (node.elementType.isMichelsonBlock()) Indent.getNormalIndent() else Indent.getNoneIndent()
                    MichelsonBlock(child, Wrap.createWrap(WrapType.ALWAYS, false), null, spacing, indent, codeStyle, parent = this)
                }

                // align chained blocks in instructions
                childType.isMichelsonBlock() -> {
                    val wrap = Wrap.createWrap(WrapType.ALWAYS, false)
                    val indent = Indent.getIndent(Indent.Type.NORMAL, true, true)
                    MichelsonBlock(child, wrap, blockChildAlign, spacing, indent, codeStyle, parent = this)
                }

                // align types in complex types which contain at least one complex type
                (childType == GENERIC_TYPE || childType == COMPLEX_TYPE) && nodePsi is PsiComplexType && nodePsi.hasComplexTypes() -> {
                    val alignment = if (michelsonSettings.COMPLEX_TYPE_ALIGN) blockChildAlign else null
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NORMAL, false), alignment, spacing, Indent.getNormalIndent(), codeStyle, parent = this)
                }

                MichelsonElementSets.INSTRUCTIONS.contains(childType) -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true), null, spacing, Indent.getNormalIndent(), codeStyle, parent = this)
                }

                childType == CONTRACT_WRAPPER && node.elementType == CREATE_CONTRACT_INSTRUCTION -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NORMAL, false), Alignment.createAlignment(), spacing, Indent.getNormalIndent(), codeStyle, parent = this)
                }
                childType == CONTRACT && node.elementType == CONTRACT_WRAPPER -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NORMAL, false), null, spacing, Indent.getNormalIndent(), codeStyle, parent = this)
                }

                MichelsonTokenSets.ANNOTATIONS.contains(childType) && michelsonSettings.COMPLEX_TYPE_ALIGN_ANNOTATIONS -> {
                    val isInComplexType = (nodePsi as PsiAnnotation).findParentType() != null
                    val alignmentReference = if (isInComplexType) parent else this

                    // align all annotations of top-level elements of a complex type with each other
                    // an annotation on a complex type itself must be aligned with the next parent of this type
                    val alignment = alignmentReference?.findNextHierarchyParent(TokenSet.create(COMPLEX_TYPE), false)?.annotationAlign
                    alignment?.let {
                        MichelsonBlock(child, Wrap.createWrap(WrapType.NONE, false), alignment, spacing, Indent.getNoneIndent(), codeStyle, parent = this)
                    }
                }

                else -> null
            }

            // fallback to default block if it's not whitespace or a zero-length block, e.g. an error marker which spans no token
            val finalBlock = block ?: when {
                childType != WHITE_SPACE && child.textLength > 0 -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(), spacing, Indent.getNoneIndent(), codeStyle, parent = this)
                }
                else -> null
            }

            if (finalBlock != null) {
                blocks.add(finalBlock)
            }

            child = child.treeNext
        }

        return blocks
    }

    private fun findNextHierarchyParent(elementTypes: TokenSet, acceptCurrent: Boolean = true): MichelsonBlock? {
        var e: MichelsonBlock? = if (acceptCurrent) this else this.parent
        while (e != null) {
            if (elementTypes.contains(e.node.elementType)) {
                return e
            }
            e = e.parent
        }
        return null
    }

    override fun getIndent(): Indent? {
        // this is a nasty to convince IntelliJ do use our configured indent size
        // by default IntelliJ detects the indent sizes of the current file and uses the max size for newly inserted lines, e.g. in EnterHandler
        // the formatted code {} has the closing curly brace on line offet 5 because Michelson requires closing braces to be at least
        // on the same offset as the opening brace in code {
        // IntelliJ detects this as a regular indent and then uses indent size 5 for all inserted newlines
        // using an indent type != NONE and != NORMAL will make this indent not be counted in the indente sixe detection
        // see com.intellij.psi.codeStyle.autodetect.IndentOptionsDetectorImpl
        if (node.elementType == RIGHT_CURLY && parent?.node?.elementType == BLOCK_INSTRUCTION) {
            return Indent.getSpaceIndent(0, true)
        }

        return _indent
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacing.getSpacing(this, child1, child2)
    }

    override fun isLeaf(): Boolean {
        return node.firstChildNode == null
    }

    override fun getChildIndent(): Indent? {
        if (node is PsiFile || node is PsiContract) {
            return Indent.getNoneIndent()
        }

        if (node.elementType.isMichelsonBlock()) {
            return Indent.getIndent(Indent.Type.NORMAL, true, false)
        }

        return super.getChildIndent()
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        if (node.elementType.isMichelsonBlock()) {
            // force no child alignment and indent of first element in empty blocks
            return ChildAttributes(Indent.getIndent(Indent.Type.NORMAL, true, false), null)
        }
        return super.getChildAttributes(newChildIndex)
    }
}

fun IElementType?.isMichelsonBlock(): Boolean = this != null && MichelsonElementSets.BLOCKS.contains(this)
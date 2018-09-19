package com.tezos.lang.michelson.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
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


/**
 * @author jansorg
 */
class MichelsonBlock(node: ASTNode, wrap: Wrap, alignment: Alignment, private val spacing: SpacingBuilder, private val _indent: Indent? = null, val codeStyle: CodeStyleSettings, val parent: MichelsonBlock? = null) : AbstractBlock(node, wrap, alignment) {
    private val blockChildAlign = Alignment.createAlignment(true, Alignment.Anchor.LEFT)
    private val lineCommentAlign = Alignment.createAlignment(true, Alignment.Anchor.LEFT)
    private val annotationAlign = Alignment.createAlignment(true, Alignment.Anchor.LEFT)

    companion object {
        val WRAPPED_BLOCKS = TokenSet.create(COMMENT_MULTI_LINE, SECTION)

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
                    val indent = when (node.elementType == BLOCK_INSTRUCTION) {
                        true -> Indent.getNormalIndent()
                        false -> Indent.getNoneIndent()
                    }

                    val commentCoversLine = child.isCompleteLineComment()
                    val nextIsBlock = child.nextNonWhitespace()?.elementType == BLOCK_INSTRUCTION

                    val alignment = when {
                        !commentCoversLine && michelsonSettings.LINE_COMMENT_ALIGN -> {
                            // align end-of-line comments with other eol-style comments of the same block
                            findNextHierarchyParent(BLOCK_INSTRUCTION)?.lineCommentAlign
                        }
                        commentCoversLine && nextIsBlock -> {
                            // align comments with the blocks if a comment directly precedes the block, i.e. is a note on that particular block
                            blockChildAlign
                        }
                        else -> null
                    } ?: Alignment.createAlignment()

                    MichelsonLineCommentBlock(child, Wrap.createWrap(WrapType.NONE, false), alignment, spacing, indent, codeStyle, parent = this)
                }

                WRAPPED_BLOCKS.contains(childType) -> {
                    val indent = if (node.elementType == BLOCK_INSTRUCTION) Indent.getNormalIndent() else Indent.getNoneIndent()
                    MichelsonBlock(child, Wrap.createWrap(WrapType.ALWAYS, false), Alignment.createAlignment(), spacing, indent, codeStyle, parent = this)
                }

                // align chained blocks in instructions
                childType == BLOCK_INSTRUCTION -> {
                    val wrap = Wrap.createWrap(WrapType.ALWAYS, false)
                    val indent = Indent.getIndent(Indent.Type.NORMAL, true, true)
                    MichelsonBlock(child, wrap, blockChildAlign, spacing, indent, codeStyle, parent = this)
                }

                // align types in complex types which contain at least one complex type
                (childType == GENERIC_TYPE || childType == LEFT_PAREN) && nodePsi is PsiComplexType && !nodePsi.hasSimpleTypes() -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NORMAL, false), blockChildAlign, spacing, Indent.getNormalIndent(), codeStyle, parent = this)
                }

                MichelsonElementSets.INSTRUCTIONS.contains(childType) -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true), Alignment.createAlignment(), spacing, Indent.getNormalIndent(), codeStyle, parent = this)
                }

                MichelsonTokenSets.ANNOTATIONS.contains(childType) && michelsonSettings.COMPLEX_TYPE_ALIGN_ANNOTATIONS -> {
                    val isInComplexType = (nodePsi as PsiAnnotation).findParentType() != null
                    val alignmentReference = if (isInComplexType) parent else this

                    // align all annotations of top-level elements of a complex type with each other
                    // an annotation on a complex type itself must be aligned with the next parent of this type
                    val alignment = alignmentReference?.findNextHierarchyParent(COMPLEX_TYPE, false)?.annotationAlign
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

    private fun findNextHierarchyParent(elementType: IElementType, acceptCurrent: Boolean = true): MichelsonBlock? {
        var e: MichelsonBlock? = if (acceptCurrent) this else this.parent
        while (e != null) {
            if (e.node.elementType == elementType) {
                return e
            }
            e = e.parent
        }
        return null
    }

    override fun getIndent(): Indent? {
        return _indent
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        val v = spacing.getSpacing(this, child1, child2)
        return v
    }

    override fun getChildIndent(): Indent? {
        return if (node.elementType == BLOCK_INSTRUCTION) {
            Indent.getNormalIndent()
        } else {
            null
        }
    }

    override fun isLeaf(): Boolean {
        return node.firstChildNode == null
    }
}
package com.tezos.lang.michelson.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonTypes.*
import com.tezos.lang.michelson.parser.MichelsonElementSets
import com.tezos.lang.michelson.psi.PsiComplexType


/**
 * @author jansorg
 */
class MichelsonBlock(node: ASTNode, wrap: Wrap, alignment: Alignment, private val spacing: SpacingBuilder, private val _indent: Indent? = null, val codeStyle: CodeStyleSettings, val parent: MichelsonBlock? = null) : AbstractBlock(node, wrap, alignment) {
    private val blockChildAlign = Alignment.createAlignment(true, Alignment.Anchor.LEFT)

    companion object {
        val WRAPPED_BLOCKS = TokenSet.create(COMMENT_MULTI_LINE, SECTION)
    }

    override fun buildChildren(): MutableList<Block> {
        val blocks = ArrayList<Block>()
        val nodePsi = node.psi

        var child = myNode.firstChildNode
        while (child != null) {
            val childType = child.elementType
            val block = when {
                // special block for line comments
                childType == COMMENT_LINE -> {
                    val indent = if (node.elementType == BLOCK_INSTRUCTION) Indent.getNormalIndent() else Indent.getNoneIndent()
                    MichelsonLineCommentBlock(child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(), spacing, indent, codeStyle, parent = this)
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

                childType != WHITE_SPACE && child.textLength > 0 -> { // exclude whitespace and empty error markers
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(), spacing, Indent.getNoneIndent(), codeStyle, parent = this)
                }

                else -> null
            }

            if (block != null) {
                blocks.add(block)
            }

            child = child.treeNext
        }

        return blocks
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
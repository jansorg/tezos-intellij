package com.tezos.lang.michelson.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.formatter.common.AbstractBlock
import com.tezos.lang.michelson.MichelsonTypes.*


/**
 * @author jansorg
 */
class MichelsonBlock(node: ASTNode, wrap: Wrap, alignment: Alignment, private val spacing: SpacingBuilder, private val _indent: Indent? = null) : AbstractBlock(node, wrap, alignment) {
    override fun buildChildren(): MutableList<Block> {
        val blocks = ArrayList<Block>()

        var child = myNode.firstChildNode
        while (child != null) {
            val elementType = child.elementType

            val block = when {
                elementType == CONTRACT || elementType == SECTION -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.ALWAYS, false), Alignment.createAlignment(), spacing, Indent.getNoneIndent())
                }

                elementType == BLOCK_INSTRUCTION -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NORMAL, false), Alignment.createAlignment(), spacing, Indent.getNormalIndent())
                }

                elementType == GENERIC_INSTRUCTION || elementType == MACRO_INSTRUCTION -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true), Alignment.createAlignment(), spacing, Indent.getNormalIndent())
                }

                elementType == LEFT_CURLY -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(), spacing, Indent.getNoneIndent())
                }
                elementType == RIGHT_CURLY -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(), spacing, Indent.getNoneIndent())
                }

                elementType != WHITE_SPACE -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(), spacing, Indent.getNoneIndent())
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

    var override = true
    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        val s = spacing.getSpacing(this, child1, child2)
        return s
    }

    override fun isLeaf(): Boolean {
        return node.firstChildNode == null
    }
}
package com.tezos.lang.michelson.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiConditionalExpression
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.formatter.common.AbstractBlock
import com.tezos.lang.michelson.MichelsonTypes.*
import com.tezos.lang.michelson.psi.PsiGenericInstruction


/**
 * @author jansorg
 */
class MichelsonBlock(node: ASTNode, wrap: Wrap, alignment: Alignment, private val spacing: SpacingBuilder, private val _indent: Indent? = null, val parent: MichelsonBlock? = null) : AbstractBlock(node, wrap, alignment) {
    private val blockChildAlign = Alignment.createAlignment(true, Alignment.Anchor.LEFT)

    override fun buildChildren(): MutableList<Block> {
        val blocks = ArrayList<Block>()
        val nodePsi = node.psi

        var child = myNode.firstChildNode
        while (child != null) {
            val childType = child.elementType
            val block = when {
                childType == CONTRACT || childType == SECTION -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.ALWAYS, false), Alignment.createAlignment(), spacing, Indent.getNoneIndent(), parent = this)
                }

                // align chained blocks in instructions
                childType == BLOCK_INSTRUCTION && nodePsi is PsiGenericInstruction -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.ALWAYS, false), blockChildAlign, spacing, Indent.getNormalIndent(), parent = this)
                }
                childType == BLOCK_INSTRUCTION -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NORMAL, false), Alignment.createAlignment(), spacing, Indent.getNoneIndent(), parent = this)
                }

                childType == GENERIC_INSTRUCTION || childType == MACRO_INSTRUCTION -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true), Alignment.createAlignment(), spacing, Indent.getNormalIndent(), parent = this)
                }

                childType == LEFT_CURLY -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(), spacing, Indent.getNoneIndent(), parent = this)
                }
                childType == RIGHT_CURLY -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(), spacing, Indent.getNoneIndent(), parent = this)
                }

                childType == BLOCK_INSTRUCTION && nodePsi is PsiConditionalExpression -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.ALWAYS, false), blockChildAlign, spacing, Indent.getNormalIndent(), parent = this)
                }

                childType != WHITE_SPACE -> {
                    MichelsonBlock(child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(), spacing, Indent.getNoneIndent(), parent = this)
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
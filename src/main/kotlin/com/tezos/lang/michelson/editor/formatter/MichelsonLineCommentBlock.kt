package com.tezos.lang.michelson.editor.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.util.containers.ContainerUtil
import java.util.*

/**
 * Special block to create sub-blocks for the line comment prefix # and following content of the line comment.
 * This is needed to be able to insert a leading space when the formatting options LINE_COMMENT_LEADING_SPACE is enabled.
 *
 * @author jansorg
 */
class MichelsonLineCommentBlock(node: ASTNode, wrap: Wrap, alignment: Alignment?, private val spacing: SpacingBuilder, private val indent: Indent?, codeStyle: CodeStyleSettings, val parent: MichelsonBlock? = null) : AbstractBlock(node, wrap, alignment) {
    private val michelsonStyle = codeStyle.getCustomSettings(MichelsonCodeStyleSettings::class.java)

    override fun buildChildren(): MutableList<Block> {
        return when {
            michelsonStyle.LINE_COMMENT_LEADING_SPACE -> {
                val blocks = ContainerUtil.newSmartList<Block>()
                val nodeRange = node.textRange
                val spaceCount = StringUtil.countChars(node.text, ' ', 1, true)

                if (nodeRange.length > 1 + spaceCount) {
                    blocks += MichelsonNodeLeafBlock(node, TextRange.from(nodeRange.startOffset, 1))

                    val contentTextRange = TextRange.create(nodeRange.startOffset + 1 + spaceCount, nodeRange.endOffset)
                    blocks += MichelsonNodeLeafBlock(node, contentTextRange)
                }

                blocks
            }

            else -> Collections.emptyList()
        }
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        // needed to add spacing between bested prefix and content blocks
        return spacing.getSpacing(this, child1, child2)
    }

    override fun getIndent(): Indent? = indent

    override fun isLeaf(): Boolean = !michelsonStyle.LINE_COMMENT_LEADING_SPACE
}

/**
 * Simple block for a leaf node which only spans a part of a node's text range.
 * @author jansorg
 */
private class MichelsonNodeLeafBlock(node: ASTNode, private val contentTextRange: TextRange) : AbstractBlock(node, null, null) {
    override fun getTextRange(): TextRange = contentTextRange

    override fun buildChildren(): MutableList<Block> = Collections.emptyList()

    override fun toString(): String = contentTextRange.substring(node.text)

    override fun isLeaf(): Boolean = true

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = null
}

package com.tezos.lang.michelson.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonTypes

/**
 * Folding builder for the Michelson language.
 * @author jansorg
 */
class MichelsonFoldingBuilder : FoldingBuilder, DumbAware {
    companion object {
        private val foldedTypes = TokenSet.create(MichelsonTypes.BLOCK_INSTRUCTION)
    }

    override fun getPlaceholderText(node: ASTNode): String? {
        return when {
            node.elementType == MichelsonTypes.BLOCK_INSTRUCTION -> "{...}"
            node.elementType == MichelsonTypes.CONTRACT_WRAPPER -> "{CONTRACT ...}"
            else -> null
        }
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false

    override fun buildFoldRegions(node: ASTNode, document: Document): Array<FoldingDescriptor> {
        val result = ArrayList<FoldingDescriptor>()
        buildRecursively(node, document, result)
        return result.toTypedArray()
    }

    private fun buildRecursively(node: ASTNode, document: Document, result: MutableList<FoldingDescriptor>) {
        if (node.elementType == MichelsonTypes.CONTRACT_WRAPPER) {
            result += FoldingDescriptor(node, node.textRange)
        } else if (foldedTypes.contains(node.elementType) && node.textLength > 2) {
            result += FoldingDescriptor(node, node.textRange)
        }

        for (c in node.getChildren(null)) {
            buildRecursively(c, document, result)
        }
    }
}

package com.tezos.lang.michelson.editor.documentation

import com.intellij.lang.documentation.DocumentationProviderEx
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ResourceUtil
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.psi.PsiInstruction

/**
 * Provides documentation for Michelson instructions.
 *
 * @author jansorg
 */
class MichelsonDocumentationProvider : DocumentationProviderEx() {
    companion object {
        private val END_TOKENS = TokenSet.create(MichelsonTypes.SEMI, MichelsonTypes.RIGHT_CURLY, MichelsonTypes.RIGHT_PAREN, TokenType.WHITE_SPACE)
    }

    override fun getCustomDocumentationElement(editor: Editor, file: PsiFile, contextElement: PsiElement?): PsiElement? {
        if (contextElement == null) {
            return null
        }

        val context = if (END_TOKENS.contains(contextElement.node.elementType)) {
            PsiTreeUtil.prevLeaf(contextElement)
        } else {
            contextElement
        }

        return when (context) {
            null -> null
            else -> {
                PsiTreeUtil.findFirstParent(context) {
                    when (it) {
                        is PsiInstruction -> true
                        else -> false
                    }
                }
            }
        }
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return when (element) {
            is PsiInstruction -> buildInstructionDocs(element)
            else -> null
        }
    }

    private fun buildInstructionDocs(element: PsiInstruction): String? {
        val name = element.instructionName
        return name?.let {
            loadDefFile("/documentation/instruction/${it.toLowerCase()}.txt", it)
        }
    }

    private fun loadDefFile(path: String, title: String): String? {
        val content = this::class.java.getResource(path)?.let {
            ResourceUtil.loadText(it)
        } ?: return null

        // lines starting with :: are defining the stack transformation
        // lines starting with > define the logic
        // lines following the last line starting with :: or > provide a textual description of the instruction

        val transformations = mutableListOf<String>()
        val logic = mutableListOf<String>()
        val desc = mutableListOf<String>()

        var last: MutableList<String>? = null
        for (line in content.lines()) {
            val trimmed = line.trim()
            when {
                line.startsWith("  ") -> when (last) {
                    null -> throw IllegalStateException("Unhandled continuation in $path")
                    else -> last.add(line)
                }
                trimmed.startsWith("::") -> {
                    transformations += trimmed
                    last = transformations
                }
                trimmed.startsWith(">") -> {
                    logic += trimmed
                    last = logic
                }
                trimmed.isNotEmpty() -> {
                    desc += trimmed
                    last = desc
                }
            }
        }

        val transformBlock = if (transformations.isEmpty()) "" else "<em>Stack transformation</em><br><pre><code>" + transformations.joinToString("<br>") + "</code></pre><br>"
        val logicBlock = if (logic.isEmpty()) "" else "<em>Implementation</em><br><pre><code>" + logic.joinToString("<br>") + "</code></pre><br>"
        val descBlock = if (desc.isEmpty()) "" else "<div>" + desc.joinToString("<br>") + "</div><br>"

        //language=HTML
        return """
            <strong>$title</strong><br>
            $descBlock
            $transformBlock
            $logicBlock
        """.trimIndent()
    }
}

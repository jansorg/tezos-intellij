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
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.psi.*

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
                        is PsiType -> true
                        is PsiTag -> true
                        else -> false
                    }
                }
            }
        }
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return when (element) {
            is PsiMacroInstruction -> buildMacroInstructionDocs(element)
            is PsiInstruction -> buildInstructionDocs(element)
            is PsiType -> buildTypeDocs(element)
            is PsiTag -> buildTagDocs(element)
            else -> null
        }
    }

    private fun buildTypeDocs(element: PsiType): String? {
        return when (element) {
            is PsiNamedType -> buildTypeReport(element.typeNameString)
            else -> null
        }
    }

    private fun buildTypeReport(name: String): String? {
        val content = this::class.java.getResource("/documentation/type/${name.toLowerCase()}.txt")?.let {
            ResourceUtil.loadText(it)
        } ?: return null

        val (firstLine, finalContent) = if (content.startsWith(": ")) {
            // the first line has a more specific notation of the type, e.g. with type parameters, use it instead
            val lines = content.lines()
            listOf(lines[0].substring(2).trim(), lines.subList(1, lines.size).joinToString("\n"))
        } else {
            listOf(name, content)
        }

        //language=HTML
        return """
            <table width='100%'><tr><td><strong><code>$firstLine</code></strong></td><td style="text-align:right;"><em>TYPE</em></td></tr></table>
            $finalContent
        """.trimIndent()
    }

    private fun buildTagDocs(element: PsiTag): String? {
        val name = element.tagToken?.text ?: return null
        return buildTagReport(name)
    }

    private fun buildTagReport(name: String): String? {
        val content = this::class.java.getResource("/documentation/tag/${name.toLowerCase()}.txt")?.let {
            ResourceUtil.loadText(it)
        } ?: return null

        val (firstLine, finalContent) = if (content.startsWith(": ")) {
            // the first line has a more specific notation of the type, e.g. with type parameters, use it instead
            val lines = content.lines()
            listOf(lines[0].substring(2).trim(), lines.subList(1, lines.size).joinToString("\n"))
        } else {
            listOf(name, content)
        }

        //language=HTML
        return """
            <table width='100%'><tr><td><strong><code>$firstLine</code></strong></td><td style="text-align:right;"><em>CONSTRUCTOR</em></td></tr></table>
            $finalContent
        """.trimIndent()
    }

    private fun buildMacroInstructionDocs(element: PsiMacroInstruction): String? {
        val name = element.instructionName ?: return null
        val macro = MichelsonLanguage.MACROS.first { it.validate(name) == null }

        val content = macro.helpContentFile(name)?.let { path ->
            this::class.java.getResource("/documentation/macro/$path")?.let {
                ResourceUtil.loadText(it)
            }
        }

        val expanded = macro.expand(name, true)
        return instructionReport(content ?: "", name, "macro", if (expanded != null) {
            "<code>$expanded</code><br><br>"
        } else "")
    }

    private fun buildInstructionDocs(element: PsiInstruction): String? {
        val name = element.instructionName
        return name?.let {
            loadReport("/documentation/instruction/${it.toLowerCase()}.txt", it, "Instruction")
        }
    }

    private fun loadReport(classpath: String, title: String, subtitle: String = "", prefix: String = ""): String? {
        return this::class.java.getResource(classpath)?.let {
            val content = ResourceUtil.loadText(it)
            return instructionReport(content, title, subtitle, prefix)
        }
    }

    private fun instructionReport(content: String, title: String, subtitle: String = "", prefix: String = ""): String? {
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
                    null -> throw IllegalStateException("Unhandled continuation")
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
            <table width='100%'><tr><td><strong><code>$title</code></strong></td><td style='text-align:right;'><em>${subtitle.toUpperCase()}</em></td></tr></table>
            $prefix
            $descBlock
            $transformBlock
            $logicBlock
        """.trimIndent()
    }
}

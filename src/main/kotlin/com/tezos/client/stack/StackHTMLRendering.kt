@file:Suppress("CssInvalidPropertyValue")

package com.tezos.client.stack

import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.ui.JBColor
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.awt.Color
import java.awt.Font

data class RenderStyle(val textColor: Color?, val bold: Boolean, val italic: Boolean) {
    constructor(attr: TextAttributes) : this(attr.foregroundColor, attr.fontType and Font.BOLD == Font.BOLD, attr.fontType and Font.ITALIC == Font.ITALIC)
}

/**
 * Options to configure the HTML rendering of a stack.
 * @param markUnchanged If true then unmodified stack frames will be grayed-out in the rendering
 * @param alignStacks If true then the two stacks will be aligned to the bottom of the stack
 * @param showAnnotations If true then annotations will be rendered for the types
 * @param codeFont The HTML font family to use for the rendering of source code
 * @param codeFontSizePt The font size to use for the rendering
 */
data class RenderOptions(
        val markUnchanged: Boolean = false,
        val alignStacks: Boolean = false,
        val showAnnotations: Boolean = false,
        val codeFont: String = "monospace",
        val codeFontSizePt: Double = 11.0,
        val showColors: Boolean = false,
        val typeNameStyle: RenderStyle? = null,
        val typeAnnotationStyle: RenderStyle? = null,
        val fieldAnnotationStyle: RenderStyle? = null,
        val varAnnotationStyle: RenderStyle? = null
)

/**
 * @author jansorg
 */
@Suppress("CssUnusedSymbol")
class StackRendering {
    companion object {
        private fun style(selector: String, s: RenderStyle?): String {
            return when (s) {
                null -> ""
                else -> {
                    val color = s.textColor?.let { "color: ${it.asHexString()}; " }
                    val fontWeight = if (s.bold) "font-weight: \"bold\"; " else ""
                    val fontStyle = if (s.italic) "font-style: \"italic\"; " else ""

                    return """
                        $selector { $color $fontWeight $fontStyle }
                    """.trimIndent()
                }
            }
        }
    }

    private fun styles(opts: RenderOptions): String {
        //language=CSS
        return """
            html, body, p, div, table, tr, td, th { margin: 0; padding: 0; border: none; border-collapse: collapse; }
            html { padding: 1em; }
            table { width:100%; }
            th { width: 50%; font-size:1.1em; font-weight:bold; color:${JBColor.darkGray.asHexString()}; padding-bottom:6px; }
            td { font-family:"${opts.codeFont}", monospace; font-size: ${opts.codeFontSizePt}pt; padding: 2px 0 4px 0; }

            .left { text-align:left; }
            .right { text-align:right; }
            .content { border-bottom:1px solid ${JBColor.LIGHT_GRAY.asHexString()}; }

            .last-row { border-bottom:none; }

            ${style(".style-type-name", opts.typeNameStyle)}
            ${style(".style-annotation-type", opts.typeAnnotationStyle)}
            ${style(".style-annotation-field", opts.fieldAnnotationStyle)}
            ${style(".style-annotation-var", opts.varAnnotationStyle)}
        """.trimIndent()
    }

    fun render(stack: MichelsonStackTransformation, opts: RenderOptions): String {
        val maxSize = Math.max(stack.before.size, stack.after.size)
        val rowOffsetLeft = if (opts.alignStacks) maxSize - stack.before.size else 0
        val rowOffsetRight = if (opts.alignStacks) maxSize - stack.after.size else 0

        return StringBuilder().appendHTML().html {
            head { style(type = "text/css") { unsafe { +styles(opts) } } }
            body {
                table {
                    tr {
                        th(classes = "left") { +"Before" }
                        th(classes = "right") { +"After" }
                    }

                    for (i in 0 until maxSize) {
                        tr("content" + if (i == maxSize - 1) " last-row" else "") {
                            val left = if (i < rowOffsetLeft) null else stack.before.frames.getOrNull(i - rowOffsetLeft)
                            val right = if (i < rowOffsetRight) null else stack.after.frames.getOrNull(i - rowOffsetRight)

                            val changed = if (opts.markUnchanged && left != null && right != null) {
                                !left.equals(right, opts.showAnnotations)
                            } else {
                                null
                            }

                            column(true, changed, left, opts.showAnnotations, opts.showColors)
                            column(false, null, right, opts.showAnnotations, opts.showColors)
                        }
                    }
                }
            }
        }.toString()
    }

    private fun TR.column(isLeft: Boolean, changed: Boolean?, frame: MichelsonStackFrame?, showAnnotations: Boolean, colored: Boolean) {
        td(if (isLeft) "left" else "right") {
            attributes += ("valign" to "top")
            if (changed != null && !changed) {
                attributes += "style" to "color: ${JBColor.gray.asHexString()};"
            }

            if (frame != null) {
                stackInfo(frame.type, showAnnotations, colored)
            }
        }
    }
}

private fun Color.asHexString(): String {
    return "#" + String.format("%02x%02x%02x", red, green, blue)
}

private fun TD.stackInfo(type: MichelsonStackType, showAnnotations: Boolean = false, colored: Boolean = false) {
    val wrap = type.arguments.isNotEmpty() && type.name.isNotEmpty()

    val (prefix, suffix) = when (wrap || type.name.isNotEmpty() && showAnnotations && type.annotations.isNotEmpty()) {
        true -> arrayOf("(", ")")
        false -> arrayOf<String?>(null, null)
    }

    if (prefix != null) {
        +prefix
    }

    if (type.name.isNotEmpty()) {
        when {
            colored -> span("style-type-name") { +type.name }
            type.name.isNotEmpty() -> +type.name
        }
    }

    for (arg in type.arguments) {
        +" "
        stackInfo(arg, showAnnotations)
    }

    if (showAnnotations && type.annotations.isNotEmpty()) {
        for (annotation in type.annotations) {
            +" "
            span("style-annotation-type") {
                +annotation.value
            }
        }
    }

    if (suffix != null) {
        +suffix
    }
}
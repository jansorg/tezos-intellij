@file:Suppress("CssInvalidPropertyValue")

package com.tezos.client.stack

import com.intellij.ui.JBColor
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.awt.Color

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
        val codeFontSizePt: Double = 11.0)

/**
 * @author jansorg
 */
@Suppress("CssUnusedSymbol")
class StackRendering {
    private fun styles(opts: RenderOptions): String {
        //language=CSS
        return """
            html, body, p, div, table, tr, td, th { margin: 0; padding: 0; border: none; border-collapse: collapse; }
            html { padding: 1em; }
            table { width:100%; }
            th { width: 50%; font-size:1.1em; font-weight:bold; color:${JBColor.darkGray.asHexString()}; padding-bottom:6px; }
            td { font-family:"${opts.codeFont}", monospace; font-size: ${opts.codeFontSizePt}pt; padding: 2px 0 4px 0; }

            .left { text-align:left; }
            .right { text-align: right; }
            .content { border-bottom: 1px solid ${JBColor(Color(197, 197, 197), Color(90, 90, 90)).asHexString()}; }

            .last-row { border-bottom: none; }
        """.trimIndent()
    }

    fun render(stack: MichelsonStackTransformation, opts: RenderOptions): String {
        val maxSize = Math.max(stack.before.size, stack.after.size)
        val rowOffsetLeft = if (opts.alignStacks) maxSize - stack.before.size else 0
        val rowOffsetRight = if (opts.alignStacks) maxSize - stack.after.size else 0

        return StringBuilder().appendHTML().html {
            head { style { unsafe { +styles(opts) } } }
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

                            column(true, changed, left, opts.showAnnotations)
                            column(false, null, right, opts.showAnnotations)
                        }
                    }
                }
            }
        }.toString()
    }

    private fun TR.column(isLeft: Boolean, changed: Boolean?, frame: MichelsonStackFrame?, showAnnotations: Boolean) {
        td(if (isLeft) "left" else "right") {
            attributes += ("valign" to "top")
            if (changed != null && !changed) {
                attributes += "style" to "color: ${JBColor.gray.asHexString()};"
            }

            frame?.let { +it.type.asString(showAnnotations) }
        }
    }
}

fun Color.asHexString(): String {
    return "#" + String.format("%02x%02x%02x", red, green, blue)
}
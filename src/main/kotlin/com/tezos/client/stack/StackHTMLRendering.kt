@file:Suppress("CssInvalidPropertyValue")

package com.tezos.client.stack

import com.intellij.ui.JBColor
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.awt.Color

data class RenderOptions(
        val markUnchanged: Boolean = false,
        val highlightChanges: Boolean = false,
        val alignStacks: Boolean = false,
        val showAnnotations: Boolean = false,
        val codeFont: String = "monospace",
        val codeFontSizePt: Double = 11.0)

@Suppress("UNUSED_PARAMETER")
/**
 * @author jansorg
 */
class StackRendering {
    fun styles(opts: RenderOptions): String {
        //language=CSS
        return """
                html, body, p, div, table, tr, td, th { margin: 0; padding: 0; border: none; border-collapse: collapse; }
                html { padding: 1em; }
                table { width:100%; }
                th, td { text-align:left; }
                th { width: 50%; font-size:1.1em; border-bottom: 1px solid ${JBColor.black.asHexString()}; }
                td { font-family: "${opts.codeFont}", monospace; font-size: ${opts.codeFontSizePt}pt; padding: 2px 0 4px 0; }

                .error { font-weight: bold; color: ${JBColor.red.asHexString()}}
        """.trimIndent()
    }

    fun render(stack: MichelsonStackTransformation, opts: RenderOptions): String {
        val maxSize = Math.max(stack.before.size, stack.after.size)
        val firstChange = findFirstChange(maxSize, stack)
        val lastChange = findLastChange(maxSize, stack)

        val rowOffsetLeft = if (opts.alignStacks) maxSize - stack.before.size else 0
        val rowOffsetRight = if (opts.alignStacks) maxSize - stack.after.size else 0

        return StringBuilder().appendHTML().html {
            head {
                style {
                    unsafe {
                        +styles(opts)
                    }
                }
            }
            body {
                table {
                    tr {
                        th { +"Before" }
                        th { +"After" }
                    }

                    for (i in 0 until maxSize) {
                        tr(if (i % 2 == 0) "even" else "odd") {
                            if (opts.highlightChanges) {
                                //classes += if (unchanged) "no-change-row" else "change-row"
                                if (i == firstChange) classes += "first-change"
                                if (i == lastChange && i < maxSize - 1) classes += "last-change"
                            }

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

    private fun findFirstChange(maxSize: Int, stack: MichelsonStackTransformation): Int {
        for (i in 0 until maxSize) {
            if (stack.before.frames.getOrNull(i) != stack.after.frames.getOrNull(i)) {
                return i
            }
        }
        return -1
    }

    private fun findLastChange(maxSize: Int, stack: MichelsonStackTransformation): Int {
        for (i in maxSize downTo 0) {
            if (stack.before.frames.getOrNull(i) != stack.after.frames.getOrNull(i)) {
                return i
            }
        }
        return maxSize + 1
    }

    private fun TR.column(isLeft: Boolean, changed: Boolean?, frame: MichelsonStackFrame?, showAnnotations: Boolean) {
        td("change") {
            classes += if (isLeft) "left" else "right"

            attributes += ("valign" to "top")
            if (changed != null && !changed) {
                attributes += "style" to "color: ${JBColor.gray.asHexString()};"
            }

            frame?.let { +it.type.asString(showAnnotations) }
        }
    }
}

fun Color.asHexString(): String {
    return String.format("#%x%x%x", red, green, blue)
}
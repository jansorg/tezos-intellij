package com.tezos.client.stack

import kotlinx.html.*
import kotlinx.html.dom.append
import org.w3c.dom.Document
import javax.xml.parsers.DocumentBuilderFactory

@Suppress("UNUSED_PARAMETER")
/**
 * @author jansorg
 */
class InlineStackRendering {
    fun defaultStyles(): String {
        //language=CSS
        return """
                html, body, p, div, table, tr, td, th { margin: 0; padding: 0; }
                html { padding: 1em; font-family: Tahoma, sans-serif; }
                table { width:100%; border-collapse: collapse; }
                th, td { text-align:left; padding: 0.2em 0; }
                td { padding-top: 1em;  }
                th { width: 50%; border-bottom: 1px solid black; }

                .no-change { color:#808080; }
              """.trimIndent()
    }

    fun styles(opts: RenderOptions): String {
        //language=CSS
        return """
            td { font-family: "${opts.codeFont}", monospace; font-size: ${opts.codeFontSizePt}px; }
        """.trimIndent()
    }

    fun render(stack: MichelsonStackTransformation, opts: RenderOptions): Document {
        val maxSize = Math.max(stack.before.size, stack.after.size)
        val firstChange = findFirstChange(maxSize, stack)
        val lastChange = findLastChange(maxSize, stack)

        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()

        document.append.html {
            head {
                style {
                    unsafe {
                        // +opts.defaultCSS
                        +defaultStyles()
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

                            val left = stack.before.frames.getOrNull(i)
                            val right = stack.after.frames.getOrNull(i)
                            val changed = if (opts.highlightChanges) {
                                left != right
                            } else {
                                null
                            }

                            column(true, changed, left)
                            column(false, null, right)
                        }
                    }
                }
            }
        }

        return document
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

    private fun TR.column(isLeft: Boolean, changed: Boolean?, frame: MichelsonStackFrame?) {
        td("change") {
            classes += if (isLeft) "left" else "right"
            changed?.let { classes += if (changed) "change" else "no-change" }

            attributes += ("valign" to "top")
            frame?.let { +it.type.asString(false) }
        }
    }
}
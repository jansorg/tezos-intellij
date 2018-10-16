package com.tezos.client.stack

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.io.StringWriter

data class RenderOptions(val highlightChanges: Boolean = false, val codeFont: String = "monospace", val codeFontSize: String? = null)

/**
 * @author jansorg
 */
class StackRendering() {
    fun render(stack: MichelsonStackTransformation, opts: RenderOptions): String {
        val out = StringWriter()

        val maxSize = Math.max(stack.before.size, stack.after.size)

        out.appendHTML().html {
            head {
                style("text/css") {
                    //language=CSS
                    +"""
                        table { width:100%; }
                        th { width: 50%; }
                        th, td { font-size: 105%; text-align:left; }
                        td { margin-bottom: 15px; }
                        .left, .right { font-family: ${opts.codeFont};
                    """.trimIndent()
                }
            }
            body {
                table {
                    thead {
                        tr {
                            th { +"Before" }
                            th { +"After" }
                        }
                    }

                    tbody {
                        for (i in 0 until maxSize) {
                            tr(if (i % 2 == 0) "even" else "odd") {
                                td("left") {
                                    attributes += ("valign" to "top")

                                    val left = stack.before.frames.getOrNull(i)
                                    if (left != null) {
                                        +left.type.asString(false)
                                    }
                                }

                                td("right") {
                                    attributes += ("valign" to "top")

                                    val right = stack.after.frames.getOrNull(i)
                                    if (right != null) {
                                        +right.type.asString(false)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return out.toString()
    }
}
package com.tezos.client.stack

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.io.StringWriter

data class RenderOptions(val highlightChanges: Boolean = false, val codeFont: String = "monospace", val codeFontSizePt: Int = 11)

/**
 * @author jansorg
 */
class StackRendering {
    fun defaultStyles(): String {
        return """
                table { width:100%; }
                th { width: 50%; border: none; }
                th, td { font-size: 105%; text-align:left; }
                td { margin-bottom: 15px; }
              """.trimIndent()
    }

    fun render(stack: MichelsonStackTransformation, opts: RenderOptions): String {
        val out = StringWriter()

        val maxSize = Math.max(stack.before.size, stack.after.size)

        out.appendHTML().html {
            head {
                // add our dynamic rules, the custom rules are applied to the global stylesheet
                style("text/css") {
                    //language=CSS
                    +".left, .right { font-family: ${opts.codeFont}; font-size: ${opts.codeFontSizePt}pt; }"
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
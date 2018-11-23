@file:Suppress("CssInvalidPropertyValue")

package com.tezos.intellij.stackRendering

import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import com.tezos.client.stack.MichelsonStackFrame
import com.tezos.client.stack.MichelsonStackTransformation
import com.tezos.client.stack.MichelsonStackType
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.awt.Color

/**
 * @author jansorg
 */
@Suppress("CssUnusedSymbol")
class StackRendering {
    companion object {
        private fun style(selector: String, s: RenderStyle?, moreStyles: String = ""): String {
            return when (s) {
                null -> ""
                else -> {
                    val color = s.textColor?.let { "color: ${it.asHexString()}; " }
                    val fontWeight = if (s.bold) "font-weight: \"bold\"; " else ""
                    val fontStyle = if (s.italic) "font-style: \"italic\"; " else ""

                    // the left elemet is shown with a lighter color when unchanged
                    val opacityColor = s.textColor?.let { "color: ${it.opacity(60, JBColor.white).asHexString()}; " }

                    return """
                        $selector { $color $fontWeight $fontStyle $moreStyles}
                        .no-change $selector { $opacityColor }
                    """.trimIndent()
                }
            }
        }
    }

    private fun styles(opts: RenderOptions): String {
        //language=CSS
        return """
            html, body, table, tr, td, th { margin: 0; padding: 0; border: none; border-collapse: collapse; font-family: sans-serif; }
            html { padding: 1em; }
            table { width:100%; }
            th { width: 50%; font-size:100%; font-weight:bold; color:${JBColor.darkGray.asHexString()}; padding-bottom:${6.scaled()}px; }
            td { font-family:"${opts.codeFont}",monospace; font-size: ${opts.codeFontSizePt}pt; padding: ${2.scaled()}px 0 ${4.scaled()}px 0; }

            .left { text-align:left; }
            .right { text-align:right; }
            .right-block { text-align:left; }
            .content { border-bottom: ${1.scaled()}px solid ${JBColor.LIGHT_GRAY.asHexString()}; }

            .last-row { border-bottom:none; }
            .no-change { color: ${JBColor.gray.asHexString()}; }

            ${style(".style-type-name", opts.typeNameStyle)}
            ${style(".style-type-name-first", opts.typeNameStyle, "font-weight: bold;")}
            ${style(".style-annotation-type", opts.typeAnnotationStyle)}
            ${style(".style-annotation-field", opts.fieldAnnotationStyle)}
            ${style(".style-annotation-var", opts.varAnnotationStyle)}
            ${style(".style-paren", opts.parenStyle)}
        """.trimIndent()
    }

    fun render(stack: MichelsonStackTransformation, opts: RenderOptions): String {
        val maxSize = Math.max(stack.before.size, stack.after.size)
        val rowOffsetLeft = maxSize - stack.before.size
        val rowOffsetRight = maxSize - stack.after.size

        return StringBuilder().appendHTML().html {
            head { style(type = "text/css") { unsafe { +styles(opts) } } }
            body {
                table {
                    tr {
                        th(classes = "left") { +"Before" }
                        th(classes = if (opts.nestedBlocks) "right-block" else "right") { +"After" }
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

                            column(true, changed, left, opts.showColors, opts)
                            column(false, changed, right, opts.showColors, opts)
                        }
                    }
                }
            }
        }.toString()
    }

    private fun TR.column(isLeft: Boolean, changed: Boolean?, frame: MichelsonStackFrame?, colored: Boolean, opts: RenderOptions) {
        val className = when {
            isLeft -> "left"
            !isLeft && opts.nestedBlocks -> "right-block"
            else -> "right"
        }
        td(className) {
            attributes += ("valign" to "top")

            if (frame != null) {
                div(if (changed != null && !changed) "no-change" else null) {
                    stackInfo(frame.type, opts, colored, 0)
                }
            }
        }
    }
}

private fun HtmlBlockTag.stackInfo(type: MichelsonStackType, opts: RenderOptions, colored: Boolean = opts.showColors, level: Int = 0) {
    val addParens = type.name.isNotEmpty() && (level > 0 || (type.arguments.isNotEmpty() || (type.annotations.isNotEmpty() && opts.showAnnotations)))

    if (level > 0 && opts.nestedBlocks) {
        for (i in 0 until level * 3) {
            entity(Entities.nbsp)
        }
    }

    if (addParens) {
        span("style-paren") {
            +"("
        }
    }

    var addedContent = false

    if (type.name.isNotEmpty()) {
        val className = when {
            level == 0 && addParens && colored -> "style-type-name-first"
            colored -> "style-type-name"
            else -> null
        }
        span(className) { +type.name }
        addedContent = true
    }


    if (opts.showAnnotations && type.annotations.isNotEmpty()) {
        if (addedContent) {
            +" "
        }

        for ((index, annotation) in type.annotations.withIndex()) {
            span(if (colored) "style-annotation-type" else null) {
                +annotation.value
            }
            if (index < type.annotations.size - 1) {
                +" "
            }
            addedContent = true
        }
    }

    for (arg in type.arguments) {
        val lineBreak = addParens && opts.nestedBlocks && (type.arguments.size > 1 || (opts.showAnnotations && type.annotations.isNotEmpty()))
        if (lineBreak) {
            br {}
        } else if (addedContent) {
            +" "
        }
        stackInfo(arg, opts, colored, if (lineBreak) level + 1 else 0)
        addedContent = true
    }

    if (addParens) {
        span("style-paren") {
            +")"
        }
    }
}

private fun Color.asHexString(): String {
    return "#" + String.format("%02x%02x%02x", red, green, blue)
}

private fun Color.opacity(percentage: Short, bgColor: Color): Color {
    val opacity = percentage.toFloat() / 100.0
    val red = this.red * opacity + bgColor.red * (1 - opacity)
    val green = this.green * opacity + bgColor.green * (1 - opacity)
    val blue = this.blue * opacity + bgColor.blue * (1 - opacity)

    return Color(red.toInt(), green.toInt(), blue.toInt())
}

private fun Int.scaled() = JBUI.scale(this)
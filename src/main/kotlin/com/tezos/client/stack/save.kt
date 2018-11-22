//@file:Suppress("CssInvalidPropertyValue")
//
//package com.tezos.client.stack
//
//import com.intellij.openapi.editor.markup.TextAttributes
//import com.intellij.ui.JBColor
//import com.intellij.util.ui.JBUI
//import kotlinx.html.*
//import kotlinx.html.stream.appendHTML
//import java.awt.Color
//import java.awt.Font
//
//data class RenderStyle(val textColor: Color?, val bold: Boolean, val italic: Boolean) {
//    constructor(attr: TextAttributes) : this(attr.foregroundColor, attr.fontType and Font.BOLD == Font.BOLD, attr.fontType and Font.ITALIC == Font.ITALIC)
//}
//
///**
// * Options to configure the HTML rendering of a stack.
// * @param markUnchanged If true then unmodified stack frames will be grayed-out in the rendering
// * @param alignStacks If true then the two stacks will be aligned to the bottom of the stack
// * @param showAnnotations If true then annotations will be rendered for the types
// * @param codeFont The HTML font family to use for the rendering of source code
// * @param codeFontSizePt The font size to use for the rendering
// */
//data class RenderOptions(
//        val markUnchanged: Boolean = false,
//        val alignStacks: Boolean = false,
//        val showAnnotations: Boolean = false,
//        val codeFont: String = "monospace",
//        val codeFontSizePt: Double = 11.0,
//        val multilineTypes: Boolean = false,
//        val showColors: Boolean = false,
//        val typeNameStyle: RenderStyle? = null,
//        val typeAnnotationStyle: RenderStyle? = null,
//        val fieldAnnotationStyle: RenderStyle? = null,
//        val varAnnotationStyle: RenderStyle? = null
//)
//
///**
// * @author jansorg
// */
//@Suppress("CssUnusedSymbol")
//class StackRendering {
//    companion object {
//        private fun style(selector: String, s: RenderStyle?): String {
//            return when (s) {
//                null -> ""
//                else -> {
//                    val color = s.textColor?.let { "color: ${it.asHexString()}; " }
//                    val fontWeight = if (s.bold) "font-weight: bold; " else ""
//                    val fontStyle = if (s.italic) "font-style: italic; " else ""
//
//                    // the left elemet is shown with a lighter color when unchanged
//                    val opacityColor = s.textColor?.let { "color: ${it.opacity(70, JBColor.white).asHexString()}; " }
//
//                    return """
//                        $selector { $color $fontWeight $fontStyle }
//                        .left .no-change $selector { $opacityColor }
//                    """.trimIndent()
//                }
//            }
//        }
//    }
//
//    /**
//     * The HTML engine of Java is pretty bad.
//     * Styles are only inherited from block elements, i.e. nested selectors only work for block elements.
//     */
//    private fun styles(opts: RenderOptions): String {
//        //language=CSS
//        return """
//            html, body, p, div, table, tr, td, th { margin: 0; padding: 0; border: none; border-collapse: collapse; }
//            html { padding: 1em; }
//            table { width:100%; }
//            th { width: 50%; font-size:1.1em; font-weight:bold; color:${JBColor.darkGray.asHexString()}; padding-bottom:6px; }
//            td { font-family:"${opts.codeFont}", monospace; font-size: ${opts.codeFontSizePt}pt; padding: 2px 0 4px 0; }
//
//            .left { text-align:left; }
//            .right { text-align:right; }
//            .content { border-bottom:1px solid ${JBColor.lightGray.asHexString()}; }
//
//            .last-row { border-bottom:none; }
//
//            .no-change { color: ${JBColor.gray.asHexString()}; }
//
//            ${style(".style-type-name", opts.typeNameStyle)}
//            ${style(".style-annotation-type", opts.typeAnnotationStyle)}
//            ${style(".style-annotation-field", opts.fieldAnnotationStyle)}
//            ${style(".style-annotation-var", opts.varAnnotationStyle)}
//
//            .level-block .level-block {
//                margin-left: ${JBUI.scale(15)}px;
//            }
//            .right .level-block {
//                text-align:left;
//            }
//            .right .level-block .level-block {
//            }
// """.trimIndent()
//    }
//
//    fun render(stack: MichelsonStackTransformation, opts: RenderOptions): String {
//        val maxSize = Math.max(stack.before.size, stack.after.size)
//        val rowOffsetLeft = if (opts.alignStacks) maxSize - stack.before.size else 0
//        val rowOffsetRight = if (opts.alignStacks) maxSize - stack.after.size else 0
//
//        return StringBuilder().appendHTML().html {
//            head { style(type = "text/css") { unsafe { +styles(opts) } } }
//            body {
//                table {
//                    tr {
//                        th(classes = "left") { +"Before" }
//                        th(classes = "right") { +"After" }
//                    }
//
//                    for (i in 0 until maxSize) {
//                        tr("content" + if (i == maxSize - 1) " last-row" else "") {
//                            val left = if (i < rowOffsetLeft) null else stack.before.frames.getOrNull(i - rowOffsetLeft)
//                            val right = if (i < rowOffsetRight) null else stack.after.frames.getOrNull(i - rowOffsetRight)
//
//                            val changed = if (opts.markUnchanged && left != null && right != null) {
//                                !left.equals(right, opts.showAnnotations)
//                            } else {
//                                null
//                            }
//
//                            column(true, changed, left, opts)
//                            column(false, null, right, opts)
//                        }
//                    }
//                }
//            }
//        }.toString()
//    }
//
//    private fun TR.column(isLeft: Boolean, changed: Boolean?, frame: MichelsonStackFrame?, opts: RenderOptions) {
//        td(if (isLeft) "left" else "right") {
//            attributes += ("valign" to "top")
//
//            if (frame != null) {
//                div(if (changed != null && !changed) "no-change" else "") {
//                    stackInfo(frame.type, opts.showAnnotations, opts.showColors, if (opts.multilineTypes) 0 else null)
//                }
//            }
//        }
//    }
//}
//
//private fun HtmlBlockTag.stackInfo(type: MichelsonStackType, showAnnotations: Boolean = false, colored: Boolean = false, alignmentLevel: Int? = null, atEndBlock: HtmlBlockTag.() -> Any = {}) {
//    val wrap = type.arguments.isNotEmpty() && type.name.isNotEmpty()
//
//    val withPrefixSuffix = wrap || type.name.isNotEmpty() && showAnnotations && type.annotations.isNotEmpty()
//    val (prefix, suffix) = when (withPrefixSuffix) {
//        true -> arrayOf("(", ")")
//        false -> arrayOf("", "")
//    }
//
//    if (alignmentLevel != null) {
//        div("level-block") {
//            stackInfoContent(prefix, suffix, type, colored, alignmentLevel, showAnnotations)
//            this.atEndBlock()
//        }
//    } else {
//        stackInfoContent(prefix, suffix, type, colored, alignmentLevel, showAnnotations)
//        this.atEndBlock()
//    }
//}
//
//private fun HtmlBlockTag.stackInfoContent(prefix: String, suffix: String, type: MichelsonStackType, colored: Boolean, alignmentLevel: Int?, showAnnotations: Boolean) {
//    +prefix
//
//    if (type.name.isNotEmpty()) {
//        when {
//            colored -> span("style-type-name") { +type.name }
//            else -> +type.name
//        }
//    }
//
//    for (arg in type.arguments) {
//        if (alignmentLevel == null) {
//            +" "
//        }
//        stackInfo(arg, showAnnotations, colored = colored && type.name.isEmpty(), alignmentLevel = alignmentLevel?.let { it + 1 }) {
//            if (alignmentLevel != null) {
//                +suffix
//            }
//        }
//    }
//
//    if (showAnnotations && type.annotations.isNotEmpty()) {
//        for (annotation in type.annotations) {
//            +" "
//            span("style-annotation-type") {
//                +annotation.value
//            }
//        }
//    }
//
//    if (alignmentLevel == null) {
//        +suffix
//    }
//}
//
//private fun Color.asHexString(): String {
//    return "#" + String.format("%02x%02x%02x", red, green, blue)
//}
//
//private fun Color.opacity(percentage: Short, bgColor: Color): Color {
//    val opacity = percentage.toFloat() / 100.0
//    val red = this.red * opacity + bgColor.red * (1 - opacity)
//    val green = this.green * opacity + bgColor.green * (1 - opacity)
//    val blue = this.blue * opacity + bgColor.blue * (1 - opacity)
//
//    return Color(red.toInt(), green.toInt(), blue.toInt())
//}

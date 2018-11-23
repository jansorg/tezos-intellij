package com.tezos.intellij.stackRendering

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
        val showAnnotations: Boolean = false,
        val codeFont: String = "monospace",
        val codeFontSizePt: Double = 11.0,
        val nestedBlocks: Boolean = false,
        val showColors: Boolean = false,
        val typeNameStyle: RenderStyle? = null,
        val typeAnnotationStyle: RenderStyle? = null,
        val fieldAnnotationStyle: RenderStyle? = null,
        val varAnnotationStyle: RenderStyle? = null,
        val parenStyle: RenderStyle? = null
)
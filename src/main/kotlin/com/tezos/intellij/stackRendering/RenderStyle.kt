package com.tezos.intellij.stackRendering

import com.intellij.openapi.editor.markup.TextAttributes
import java.awt.Color
import java.awt.Font

data class RenderStyle(val textColor: Color?, val bold: Boolean, val italic: Boolean) {
    constructor(attr: TextAttributes) : this(attr.foregroundColor, attr.fontType and Font.BOLD == Font.BOLD, attr.fontType and Font.ITALIC == Font.ITALIC)
}
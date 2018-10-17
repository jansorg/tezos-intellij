package com.tezos.lang.michelson.editor.stack.michelson

import com.intellij.ide.ui.UISettings
import org.xhtmlrenderer.simple.XHTMLPanel
import org.xhtmlrenderer.swing.FSMouseListener
import java.awt.Graphics

/**
 * @author jansorg
 */
class CustomXHTMLPanel : XHTMLPanel() {
    init {
        mouseTrackingListeners.forEach { l -> removeMouseTrackingListener(l as FSMouseListener) }
        isOpaque = false
    }

    override fun paintComponent(g: Graphics) {
        UISettings.setupAntialiasing(g)
        super.paintComponent(g)
    }
}
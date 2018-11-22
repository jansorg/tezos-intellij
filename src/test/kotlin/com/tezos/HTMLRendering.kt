package com.tezos

import com.intellij.util.ui.JBDimension
import com.intellij.util.ui.UIUtil
import java.io.StringReader
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.JEditorPane
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.text.Document
import javax.swing.text.html.HTMLEditorKit
import kotlin.concurrent.timerTask

fun run(args: Array<String>) {
    var lastContent = readFile(args)
    val htmlPanel = JEditorPane(UIUtil.HTML_MIME, "")
    val htmlKit = UIUtil.getHTMLEditorKit()

    val doc = readDoc(htmlKit, lastContent)
    htmlPanel.editorKit = htmlKit
    htmlPanel.document = doc
    htmlPanel.caretPosition = 0
    htmlPanel.isEditable = false

    val frame = JFrame("HelloWorldSwing");
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE;
    frame.minimumSize = JBDimension(600, 600)
    frame.contentPane.add(htmlPanel)
    frame.pack()
    frame.isVisible = true

    java.util.Timer().scheduleAtFixedRate(timerTask {
        val new = readFile(args)
        if (lastContent != new) {
            lastContent = new

            htmlPanel.document = readDoc(htmlKit, lastContent)
        }
    }, 0, 500)
}

private fun readDoc(htmlKit: HTMLEditorKit, lastContent: StringReader): Document? {
    val doc = htmlKit.createDefaultDocument()
    htmlKit.read(lastContent, doc, 0)
    return doc
}

private fun readFile(args: Array<String>): StringReader {
    val file = args.getOrNull(0) ?: "/home/jansorg/test.html"
    val html = String(Files.readAllBytes(Paths.get(file)))
    val lastContent = StringReader(html)
    return lastContent
}

fun main(args: Array<String>) {
    SwingUtilities.invokeLater {
        run(args)
    }
}
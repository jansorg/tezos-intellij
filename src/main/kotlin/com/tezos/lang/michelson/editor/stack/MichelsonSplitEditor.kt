package com.tezos.lang.michelson.editor.stack

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.event.CaretAdapter
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.util.Alarm
import com.tezos.lang.michelson.editor.stack.michelson.MichelsonStackVisualizationEditor
import com.tezos.lang.michelson.editor.stack.split.SplitFileEditor
import javax.swing.JComponent

/**
 * @author jansorg
 */
class MichelsonSplitEditor(private val mainEditor: TextEditor, private val stackEditor: MichelsonStackVisualizationEditor) : SplitFileEditor<TextEditor, MichelsonStackVisualizationEditor>(mainEditor, stackEditor) {
    private companion object {
        val LOG = Logger.getInstance("#tezos.client")
    }

    private val alarm = Alarm(this)
    @Volatile
    private var caretListener: CaretListener? = null

    override fun getName(): String = "michelson.splitEditor"

    override fun getComponent(): JComponent {
        if (caretListener == null) {
            caretListener = object : CaretAdapter() {
                override fun caretPositionChanged(e: CaretEvent) {
                    // debounce
                    alarm.cancelAllRequests()
                    alarm.addRequest({
                        LOG.warn("Updating stack info for offset ${e.caret?.offset}...")
                        e.caret?.offset?.let {
                            try {
                                stackEditor.updateStack(e.editor.colorsScheme, e.editor.document.text, it)
                            } catch (e: Throwable) {
                                LOG.warn("error running tezos client", e)
                            }
                        }
                    }, 300)
                }
            }

            mainEditor.editor.caretModel.addCaretListener(caretListener!!)
        }

        return super.getComponent()
    }

    override fun dispose() {
        caretListener?.let {
            mainEditor.editor.caretModel.removeCaretListener(it)
        }
    }
}
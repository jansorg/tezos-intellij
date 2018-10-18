package com.tezos.lang.michelson.editor.stack

import com.intellij.ide.ui.UISettings
import com.intellij.ide.ui.UISettingsListener
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
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
class MichelsonSplitEditor(private val mainEditor: TextEditor, private val stackEditor: MichelsonStackVisualizationEditor) : SplitFileEditor<TextEditor, MichelsonStackVisualizationEditor>(mainEditor, stackEditor), UISettingsListener, CaretListener {
    private companion object {
        val LOG = Logger.getInstance("#tezos.client")
    }

    private val alarm = Alarm(this)

    override fun getName(): String = "michelson.splitEditor"

    override fun uiSettingsChanged(source: UISettings) {
        updateStackRendering(mainEditor.editor)
    }

    override fun getComponent(): JComponent {
        UISettings.getInstance().addUISettingsListener(this, this)
        mainEditor.editor.caretModel.addCaretListener(this)

        return super.getComponent()
    }

    private fun updateStackRendering(editor: Editor) {
        try {
            stackEditor.updateStack(editor.colorsScheme, editor.document.text, editor.caretModel.offset)
        } catch (e: Throwable) {
            LOG.warn("error running tezos client", e)
        }
    }

    override fun dispose() {
        alarm.cancelAllRequests()
        mainEditor.editor.caretModel.removeCaretListener(this)
    }

    override fun caretPositionChanged(e: CaretEvent) {
        alarm.cancelAllRequests()
        alarm.addRequest({
            LOG.warn("Updating stack info for offset ${e.caret?.offset}...")
            updateStackRendering(e.editor)
        }, 300)
    }

    override fun caretAdded(e: CaretEvent?) {
    }

    override fun caretRemoved(e: CaretEvent?) {
    }
}
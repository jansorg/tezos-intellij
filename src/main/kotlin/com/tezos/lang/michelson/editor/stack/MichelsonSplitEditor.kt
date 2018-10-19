package com.tezos.lang.michelson.editor.stack

import com.intellij.ide.ui.UISettings
import com.intellij.ide.ui.UISettingsListener
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.util.Alarm
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.lang.michelson.editor.stack.michelson.MichelsonStackVisualizationEditor
import com.tezos.lang.michelson.editor.stack.split.SplitFileEditor

/**
 * @author jansorg
 */
class MichelsonSplitEditor(private val mainEditor: TextEditor, private val stackEditor: MichelsonStackVisualizationEditor) : SplitFileEditor<TextEditor, MichelsonStackVisualizationEditor>("tezos-split-editor", mainEditor, stackEditor), UISettingsListener, CaretListener {
    private companion object {
        val LOG = Logger.getInstance("#tezos.client")
    }

    private val alarm = Alarm(this)

    init {
        UISettings.getInstance().addUISettingsListener(this, this)
        mainEditor.editor.caretModel.addCaretListener(this)

        //  ApplicationManager.getApplication().messageBus.connect(this).subscribe<Any>(MarkdownApplicationSettings.SettingsChangedListener.TOPIC, settingsChangedListener)
    }

    override fun dispose() {
        alarm.cancelAllRequests()
        mainEditor.editor.caretModel.removeCaretListener(this)
    }

    override fun getName(): String = "michelson.splitEditor"

    override fun isSecondEditorVisible(): Boolean {
        return TezosSettingService.getSettings().showStackVisualization
    }

    override fun isVerticalSplit(): Boolean {
        return TezosSettingService.getSettings().stackPanelPosition.isVerticalSplit()
    }

    override fun uiSettingsChanged(source: UISettings) {
        updateStackRendering(mainEditor.editor)
    }

    override fun caretPositionChanged(e: CaretEvent) {
        alarm.cancelAllRequests()
        alarm.addRequest({
            LOG.warn("Updating stack info for offset ${e.caret?.offset}...")
            updateStackRendering(e.editor)
        }, 200)
    }

    private fun updateStackRendering(editor: Editor) {
        try {
            stackEditor.updateStack(editor.colorsScheme, editor.document.text, editor.caretModel.offset)
        } catch (e: Throwable) {
            LOG.warn("error updating the stack visualization", e)
        }
    }

    override fun caretAdded(e: CaretEvent?) {}

    override fun caretRemoved(e: CaretEvent?) {}
}
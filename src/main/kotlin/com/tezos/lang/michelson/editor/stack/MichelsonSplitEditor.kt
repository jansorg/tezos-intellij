package com.tezos.lang.michelson.editor.stack

import com.intellij.ide.ui.UISettings
import com.intellij.ide.ui.UISettingsListener
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.ui.components.JBLabel
import com.intellij.util.Alarm
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.tezos.client.stack.RenderOptions
import com.tezos.intellij.editor.split.SplitFileEditor
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.intellij.settings.TezosSettingsListener
import com.tezos.lang.michelson.editor.stack.michelson.MichelsonStackVisualizationEditor
import java.awt.BorderLayout
import javax.swing.JPanel

/**
 * A split editor for Michelson files.
 *
 * @author jansorg
 */
class MichelsonSplitEditor(private val mainEditor: TextEditor, private val stackEditor: MichelsonStackVisualizationEditor)
    : SplitFileEditor<TextEditor, MichelsonStackVisualizationEditor>("tezos-split-editor", mainEditor, stackEditor), UISettingsListener, CaretListener, TezosSettingsListener {

    private companion object {
        const val ACTION_GROUP_ID = "tezos.editorToolbar"
        val LOG = Logger.getInstance("#tezos.client")
    }

    private val alarm = Alarm(Alarm.ThreadToUse.SWING_THREAD, this)

    var stackAlignStacks = true
    var stackHighlightUnchanged = true
    var stackShowAnnotations = false

    init {
        mainEditor.editor.caretModel.addCaretListener(this)

        // we can't use UISettings.getInstance() because it switched from Java to Kotlin (in 182.x at the latest)
        // we're using what 182.x is doing in its implementation
        // 182.x also deprecated addUISettingsListener()
        ApplicationManager.getApplication().messageBus.connect(this).subscribe(UISettingsListener.TOPIC, this)
        ApplicationManager.getApplication().messageBus.connect(this).subscribe(TezosSettingService.TOPIC, this)
    }

    override fun dispose() {
        alarm.cancelAllRequests()
        mainEditor.editor.caretModel.removeCaretListener(this)
    }

    override fun getName(): String = "michelson.splitEditor"

    override fun isSecondEditorVisible(): Boolean {
        return TezosSettingService.getSettings().isShowStackVisualization()
    }

    override fun isVerticalSplit(): Boolean {
        return TezosSettingService.getSettings().stackPanelPosition.isVerticalSplit()
    }

    fun refreshRendering() {
        updateStackRendering(mainEditor.editor)
    }

    override fun uiSettingsChanged(source: UISettings) {
        refreshRendering()
    }

    override fun defaultTezosClientChanged() {
        LOG.info("defaultTezosClientChanged()")
        stackEditor.reset()
        refreshRendering()
    }

    override fun tezosStackPositionChanged() {
        LOG.info("tezosStackPositionChanged()")
        triggerSplitOrientationChange(TezosSettingService.getSettings().stackPanelPosition.isVerticalSplit())
    }

    override fun createToolbar(): JPanel {
        val toolbar = JPanel(BorderLayout(JBUI.scale(3), 0))
        toolbar.border = JBUI.Borders.empty(0, 5, 0, 1)
        toolbar.add(JBLabel("Michelson stack", UIUtil.ComponentStyle.SMALL, UIUtil.FontColor.BRIGHTER), BorderLayout.WEST)

        val actions = findActions()
        toolbar.add(actions.component, BorderLayout.EAST)
        return toolbar
    }

    private fun findActions(): ActionToolbar {
        val mgr = ActionManager.getInstance()
        if (!mgr.isGroup(ACTION_GROUP_ID)) {
            throw IllegalStateException("$ACTION_GROUP_ID is not an action group")
        }

        val group = mgr.getAction(ACTION_GROUP_ID) as ActionGroup

        val bar = mgr.createActionToolbar(ActionPlaces.EDITOR_TOOLBAR, group, true) as ActionToolbarImpl
        bar.isOpaque = false
        bar.setTargetComponent(stackEditor.component)
        bar.border = JBUI.Borders.empty()

        return bar
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
            stackEditor.updateStack(editor.document.text, editor.caretModel.offset, renderOptions(editor.colorsScheme))
        } catch (e: Throwable) {
            LOG.warn("error updating the stack visualization", e)
        }
    }

    private fun renderOptions(settings: EditorColorsScheme): RenderOptions {
        return RenderOptions(
                markUnchanged = stackHighlightUnchanged,
                alignStacks = stackAlignStacks,
                showAnnotations = stackShowAnnotations,
                codeFont = settings.editorFontName,
                codeFontSizePt = settings.editorFontSize * 1.1
        )
    }

    override fun caretAdded(e: CaretEvent?) {}

    override fun caretRemoved(e: CaretEvent?) {}
}
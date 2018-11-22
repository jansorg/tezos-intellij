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
import com.tezos.intellij.editor.split.SplitFileEditor
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.intellij.settings.TezosSettingsListener
import com.tezos.intellij.stackRendering.RenderOptions
import com.tezos.intellij.stackRendering.RenderStyle
import com.tezos.lang.michelson.editor.highlighting.MichelsonSyntaxHighlighter
import com.tezos.lang.michelson.editor.stack.michelsonStackVisualization.MichelsonStackVisualizationEditor
import com.tezos.lang.michelson.stackInfo.MichelsonStackInfoManager
import java.awt.BorderLayout
import java.util.concurrent.TimeUnit
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
        const val UPDATE_DELAY = 200
        val LOG = Logger.getInstance("#tezos.client")
    }

    private val alarm = Alarm(Alarm.ThreadToUse.SWING_THREAD, this)

    var stackAlignStacks = true
    var stackHighlightUnchanged = true
    var stackShowAnnotations = false
    var stackColored = true
    var nestedBlocks = true

    init {
        mainEditor.editor.caretModel.addCaretListener(this)

        // we can't use UISettings.getInstance() because it switched from Java to Kotlin (in 182.x at the latest)
        // we're using what 182.x is doing in its implementation
        // 182.x also deprecated addUISettingsListener()
        val bus = ApplicationManager.getApplication().messageBus
        bus.connect(this).subscribe(UISettingsListener.TOPIC, this)
        bus.connect(this).subscribe(TezosSettingService.TOPIC, this)

        MichelsonStackInfoManager.getInstance(mainEditor.editor.project).registerFile(mainEditor.editor.document)
    }

    override fun dispose() {
        alarm.cancelAllRequests()
        mainEditor.editor.caretModel.removeCaretListener(this)
        MichelsonStackInfoManager.getInstance(mainEditor.editor.project).unregisterFile(mainEditor.editor.document)

        super.dispose()
    }

    override fun getName(): String = "michelson.splitEditor"

    override fun isSecondEditorVisible(): Boolean {
        return TezosSettingService.getSettings().isShowStackVisualization()
    }

    override fun isVerticalSplit(): Boolean {
        return TezosSettingService.getSettings().stackPanelPosition.isVerticalSplit()
    }

    override fun uiSettingsChanged(source: UISettings) {
        triggerStackUpdate(mainEditor.editor, 0)
    }

    override fun defaultTezosClientChanged() {
        LOG.info("defaultTezosClientChanged()")
        stackEditor.reset()
        triggerStackUpdate(mainEditor.editor, 0)
    }

    override fun tezosStackPositionChanged() {
        LOG.debug("tezosStackPositionChanged()")
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
        bar.layoutPolicy = ActionToolbar.NOWRAP_LAYOUT_POLICY

        return bar
    }

    override fun caretPositionChanged(e: CaretEvent) {
        triggerStackUpdate(e.editor, UPDATE_DELAY)
    }

    fun triggerStackUpdate() {
        triggerStackUpdate(mainEditor.editor, UPDATE_DELAY)
    }

    private fun triggerStackUpdate(editor: Editor, delay: Int) {
        alarm.cancelAllRequests()
        alarm.addRequest({
            val offset = editor.caretModel.offset
            LOG.warn("Updating stack info for offset $offset")

            try {
                val stack = MichelsonStackInfoManager.getInstance(editor.project).stackInfo(editor.document, 0, TimeUnit.MILLISECONDS)
                when (stack) {
                    null -> stackEditor.showError("error while retrieving stack info")
                    else -> stackEditor.updateStackInfo(stack, offset, renderOptions(editor.colorsScheme))
                }
            } catch (e: Exception) {
                stackEditor.showError(e)
            }
        }, delay)
    }

    private fun renderOptions(settings: EditorColorsScheme): RenderOptions {
        var typeNameStyle: RenderStyle? = null
        var fieldAnnotationStyle: RenderStyle? = null
        var typeAnnotationStyle: RenderStyle? = null
        var varAnnotationStyle: RenderStyle? = null
        var parenStyle: RenderStyle? = null

        if (stackColored) {
            typeNameStyle = RenderStyle(settings.getAttributes(MichelsonSyntaxHighlighter.TYPE_NAME))
            fieldAnnotationStyle = RenderStyle(settings.getAttributes(MichelsonSyntaxHighlighter.FIELD_ANNOTATION))
            typeAnnotationStyle = RenderStyle(settings.getAttributes(MichelsonSyntaxHighlighter.TYPE_ANNOTATION))
            varAnnotationStyle = RenderStyle(settings.getAttributes(MichelsonSyntaxHighlighter.VARIABLE_ANNOTATION))
            parenStyle = RenderStyle(settings.getAttributes(MichelsonSyntaxHighlighter.PAREN))
        }

        return RenderOptions(
                markUnchanged = stackHighlightUnchanged,
                alignStacks = stackAlignStacks,
                showAnnotations = stackShowAnnotations,
                codeFont = settings.editorFontName,
                codeFontSizePt = settings.editorFontSize * 1.1,
                nestedBlocks = nestedBlocks,
                showColors = stackColored,
                typeNameStyle = typeNameStyle,
                fieldAnnotationStyle = fieldAnnotationStyle,
                typeAnnotationStyle = typeAnnotationStyle,
                varAnnotationStyle = varAnnotationStyle,
                parenStyle = parenStyle
        )
    }

    override fun caretAdded(e: CaretEvent?) {}

    override fun caretRemoved(e: CaretEvent?) {}
}
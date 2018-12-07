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
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.components.JBLabel
import com.intellij.util.Alarm
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.tezos.intellij.editor.split.SplitFileEditor
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.intellij.settings.TezosSettingsListener
import com.tezos.intellij.stackRendering.RenderOptions
import com.tezos.intellij.stackRendering.RenderStyle
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.editor.highlighting.MichelsonSyntaxHighlighter
import com.tezos.lang.michelson.editor.stack.michelsonStackVisualization.MichelsonStackVisualizationEditor
import com.tezos.lang.michelson.psi.PsiBlockInstruction
import com.tezos.lang.michelson.psi.PsiInstruction
import com.tezos.lang.michelson.psi.PsiSection
import com.tezos.lang.michelson.stackInfo.MichelsonStackInfoManager
import com.tezos.lang.michelson.stackInfo.StackInfoUpdateListener
import java.awt.BorderLayout
import javax.swing.JPanel

/**
 * A split editor for Michelson files.
 *
 * @author jansorg
 */
class MichelsonSplitEditor(internal val mainEditor: TextEditor, val stackEditor: MichelsonStackVisualizationEditor)
    : SplitFileEditor<TextEditor, MichelsonStackVisualizationEditor>("tezos-split-editor", mainEditor, stackEditor), UISettingsListener, TezosSettingsListener, StackInfoUpdateListener, CaretListener {

    private companion object {
        const val ACTION_GROUP_ID = "tezos.editorToolbar"
        const val CARET_DEBOUNCE_DELAY = 175
        val LOG = Logger.getInstance("#tezos.client")!!
    }

    private val alarm = Alarm(Alarm.ThreadToUse.SWING_THREAD, this)

    var stackHighlightUnchanged = true
    var stackShowAnnotations = false
    var stackColored = true
    var nestedBlocks = true

    init {
        // we can't use UISettings.getInstance() because it switched from Java to Kotlin (in 182.x at the latest)
        // we're using what 182.x is doing in its implementation
        // 182.x also deprecated addUISettingsListener()
        val bus = ApplicationManager.getApplication().messageBus
        bus.connect(this).subscribe(UISettingsListener.TOPIC, this)
        bus.connect(this).subscribe(TezosSettingService.TOPIC, this)

        val stackInfoManager = MichelsonStackInfoManager.getInstance(mainEditor.editor.project)
        stackInfoManager.registerDocument(mainEditor.editor.document, this)
        stackInfoManager.addListener(this, this)

        mainEditor.editor.caretModel.addCaretListener(this)
    }

    override fun dispose() {
        alarm.cancelAllRequests()
        mainEditor.editor.caretModel.removeCaretListener(this)
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
        triggerStackUpdate()
    }

    /**
     * Debounce updates after caret changes.
     */
    override fun caretPositionChanged(e: CaretEvent) {
        alarm.cancelAllRequests()
        alarm.addRequest(this::triggerStackUpdate, CARET_DEBOUNCE_DELAY)
    }

    override fun defaultTezosClientChanged() {
        LOG.info("defaultTezosClientChanged()")
        triggerStackUpdate()
    }

    override fun tezosStackPositionChanged() {
        LOG.debug("tezosStackPositionChanged()")
        triggerSplitOrientationChange(TezosSettingService.getSettings().stackPanelPosition.isVerticalSplit())
    }

    override fun stackInfoUpdated(doc: Document) {
        if (doc == mainEditor.editor.document) {
            triggerStackUpdate()
        }
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

    fun triggerStackUpdate() {
        val editor = mainEditor.editor
        var offset = editor.caretModel.offset
        LOG.debug("Updating stack info for offset $offset")

        val stack = MichelsonStackInfoManager.getInstance(editor.project).stackInfo(editor.document)
        if (stack != null && stack.isStack && stack.getStackOrThrow().isOnWhitespace(offset)) {
            offset = fixOffset(offset)
        }

        when (stack) {
            null -> stackEditor.showError("Error while retrieving stack info") //fixme
            else -> stackEditor.updateStackInfo(stack, offset, renderOptions(editor.colorsScheme))
        }
    }

    /**
     * The tezos client has a few limitations. For example, the anootations of 'PAIR @a' are not included in the emacs-style map.
     * Also, we want to render the stack when the caret is on the code keyword, this isn't included in the mapping as well
     */
    private fun fixOffset(offset: Int): Int {
        // lookup with a replacement offset when available
        val psiFile = PsiDocumentManager.getInstance(mainEditor.editor.project!!).getCachedPsiFile(mainEditor.editor.document)
                ?: return offset

        var psiElement = psiFile.findElementAt(offset)
        if (psiElement is PsiWhiteSpace) {
            psiElement = psiFile.findElementAt(offset-1)
        }

        if (psiElement != null && psiElement.node.elementType == MichelsonTypes.SEMI) {
            psiElement = psiElement.prevSibling
            val instr = PsiTreeUtil.findFirstParent(psiElement, false) { it is PsiInstruction }
            if (instr != null && instr !is PsiBlockInstruction) {
                return instr.textOffset
            }
        } else if (psiElement != null && psiElement.node.elementType == MichelsonTypes.SECTION_NAME && psiElement.textMatches("code")) {
            psiElement = PsiTreeUtil.nextVisibleLeaf(psiElement)
            if (psiElement != null) {
                return psiElement.textOffset
            }
        }

        return offset
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
                showAnnotations = stackShowAnnotations,
                codeFont = settings.editorFontName,
                codeFontSizePt = settings.editorFontSize * 1.1,
                nestedBlocks = nestedBlocks,
                showColors = stackColored,
                typeNameStyle = typeNameStyle,
                fieldAnnotationStyle = fieldAnnotationStyle,
                typeAnnotationStyle = typeAnnotationStyle,
                varAnnotationStyle = varAnnotationStyle,
                parenStyle = parenStyle)
    }

    override fun caretAdded(e: CaretEvent?) {}

    override fun caretRemoved(e: CaretEvent) {}
}
package com.tezos.lang.michelson.runConfig

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.actionSystem.impl.PresentationFactory
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.JBDimension
import com.tezos.client.stack.MichelsonStackType
import com.tezos.client.stack.MichelsonStackUtils
import com.tezos.intellij.ui.Icons
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JTextField

/**
 * A Swing component to enter a Michelson value.
 * If a type specification is provided them a dropdown will be shown to let the user select one of the possible sample values.
 *
 * @author jansorg
 */
class MichelsonValueInput(private var expectedType: MichelsonStackType?) : JPanel(BorderLayout()) {
    val input = JBTextField()
    private var button: ActionButton? = null

    init {
        add(input, BorderLayout.CENTER)

        if (expectedType != null) {
            input.emptyText.text = expectedType!!.asString(true)
            setupType(expectedType!!)
        }
    }

    var text: String
        get() = input.text
        set(value) {
            input.text = value
        }

    var type: MichelsonStackType?
        get() = expectedType
        set(type) {
            this.expectedType = type
            when (type) {
                null -> removeType()
                else -> setupType(type)
            }
        }

    private fun removeType() {
        remove(button)
        button = null

        input.emptyText.clear()
    }

    private fun setupType(type: MichelsonStackType) {
        if (button != null) {
            remove(button)
        }

        val popupAction = PopupValuesAction(input, type)
        button = ActionButton(popupAction, PresentationFactory().getPresentation(popupAction), "", JBDimension(25, 20))
        button!!.maximumSize = JBDimension(30, 30)
        add(button, BorderLayout.EAST)

        input.emptyText.text = type.asString(true)
    }
}

class PopupValuesAction(private val target: JTextField, var type: MichelsonStackType) : ActionGroup("Fill with sample value", "", Icons.Tezos), DumbAware {
    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return MichelsonStackUtils.generateSampleList(type).map {
            SelectSampleValueAction(target, it)
        }.toTypedArray()
    }

    override fun actionPerformed(e: AnActionEvent) {
        val popup = JBPopupFactory.getInstance().createActionGroupPopup("Choose a value", this, e.dataContext, JBPopupFactory.ActionSelectionAid.ALPHA_NUMBERING, false)
        popup.showUnderneathOf(e.inputEvent?.component ?: target)
    }
}

class SelectSampleValueAction(private val target: JTextField, val value: String) : DumbAwareAction(value) {
    override fun actionPerformed(e: AnActionEvent?) {
        target.text = value
    }
}
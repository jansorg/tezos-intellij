package com.tezos.lang.michelson.runConfig

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.util.ui.JBDimension
import com.tezos.client.stack.MichelsonStackType
import javax.swing.JComponent

/**
 * @author jansorg
 */
class MichelsonInputDialog(project: Project, inputParameterSpec: MichelsonStackType?, parameterInput: String, inputStorageSpec: MichelsonStackType?, storageInput: String) : DialogWrapper(project, false, IdeModalityType.PROJECT) {

    private val form = MichelsonInputForm(inputParameterSpec, inputStorageSpec)
    init {
        title = "Michelson: Input Values"
        form.expectedParameterType.text = inputParameterSpec?.asString(true) ?: ""
        form.expectedStorageType.text = inputStorageSpec?.asString(true) ?: ""

        form.paramterInput.text = parameterInput
        form.storageInput.text = storageInput

        form.mainPanel.minimumSize = form.mainPanel.preferredSize

        init()
    }

    override fun getStyle(): DialogStyle {
        return DialogStyle.COMPACT
    }


    override fun getPreferredFocusedComponent(): JComponent? = form.paramterInput

    override fun postponeValidation(): Boolean = false

    override fun createCenterPanel(): JComponent? = form.mainPanel

    override fun doValidate(): ValidationInfo? {
        if (paramInputValue.isEmpty()) {
            return ValidationInfo("Parameter value must not be empty", form.paramterInput)
        }

        if (storageInputValue.isEmpty()) {
            return ValidationInfo("Storage value must not be empty", form.storageInput)
        }

        return null
    }

    var paramInputValue: String
        get() {
            return form.paramterInput.text
        }
        set(value) {
            form.paramterInput.text = value
        }

    var storageInputValue: String
        get() {
            return form.storageInput.text
        }
        set(value) {
            form.storageInput.text = value
        }
}
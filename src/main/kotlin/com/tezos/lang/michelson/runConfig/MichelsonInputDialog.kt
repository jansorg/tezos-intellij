package com.tezos.lang.michelson.runConfig

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.DocumentAdapter
import javax.swing.JComponent
import javax.swing.event.DocumentEvent

/**
 * @author jansorg
 */
class MichelsonInputDialog(project: Project, inputParameter: String, inputStorage: String) : DialogWrapper(project, false, IdeModalityType.PROJECT) {
    private val form = MichelsonInputForm()

    init {
        title = "Michelson: Input Values"
        form.paramterInput.text = inputParameter
        form.storageInput.text = inputStorage

        val dialog = this
        form.paramterInput.document.addDocumentListener(object: DocumentAdapter() {
            override fun textChanged(e: DocumentEvent?) {
                dialog.validate()
            }
        })

        init()
    }

    override fun getStyle(): DialogStyle {
        return DialogStyle.COMPACT
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return form.paramterInput
    }

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
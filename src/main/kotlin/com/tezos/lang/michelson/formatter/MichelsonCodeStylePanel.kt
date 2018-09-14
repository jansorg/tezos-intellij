package com.tezos.lang.michelson.formatter

import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.highlighter.EditorHighlighter
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.options.ConfigurationException
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.testFramework.LightVirtualFile
import com.tezos.lang.michelson.MichelsonFileType
import com.tezos.lang.michelson.MichelsonLanguage
import javax.swing.JComponent
import javax.swing.JPanel

/**
 */
class MichelsonCodeStylePanel(settings: CodeStyleSettings) : CodeStyleAbstractPanel(MichelsonLanguage, null, settings) {
    //    private val myPropertiesAlignmentCombo: JComboBox<*>? = null
//    private val myPreviewPanel: JPanel? = null
    private val myPanel: JPanel? = null

//    private val selectedAlignmentType: PropertyAlignment
//        get() = myPropertiesAlignmentCombo!!.selectedItem as PropertyAlignment

    init {
        addPanelToWatch(myPanel)
//        installPreviewPanel(myPreviewPanel!!)
//
//        // Initialize combo box with property value alignment types
//        for (alignment in PropertyAlignment.values()) {
//            myPropertiesAlignmentCombo!!.addItem(alignment)
//        }
//        myPropertiesAlignmentCombo!!.setRenderer(object : ListCellRendererWrapper<PropertyAlignment>() {
//            override fun customize(list: JList<*>, value: PropertyAlignment, index: Int, selected: Boolean, hasFocus: Boolean) {
//                setText(value.description)
//            }
//        })
//        myPropertiesAlignmentCombo.addItemListener { e ->
//            if (e.stateChange == ItemEvent.SELECTED) {
//                somethingChanged()
//            }
//        }
    }

    override fun getRightMargin(): Int {
        return 80
    }

    override fun createHighlighter(scheme: EditorColorsScheme): EditorHighlighter? {
        return EditorHighlighterFactory.getInstance().createEditorHighlighter(LightVirtualFile("a.tz"), scheme, null)
    }

    override fun getFileType(): FileType {
        return MichelsonFileType
    }

    override fun getPreviewText(): String? {
        return ALIGNMENT_SAMPLE
    }

    @Throws(ConfigurationException::class)
    override fun apply(settings: CodeStyleSettings) {
//        getCustomSettings(settings).PROPERTY_ALIGNMENT = selectedAlignmentType.id
    }

    override fun isModified(settings: CodeStyleSettings): Boolean {
//        return getCustomSettings(settings).PROPERTY_ALIGNMENT !== selectedAlignmentType.id
        return false
    }

    override fun getPanel(): JComponent? {
//        return myPanel
        return null
    }

    override fun resetImpl(settings: CodeStyleSettings) {
//        for (i in 0 until myPropertiesAlignmentCombo!!.itemCount) {
//            if ((myPropertiesAlignmentCombo.getItemAt(i) as PropertyAlignment).id == getCustomSettings(settings).PROPERTY_ALIGNMENT) {
//                myPropertiesAlignmentCombo.selectedIndex = i
//                break
//            }
//        }
    }

    private fun getCustomSettings(settings: CodeStyleSettings): MichelsonCodeStyleSettings {
        return settings.getCustomSettings(MichelsonCodeStyleSettings::class.java)
    }

    companion object {
        val ALIGNMENT_SAMPLE = "{\n" +
                "    \"foo\": {\n" +
                "        \"bar\": true,\n" +
                "        \"baz\": false\n" +
                "    },\n" +
                "    \"quux\": [\n" +
                "        1, 2.0, 3e0, 4.0e0\n" +
                "    ],\n" +
                "    \"longPropertyName\": null\n" +
                "}"
    }
}

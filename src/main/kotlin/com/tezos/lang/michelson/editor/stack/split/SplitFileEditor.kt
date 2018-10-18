// Copyright 2000-2018 JetBrains s.r.o.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.tezos.lang.michelson.editor.stack.split

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.fileEditor.*
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.ui.JBSplitter
import com.tezos.intellij.settings.TezosSettingService
import java.awt.BorderLayout
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel


/**
 * This class is based on SplitFileEditor of the Markdown plugin at https://github.com/JetBrains/intellij-plugins/tree/master/markdown/src/org/intellij/plugins/markdown/ui/split .
 */
abstract class SplitFileEditor<in E1 : FileEditor, in E2 : FileEditor>(private val mainEditor: E1, private val secondEditor: E2) : UserDataHolderBase(), FileEditor {
    private val mainComponent: JComponent
    private var horizontalSplitOption = TezosSettingService.getInstance().state.stackPanelPosition.isVerticalSplit()

    private var splitter: JBSplitter? = null

    init {
        mainComponent = createComponent()

        if (this.mainEditor is TextEditor) {
            this.mainEditor.putUserData<SplitFileEditor<*, *>>(PARENT_SPLIT_KEY, this)
        }

        if (this.secondEditor is TextEditor) {
            this.secondEditor.putUserData<SplitFileEditor<*, *>>(PARENT_SPLIT_KEY, this)
        }

//        val settingsChangedListener = object : MarkdownApplicationSettings.SettingsChangedListener() {
//            fun beforeSettingsChanged(newSettings: MarkdownApplicationSettings) {
//                val oldSplitEditorLayout = MarkdownApplicationSettings.getInstance().getMarkdownPreviewSettings().getSplitEditorLayout()
//
//                val oldVerticalSplitOption = MarkdownApplicationSettings.getInstance().getMarkdownPreviewSettings().isVerticalSplit()
//
//                ApplicationManager.getApplication().invokeLater {
//                    if (oldSplitEditorLayout == currentEditorLayout) {
//                        triggerLayoutChange(newSettings.getMarkdownPreviewSettings().getSplitEditorLayout(), false)
//                    }
//
//                    if (oldVerticalSplitOption == myVerticalSplitOption) {
//                        triggerSplitOrientationChange(newSettings.getMarkdownPreviewSettings().isVerticalSplit())
//                    }
//                }
//            }
//        }

//        ApplicationManager.getApplication().messageBus.connect(this)
//                .subscribe<Any>(MarkdownApplicationSettings.SettingsChangedListener.TOPIC, settingsChangedListener)
    }

/*
    private fun triggerSplitOrientationChange(isVerticalSplit: Boolean) {
        if (myVerticalSplitOption == isVerticalSplit) {
            return
        }

        myVerticalSplitOption = isVerticalSplit

        myToolbarWrapper!!.refresh()
        splitter!!.orientation = !myVerticalSplitOption
        myComponent.repaint()
    }
*/

    private fun createComponent(): JComponent {
        val newSplitter = JBSplitter(!TezosSettingService.getSettings().stackPanelPosition.isVerticalSplit(), 0.65f, 0.15f, 0.85f)
        newSplitter.splitterProportionKey = PROPORTION_KEY
        newSplitter.firstComponent = mainEditor.component
        newSplitter.secondComponent = secondEditor.component
        this.splitter = newSplitter

//        myToolbarWrapper = SplitEditorToolbar(splitter)
//        if (mainEditor is TextEditor) {
//            myToolbarWrapper!!.addGutterToTrack((mainEditor as TextEditor).editor.gutter as EditorGutterComponentEx)
//        }
//        if (secondEditor is TextEditor) {
//            myToolbarWrapper!!.addGutterToTrack((secondEditor as TextEditor).editor.gutter as EditorGutterComponentEx)
//        }

        val panel = JPanel(BorderLayout())
//        result.add(myToolbarWrapper, BorderLayout.NORTH)
        panel.add(newSplitter, BorderLayout.CENTER)
        adjustEditorsVisibility()


        return panel
    }

//    fun getCurrentEditorLayout(): SplitEditorLayout {
//        return mySplitEditorLayout
//    }

//    fun triggerLayoutChange() {
//        val oldValue = currentEditorLayout.ordinal
//        val N = SplitEditorLayout.values().size
//        val newValue = (oldValue + N - 1) % N
//
//        triggerLayoutChange(SplitEditorLayout.values()[newValue], true)
//    }

//    fun triggerLayoutChange(newLayout: SplitEditorLayout, requestFocus: Boolean) {
//        if (currentEditorLayout == newLayout) {
//            return
//        }
//
//        currentEditorLayout = newLayout
//        invalidateLayout(requestFocus)
//    }

    private fun invalidateLayout(requestFocus: Boolean) {
        adjustEditorsVisibility()
//        myToolbarWrapper!!.refresh()
        mainComponent.repaint()

        if (!requestFocus) return

        val focusComponent = preferredFocusedComponent
        if (focusComponent != null) {
            IdeFocusManager.findInstanceByComponent(focusComponent).requestFocus(focusComponent, true)
        }
    }

    private fun adjustEditorsVisibility() {
//        mainEditor.component.isVisible = currentEditorLayout.showFirst
        secondEditor.component.isVisible = TezosSettingService.getSettings().showStackVisualization
    }

    override fun getComponent(): JComponent {
        return mainComponent
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return mainEditor.preferredFocusedComponent
    }

    override fun getState(level: FileEditorStateLevel): FileEditorState {
        return SplitFileEditorState("tezos-split-editor", mainEditor.getState(level), secondEditor.getState(level))
    }

    override fun setState(state: FileEditorState) {
        if (state is SplitFileEditorState) {
            if (state.firstState != null) {
                mainEditor.setState(state.firstState)
            }
            if (state.secondState != null) {
                secondEditor.setState(state.secondState)
            }
            if (state.splitLayout != null) {
//                currentEditorLayout = SplitEditorLayout.valueOf(state.splitLayout)
                invalidateLayout(true)
            }
        }
    }

    override fun isModified(): Boolean {
        return mainEditor.isModified || secondEditor.isModified
    }

    override fun isValid(): Boolean {
        return mainEditor.isValid && secondEditor.isValid
    }

    override fun selectNotify() {
        mainEditor.selectNotify()
        secondEditor.selectNotify()
    }

    override fun deselectNotify() {
        mainEditor.deselectNotify()
        secondEditor.deselectNotify()
    }

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
        mainEditor.addPropertyChangeListener(listener)
        secondEditor.addPropertyChangeListener(listener)

//        val delegate = myListenersGenerator.addListenerAndGetDelegate(listener)
//        mainEditor.addPropertyChangeListener(delegate)
//        secondEditor.addPropertyChangeListener(delegate)
    }

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
        mainEditor.removePropertyChangeListener(listener)
        secondEditor.removePropertyChangeListener(listener)

//        val delegate = myListenersGenerator.removeListenerAndGetDelegate(listener)
//        if (delegate != null) {
//            mainEditor.removePropertyChangeListener(delegate)
//            secondEditor.removePropertyChangeListener(delegate)
//        }
    }

    override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? {
        return mainEditor.backgroundHighlighter
    }

    override fun getCurrentLocation(): FileEditorLocation? {
        return mainEditor.currentLocation
    }

    override fun getStructureViewBuilder(): StructureViewBuilder? {
        return mainEditor.structureViewBuilder
    }

    override fun dispose() {
        Disposer.dispose(mainEditor)
        Disposer.dispose(secondEditor)
    }

    class SplitFileEditorState(val splitLayout: String?, val firstState: FileEditorState?, val secondState: FileEditorState?) : FileEditorState {
        override fun canBeMergedWith(otherState: FileEditorState, level: FileEditorStateLevel): Boolean {
            return (otherState is SplitFileEditorState
                    && (firstState == null || firstState.canBeMergedWith(otherState.firstState, level))
                    && (secondState == null || secondState.canBeMergedWith(otherState.secondState, level)))
        }
    }

/*
    private inner class DoublingEventListenerDelegate private constructor(private val myDelegate: PropertyChangeListener) : PropertyChangeListener {

        override fun propertyChange(evt: PropertyChangeEvent) {
            myDelegate.propertyChange(PropertyChangeEvent(this@SplitFileEditor, evt.propertyName, evt.oldValue, evt.newValue))
        }
    }
*/

/*
    private inner class MyListenersMultimap {
        private val myMap = HashMap<PropertyChangeListener, Pair<Int, DoublingEventListenerDelegate>>()

        fun addListenerAndGetDelegate(listener: PropertyChangeListener): DoublingEventListenerDelegate {
            if (!myMap.containsKey(listener)) {
                myMap[listener] = Pair.create(1, DoublingEventListenerDelegate(listener))
            } else {
                val oldPair = myMap[listener]
                myMap[listener] = Pair.create(oldPair.getFirst() + 1, oldPair.getSecond())
            }

            return myMap[listener].getSecond()
        }

        fun removeListenerAndGetDelegate(listener: PropertyChangeListener): DoublingEventListenerDelegate? {
            val oldPair = myMap[listener] ?: return null

            if (oldPair.getFirst() == 1) {
                myMap.remove(listener)
            } else {
                myMap[listener] = Pair.create(oldPair.getFirst() - 1, oldPair.getSecond())
            }
            return oldPair.getSecond()
        }
    }
*/

/*
    enum class SplitEditorLayout private constructor(val showFirst: Boolean, val showSecond: Boolean, val presentationName: String) {
        FIRST(true, false, MarkdownBundle.message("markdown.layout.editor.only")),
        SECOND(false, true, MarkdownBundle.message("markdown.layout.preview.only")),
        SPLIT(true, true, MarkdownBundle.message("markdown.layout.editor.and.preview"));

        val presentationText: String
            get() = StringUtil.capitalize(StringUtil.substringAfter(presentationName, "Show ")!!)

        override fun toString(): String {
            return presentationName
        }
    }
*/

    companion object {
        val PARENT_SPLIT_KEY: Key<SplitFileEditor<*, *>> = Key.create("parentSplit")

        private const val PROPORTION_KEY = "TezosSplitFileEditor.Proportion"
    }
}
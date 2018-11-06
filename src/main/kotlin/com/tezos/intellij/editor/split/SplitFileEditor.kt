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
package com.tezos.intellij.editor.split

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.fileEditor.*
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.ui.JBSplitter
import java.awt.BorderLayout
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * This class is based on SplitFileEditor of the Markdown plugin at https://github.com/JetBrains/intellij-plugins/tree/master/markdown/src/org/intellij/plugins/markdown/ui/split .
 */
abstract class SplitFileEditor<in E1 : FileEditor, in E2 : FileEditor>(private val id: String, private val mainEditor: E1, private val secondEditor: E2) : UserDataHolderBase(), FileEditor {
    companion object {
        val PARENT_SPLIT_KEY: Key<SplitFileEditor<*, *>> = Key.create("parentSplit")

        private const val PROPORTION_KEY = "TezosSplitFileEditor.Proportion"
    }

    private lateinit var mainComponent: JComponent
    private lateinit var splitter: JBSplitter
    private var toolbar: JPanel? = null

    init {
        if (this.mainEditor is TextEditor) {
            @Suppress("LeakingThis")
            this.mainEditor.putUserData<SplitFileEditor<*, *>>(PARENT_SPLIT_KEY, this)
        }

        if (this.secondEditor is TextEditor) {
            @Suppress("LeakingThis")
            this.secondEditor.putUserData<SplitFileEditor<*, *>>(PARENT_SPLIT_KEY, this)
        }
    }

    protected open fun isFirstEditorVisible() = true
    protected open fun isSecondEditorVisible() = true
    protected open fun isVerticalSplit() = true

    protected fun triggerSplitOrientationChange(isVerticalSplit: Boolean) {
        if (splitter.orientation == isVerticalSplit) {
            return
        }

        // toolbar.refresh()
        splitter.orientation = isVerticalSplit
        mainComponent.repaint()
    }

    protected open fun createToolbar(): JPanel? = null

    private fun createComponent(): JComponent {
        toolbar = createToolbar()
        val second = JPanel(BorderLayout())
        second.add(toolbar, BorderLayout.NORTH)
        second.add(secondEditor.component, BorderLayout.CENTER)

        splitter = JBSplitter(isVerticalSplit(), 0.65f, 0.15f, 0.85f)
        splitter.splitterProportionKey = PROPORTION_KEY
        splitter.firstComponent = mainEditor.component
        splitter.secondComponent = second

        val mainPanel = JPanel(BorderLayout())
        mainPanel.add(splitter, BorderLayout.CENTER)
        adjustEditorsVisibility()
        return mainPanel
    }

    private fun invalidateLayout(requestFocus: Boolean) {
        adjustEditorsVisibility()
        toolbar?.repaint()
        mainComponent.repaint()

        if (!requestFocus) {
            return
        }

        val focusComponent = preferredFocusedComponent
        if (focusComponent != null) {
            IdeFocusManager.findInstanceByComponent(focusComponent).requestFocus(focusComponent, true)
        }
    }

    private fun adjustEditorsVisibility() {
        mainEditor.component.isVisible = isFirstEditorVisible()
        secondEditor.component.isVisible = isSecondEditorVisible()
    }

    override fun getComponent(): JComponent {
        if (!this::mainComponent.isInitialized) {
            mainComponent = createComponent()
        }

        return mainComponent
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return mainEditor.preferredFocusedComponent
    }

    override fun getState(level: FileEditorStateLevel): FileEditorState {
        return SplitFileEditorState(id, mainEditor.getState(level), secondEditor.getState(level))
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
    }

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
        mainEditor.removePropertyChangeListener(listener)
        secondEditor.removePropertyChangeListener(listener)
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

    class SplitFileEditorState(@JvmField val splitLayout: String?, @JvmField val firstState: FileEditorState?, @JvmField val secondState: FileEditorState?) : FileEditorState {
        override fun canBeMergedWith(otherState: FileEditorState, level: FileEditorStateLevel): Boolean {
            return (otherState is SplitFileEditorState
                    && (firstState == null || firstState.canBeMergedWith(otherState.firstState, level))
                    && (secondState == null || secondState.canBeMergedWith(otherState.secondState, level)))
        }
    }
}
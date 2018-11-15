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

import com.intellij.openapi.fileEditor.*
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.jdom.Element

/**
 * This class is based on SplitFileEditorProvider of the Markdown plugin at https://github.com/JetBrains/intellij-plugins/tree/master/markdown/src/org/intellij/plugins/markdown/ui/split .
 */
abstract class SplitTextEditorProvider(protected val myFirstProvider: FileEditorProvider, protected val mySecondProvider: FileEditorProvider) : AsyncFileEditorProvider, DumbAware {
    private val myEditorTypeId: String = "split-provider[" + myFirstProvider.editorTypeId + ";" + mySecondProvider.editorTypeId + "]"

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return myFirstProvider.accept(project, file) && mySecondProvider.accept(project, file)
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        return createEditorAsync(project, file).build()
    }

    override fun getEditorTypeId(): String = myEditorTypeId

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        val firstBuilder = getBuilderFromEditorProvider(myFirstProvider, project, file)
        val secondBuilder = getBuilderFromEditorProvider(mySecondProvider, project, file)

        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor {
                return createSplitEditor(firstBuilder.build(), secondBuilder.build())
            }
        }
    }

    override fun readState(sourceElement: Element, project: Project, file: VirtualFile): FileEditorState {
        var child: Element? = sourceElement.getChild(FIRST_EDITOR)
        var firstState: FileEditorState? = null
        if (child != null) {
            firstState = myFirstProvider.readState(child, project, file)
        }
        child = sourceElement.getChild(SECOND_EDITOR)
        var secondState: FileEditorState? = null
        if (child != null) {
            secondState = mySecondProvider.readState(child, project, file)
        }

        val attribute = sourceElement.getAttribute(SPLIT_LAYOUT)

        val layoutName: String?
        if (attribute != null) {
            layoutName = attribute.value
        } else {
            layoutName = null
        }

        return SplitFileEditor.SplitFileEditorState(layoutName, firstState, secondState)
    }

    override fun writeState(state: FileEditorState, project: Project, targetElement: Element) {
        if (state !is SplitFileEditor.SplitFileEditorState) {
            return
        }

        var child = Element(FIRST_EDITOR)
        if (state.firstState != null) {
            myFirstProvider.writeState(state.firstState, project, child)
            targetElement.addContent(child)
        }

        child = Element(SECOND_EDITOR)
        if (state.secondState != null) {
            mySecondProvider.writeState(state.secondState, project, child)
            targetElement.addContent(child)
        }

        if (state.splitLayout != null) {
            targetElement.setAttribute(SPLIT_LAYOUT, state.splitLayout)
        }
    }

    protected abstract fun createSplitEditor(firstEditor: FileEditor, secondEditor: FileEditor): FileEditor

    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR
    }

    companion object {
        private const val FIRST_EDITOR = "first_editor"
        private const val SECOND_EDITOR = "second_editor"
        private const val SPLIT_LAYOUT = "split_layout"

        fun getBuilderFromEditorProvider(provider: FileEditorProvider, project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
            return when (provider) {
                is AsyncFileEditorProvider -> provider.createEditorAsync(project, file)
                else -> object : AsyncFileEditorProvider.Builder() {
                    override fun build(): FileEditor {
                        return provider.createEditor(project, file)
                    }
                }
            }
        }
    }
}
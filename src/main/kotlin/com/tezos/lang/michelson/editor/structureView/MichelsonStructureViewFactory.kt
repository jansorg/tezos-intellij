package com.tezos.lang.michelson.editor.structureView

import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.tezos.lang.michelson.psi.MichelsonPsiFile

/**
 * @author jansorg
 */
class MichelsonStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder? {
        return MichelsonStructureViewBuilder(psiFile)
    }
}

private class MichelsonStructureViewBuilder(val psiFile: PsiFile) : TreeBasedStructureViewBuilder() {
    override fun isRootNodeShown(): Boolean = false

    override fun createStructureViewModel(editor: Editor?): StructureViewModel {
        return MichelsonStructureViewModel(psiFile as MichelsonPsiFile, editor)
    }
}
package com.tezos.lang.michelson.structureView

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.openapi.editor.Editor
import com.tezos.lang.michelson.psi.MichelsonPsiFile
import com.tezos.lang.michelson.psi.PsiSection
import com.tezos.lang.michelson.psi.PsiSectionType

class MichelsonStructureViewModel(psiFile: MichelsonPsiFile, editor: Editor?) : StructureViewModelBase(psiFile, editor, MichelsonFileViewElement(psiFile)), StructureViewModel.ElementInfoProvider {
    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean {
        return false
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        return element is PsiSection && element.sectionType != PsiSectionType.CODE
    }
}

package com.tezos.lang.michelson.editor.structureView

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.tezos.lang.michelson.psi.PsiContract
import com.tezos.lang.michelson.ui.Icons
import javax.swing.Icon

internal class MichelsonContractViewElement(private val contract: PsiContract) : PsiTreeElementBase<PsiContract>(contract) {
    override fun getPresentableText(): String? = "Contract"

    override fun getIcon(open: Boolean): Icon? = Icons.Tezos

    override fun getChildrenBase(): MutableCollection<StructureViewTreeElement> {
        return contract.sections.asSequence().map { MichelsonSectionViewElement(it) }.toMutableList()
    }
}

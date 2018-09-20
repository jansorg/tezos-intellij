package com.tezos.lang.michelson.structureView

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.tezos.lang.michelson.psi.PsiSection
import java.util.*

class MichelsonSectionViewElement(private val section: PsiSection) : PsiTreeElementBase<PsiSection>(section) {
    override fun getPresentableText(): String? = "Section " + section.sectionType.name.toLowerCase()

    override fun getChildrenBase(): MutableCollection<StructureViewTreeElement> {
        return Collections.emptyList()
    }
}

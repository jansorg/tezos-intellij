package com.tezos.lang.michelson.editor.structureView

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.psi.util.PsiTreeUtil
import com.tezos.lang.michelson.psi.PsiCreateContractInstruction
import com.tezos.lang.michelson.psi.PsiSection

internal class MichelsonSectionViewElement(private val section: PsiSection) : PsiTreeElementBase<PsiSection>(section) {
    override fun getPresentableText(): String? = "Section " + section.sectionType.name.toLowerCase()

    override fun getChildrenBase(): MutableCollection<StructureViewTreeElement> {
        val elements = PsiTreeUtil.collectElements(section) { child ->
            when (child is PsiCreateContractInstruction) {
                true -> {
                    val childSection = PsiTreeUtil.findFirstParent(child, true) { it is PsiSection }
                    section.isEquivalentTo(childSection)
                }
                false -> false
            }
        }

        return elements
                .map { MichelsonCreateContractViewElement(it as PsiCreateContractInstruction) }
                .toMutableList()
    }
}

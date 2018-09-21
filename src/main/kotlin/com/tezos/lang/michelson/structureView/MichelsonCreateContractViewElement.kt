package com.tezos.lang.michelson.structureView

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.tezos.lang.michelson.psi.PsiCreateContractInstruction
import java.util.*

internal class MichelsonCreateContractViewElement(private val instruction: PsiCreateContractInstruction) : PsiTreeElementBase<PsiCreateContractInstruction>(instruction) {
    override fun getPresentableText(): String = "Create contract"

    override fun getChildrenBase(): MutableCollection<StructureViewTreeElement> {
        val contract = instruction.contractWrapper?.contract
        return when (contract) {
            null -> Collections.emptyList()
            else -> mutableListOf(MichelsonContractViewElement(contract))
        }
    }
}

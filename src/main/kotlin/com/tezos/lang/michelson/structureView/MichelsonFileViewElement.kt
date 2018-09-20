package com.tezos.lang.michelson.structureView

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.tezos.lang.michelson.psi.MichelsonPsiFile
import java.util.*

internal class MichelsonFileViewElement(private val file: MichelsonPsiFile) : PsiTreeElementBase<MichelsonPsiFile>(file) {
    override fun getPresentableText(): String? = "file"

    override fun getChildrenBase(): MutableCollection<StructureViewTreeElement> {
        val contract = file.getContract()
        return when (contract) {
            null -> Collections.emptyList()
            else -> mutableListOf(MichelsonContractViewElement(contract))
        }
    }
}

package com.tezos.lang.michelson.structureView

import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.openapi.editor.Editor
import com.tezos.lang.michelson.psi.MichelsonPsiFile

class MichelsonStructureViewModel(psiFile: MichelsonPsiFile, editor: Editor?) : StructureViewModelBase(psiFile, editor, MichelsonFileViewElement(psiFile))

package com.tezos.lang.michelson.psi.impl

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.tezos.lang.michelson.MichelsonFileType
import com.tezos.lang.michelson.MichelsonLanguage
import com.tezos.lang.michelson.psi.MichelsonPsiFile
import com.tezos.lang.michelson.psi.PsiContract

/**
 * @author jansorg
 */
class MichelsonPsiFileImpl(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, MichelsonLanguage), MichelsonPsiFile {
    override fun getFileType(): FileType = MichelsonFileType

    override fun getContract(): PsiContract? = findChildByClass(PsiContract::class.java)
}
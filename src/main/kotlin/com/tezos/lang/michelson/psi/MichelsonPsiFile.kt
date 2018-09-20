package com.tezos.lang.michelson.psi

import com.intellij.psi.PsiFile

/** The base of a Michelson PsiFile.
 * @author jansorg
 */
interface MichelsonPsiFile : PsiFile {
    fun getContract(): PsiContract?
}
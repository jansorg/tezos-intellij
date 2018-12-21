package com.tezos.lang.michelson.editor.liveTemplate

import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtil
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.psi.PsiCodeSection
import com.tezos.lang.michelson.psi.PsiTypeSection

/**
 * @author jansorg
 */
class MichelsonToplevelContext : TemplateContextType("MICHELSON_TOPLEVEL", "Michelson toplevel", MichelsonContext::class.java) {
    override fun isInContext(file: PsiFile, offset: Int): Boolean {
        if (PsiUtil.getLanguageAtOffset(file, offset) != MichelsonLanguage) {
            return false
        }

        // show templates which are not in any of the well known sections parameter, storage, or code.
        // this accepts unknown sections which accept errors
        val psi = file.findElementAt(offset)
        return PsiTreeUtil.findFirstParent(psi) { it is PsiCodeSection || it is PsiTypeSection } == null
    }
}

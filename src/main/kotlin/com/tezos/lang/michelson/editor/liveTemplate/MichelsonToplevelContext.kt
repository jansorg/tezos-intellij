package com.tezos.lang.michelson.editor.liveTemplate

import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiUtil
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.psi.MichelsonPsiFile

/**
 * @author jansorg
 */
class MichelsonToplevelContext : TemplateContextType("MICHELSON_TOPLEVEL", "Michelson toplevel", MichelsonContext::class.java) {
    override fun isInContext(file: PsiFile, offset: Int): Boolean {
        if (PsiUtil.getLanguageAtOffset(file, offset) != MichelsonLanguage) {
            return false
        }

        val psi = file.findElementAt(offset)
        return psi == null || psi.parent is MichelsonPsiFile
    }
}

package com.tezos.lang.michelson.editor.liveTemplate

import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiUtil
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.psi.PsiBlockInstruction

/**
 * @author jansorg
 */
class MichelsonCodeContext : TemplateContextType("MICHELSON_CODE", "Michelson code block", MichelsonContext::class.java) {
    override fun isInContext(file: PsiFile, offset: Int): Boolean {
        if (PsiUtil.getLanguageAtOffset(file, offset) != MichelsonLanguage) {
            return false
        }

        val psi = file.findElementAt(offset)
        return psi is PsiWhiteSpace && psi.parent is PsiBlockInstruction
    }
}

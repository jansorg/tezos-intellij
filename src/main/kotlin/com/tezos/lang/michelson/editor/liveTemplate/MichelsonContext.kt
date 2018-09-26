package com.tezos.lang.michelson.editor.liveTemplate

import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiUtil
import com.tezos.lang.michelson.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonContext : TemplateContextType("MICHELSON", "Michelson") {
    override fun isInContext(file: PsiFile, offset: Int): Boolean {
        return PsiUtil.getLanguageAtOffset(file, offset) == MichelsonLanguage
    }
}

package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.patterns.PlatformPatterns
import com.tezos.lang.michelson.psi.MichelsonPsiFile

/**
 * @author jansorg
 */
class MichelsonFileCompletionContributor : CompletionContributor() {
    companion object {
        // dummy text IntelliJ -> Error element -> File
        private val TOPLEVEL_PATTERN = PlatformPatterns
                .psiElement()
                .withSuperParent(2, MichelsonPsiFile::class.java)
    }

    init {
        extend(null, TOPLEVEL_PATTERN, MichelsonSectionCompletion())
    }
}


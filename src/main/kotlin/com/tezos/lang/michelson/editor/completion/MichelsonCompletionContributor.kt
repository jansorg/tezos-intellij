package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.patterns.PlatformPatterns
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.psi.MichelsonPsiFile
import com.tezos.lang.michelson.psi.PsiBlockInstruction

/**
 * @author jansorg
 */
class MichelsonCompletionContributor : CompletionContributor() {
    companion object {
        // dummy text IntelliJ -> Error element -> File
        private val TOPLEVEL_PATTERN = PlatformPatterns
                .psiElement()
                .withSuperParent(2, MichelsonPsiFile::class.java)

        // the suffic completion of an instruction token or a top-level instruction in a block
        private val INSTRUCTION_PATTERN = PlatformPatterns.or(
                PlatformPatterns.psiElement(MichelsonTypes.INSTRUCTION_TOKEN),
                PlatformPatterns.psiElement().withParent(PsiBlockInstruction::class.java))
    }

    init {

        extend(null, TOPLEVEL_PATTERN, MichelsonSectionCompletion())

        extend(null, INSTRUCTION_PATTERN, MichelsonInstructionNameCompletion())
    }
}


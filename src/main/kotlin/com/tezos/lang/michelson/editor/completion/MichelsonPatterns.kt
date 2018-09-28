package com.tezos.lang.michelson.editor.completion

import com.intellij.patterns.PlatformPatterns
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.psi.MichelsonPsiFile
import com.tezos.lang.michelson.psi.PsiBlockInstruction

val PATTERN_IN_MICHELSON_FILE = PlatformPatterns.psiElement().inside(PlatformPatterns.psiFile(MichelsonPsiFile::class.java))!!

val PATTERN_INSTRUCTION_TOKEN = PlatformPatterns.psiElement(MichelsonTypes.INSTRUCTION_TOKEN)!!

val PATTERN_WITH_BLOCK_PARENT = PlatformPatterns.psiElement().withParent(PlatformPatterns.psiElement(PsiBlockInstruction::class.java))!!

// dummy text IntelliJ -> Error element -> File
val TOPLEVEL_PATTERN = PlatformPatterns
        .psiElement()
        .withSuperParent(2, MichelsonPsiFile::class.java)!!

// the suffix completion of an instruction token or a top-level instruction in a block
val INSTRUCTION_PATTERN = PlatformPatterns.or(PATTERN_INSTRUCTION_TOKEN, PATTERN_WITH_BLOCK_PARENT)!!

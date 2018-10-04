package com.tezos.lang.michelson.editor.completion

import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.util.ProcessingContext
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.psi.MichelsonPsiFile
import com.tezos.lang.michelson.psi.PsiBlockInstruction
import com.tezos.lang.michelson.psi.PsiInstruction

fun IElementType.toPsiPattern() = PlatformPatterns.psiElement().withElementType(this)
fun TokenSet.toPsiPattern() = PlatformPatterns.psiElement().withElementType(this)

val LEFT_PAREN_PATTERN = MichelsonTypes.LEFT_PAREN.toPsiPattern()
val PATTERN_LEFT_CURLY = MichelsonTypes.LEFT_CURLY.toPsiPattern()
val PATTERN_SEMI = MichelsonTypes.SEMI.toPsiPattern()
val PATTERN_INSTRUCTION_TOKEN = MichelsonTypes.INSTRUCTION_TOKEN.toPsiPattern()

val PATTERN_DATA_ELEMENT = MichelsonTypes.LITERAL_DATA.toPsiPattern()
val INSTRUCTION_ELEMENT_PATTERN = PlatformPatterns.psiElement(PsiInstruction::class.java).andNot(PlatformPatterns.psiElement(PsiBlockInstruction::class.java))

val PATTERN_INSIDE_DATA_ELEMENT = PlatformPatterns.psiElement().withParent(PATTERN_DATA_ELEMENT)!!

// dummy text IntelliJ -> Error element -> File
val TOPLEVEL_PATTERN = PlatformPatterns
        .psiElement()
        .withSuperParent(2, MichelsonPsiFile::class.java)!!
val WHITESPACE_PATTERN = PlatformPatterns.psiElement(TokenType.WHITE_SPACE)!!

val DEBUG = PlatformPatterns.psiElement().with(object : PatternCondition<PsiElement?>("debug") {
    override fun accepts(t: PsiElement, context: ProcessingContext): Boolean {
        return true
    }
})


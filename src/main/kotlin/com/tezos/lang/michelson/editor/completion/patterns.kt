package com.tezos.lang.michelson.editor.completion

import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.psi.MichelsonPsiFile
import com.tezos.lang.michelson.psi.PsiBlockInstruction
import com.tezos.lang.michelson.psi.PsiInstruction

fun IElementType.toPsiPattern() = PlatformPatterns.psiElement().withElementType(this)
fun TokenSet.toPsiPattern() = PlatformPatterns.psiElement().withElementType(this)

val DEBUG_FALSE = PlatformPatterns.psiElement().with(object : PatternCondition<PsiElement?>("debug") {
    override fun accepts(t: PsiElement, context: ProcessingContext): Boolean {
        return false
    }
})

val DEBUG_TRUE = PlatformPatterns.psiElement().with(object : PatternCondition<PsiElement?>("debug") {
    override fun accepts(t: PsiElement, context: ProcessingContext): Boolean {
        return true
    }
})

// we can't use PlatformPatterns.psiElement().afterLeakSkipping() because it always skips empty-elements, i.e. error elements
val AFTER_ERROR_LEAF_SKIPPING_WS = PlatformPatterns.psiElement().with(object : PatternCondition<PsiElement?>("after-error-skipping-ws") {
    override fun accepts(t: PsiElement, context: ProcessingContext): Boolean {
        var element: PsiElement? = t
        while (true) {
            element = PsiTreeUtil.prevLeaf(element!!)
            if (element == null) {
                return false
            }

            if (element is PsiErrorElement) {
                return true
            }

            if (element.textLength == 0) {
                continue
            }

            if (element.node?.elementType != TokenType.WHITE_SPACE) {
                return false
            }
        }
    }
})!!

val LEFT_PAREN_PATTERN = MichelsonTypes.LEFT_PAREN.toPsiPattern()
val PATTERN_TAG_TOKEN = MichelsonTypes.TAG_TOKEN.toPsiPattern()

val PATTERN_INSTRUCTION_TOKEN = MichelsonTypes.INSTRUCTION_TOKEN.toPsiPattern()
val INSTRUCTION_ELEMENT_PATTERN = PlatformPatterns.psiElement(PsiInstruction::class.java).andNot(PlatformPatterns.psiElement(PsiBlockInstruction::class.java))

val PATTERN_INSTRUCTION_OR_DATA_TOKEN = TokenSet.create(MichelsonTypes.INSTRUCTION_TOKEN, MichelsonTypes.TAG_TOKEN).toPsiPattern()

val PATTERN_DATA_ELEMENT = MichelsonTypes.LITERAL_DATA.toPsiPattern()
val PATTERN_INSIDE_DATA_ELEMENT = PlatformPatterns.psiElement().withParent(PATTERN_DATA_ELEMENT)!!

// dummy text IntelliJ -> Error element -> File
val TOPLEVEL_PATTERN = PlatformPatterns
        .psiElement()
        .withSuperParent(2, MichelsonPsiFile::class.java)!!

val WHITESPACE_PATTERN = PlatformPatterns.psiElement(TokenType.WHITE_SPACE)!!


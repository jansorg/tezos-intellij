package com.tezos.lang.michelson

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.tezos.lang.michelson.psi.MichelsonPsiFile

/**
 * @author jansorg
 */
abstract class MichelsonFixtureTest : LightPlatformCodeInsightFixtureTestCase() {
    override fun getTestDataPath(): String = MichelsonTestUtils.dataPath().toString()

    fun configureByCode(code: String): Pair<MichelsonPsiFile, PsiElement?> {
        val file = myFixture.configureByText("file.tz", """
            parameter unit;
            storage unit;
            code { $code }
        """.trimIndent())

        return Pair(file as MichelsonPsiFile, getPsiAtCaret())
    }

    fun configureByCode(code: () -> String): Pair<MichelsonPsiFile, PsiElement?> {
        return configureByCode(code())
    }

    fun getPsiAtCaret(): PsiElement? {
        val offset = myFixture.caretOffset

        var psi = myFixture.file.findElementAt(offset)
        if (psi is PsiWhiteSpace) {
            psi = myFixture.file.findElementAt(offset - 1)
        }
        if (psi is PsiWhiteSpace) {
            psi = myFixture.file.findElementAt(offset + 1)
        }

        return psi
    }

    /**
     * Returns the psi parent of the expected type when it's range exaclty matches the start element.
     * This mostly happens for wrappers of leaf nodes.
     */
    inline fun <reified T : PsiElement> psiWrappingParent(current: PsiElement): T? {
        val found = PsiTreeUtil.findFirstParent(current, false) { it is T }
        return when {
            found?.textRange == current.textRange -> found as T
            else -> null
        }
    }

    inline fun <reified T : PsiElement> PsiElement?.wrappingParent(): T? {
        return when (this) {
            null -> null
            else -> psiWrappingParent(this)
        }
    }

    inline fun <reified T : PsiElement> PsiElement?.firstParent(): T? {
        return when (this) {
            null -> null
            else -> PsiTreeUtil.findFirstParent(this, false) { it is T } as T?
        }
    }
}


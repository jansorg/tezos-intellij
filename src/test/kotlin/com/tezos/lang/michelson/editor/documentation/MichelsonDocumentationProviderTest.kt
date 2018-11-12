package com.tezos.lang.michelson.editor.documentation

import com.intellij.codeInsight.documentation.DocumentationManager
import com.intellij.psi.PsiFile
import com.tezos.lang.michelson.MichelsonFixtureTest
import com.tezos.lang.michelson.lang.MichelsonLanguage
import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonDocumentationProviderTest : MichelsonFixtureTest() {
    fun testInstructions() {
        for (i in MichelsonLanguage.INSTRUCTIONS) {
            configureByCode("$i<caret>")
            val doc = findDocumentation()
            Assert.assertFalse("documentation for instruction '$i' must not be null or empty.", doc.isNullOrEmpty())
        }
    }

    fun testtestPositions() {
        assertDocs("CREATE_CONTRACT<caret>\n")
        assertDocs("PAIR<caret>;UNPAIR;")
        assertDocs("PAIR<caret> ;UNPAIR;")
    }

    private fun findDocumentation(): String? {
        val originalElement = myFixture.file.findElementAt(myFixture.caretOffset)
        val manager = DocumentationManager.getInstance(project)
        val targetElement = manager.findTargetElement(myFixture.editor, myFixture.file, originalElement)
        if (targetElement is PsiFile || targetElement == null) {
            return null
        }

        val provider = DocumentationManager.getProviderFromElement(targetElement, originalElement)
        val doc = provider.generateDoc(targetElement, originalElement)
        return if ("No documentation found." == doc) null else doc
    }

    private fun assertDocs(code: String) {
        configureByCode(code)
        val doc = findDocumentation()
        Assert.assertFalse("documentation not found, must not be null or empty.", doc.isNullOrEmpty())
    }
}
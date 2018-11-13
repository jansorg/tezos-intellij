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
            Assert.assertTrue("documentation for instruction '$i' must not be null or empty.", !doc.isNullOrEmpty() || i in MichelsonLanguage.QUESTIONABLE_INSTRUCTIONS)
        }
    }

    fun testPositions() {
        assertDocs("CREATE_CONTRACT<caret>\n")
        assertDocs("PAIR<caret>;UNPAIR;")
        assertDocs("PAIR<caret> ;UNPAIR;")
    }

    fun testMacros() {
        assertDocs("FAIL<caret>")

        for (i in MichelsonLanguage.MACRO_NAMES) {
            configureByCode("$i<caret>")
            val doc = findDocumentation()
            Assert.assertFalse("documentation for instruction '$i' must not be null or empty.", doc.isNullOrEmpty())
        }
    }

    fun testTypes(){
        assertDocs("NIL in<caret>t")
        assertDocs("NIL string<caret>")
        assertDocs("NIL nat<caret>")
        assertDocs("NIL int<caret>")
        assertDocs("NIL bytes<caret>")

        assertDocs("NIL address<caret>")
        assertDocs("NIL big_map<caret>")
        assertDocs("NIL contract<caret>")
        assertDocs("NIL key<caret>")
        assertDocs("NIL key_hash<caret>")
        assertDocs("NIL list<caret>")
        assertDocs("NIL map<caret>")
        assertDocs("NIL mutez<caret>")
        assertDocs("NIL operation<caret>")
        assertDocs("NIL option<caret>")
        assertDocs("NIL or<caret>")
        assertDocs("NIL pair<caret>")
        assertDocs("NIL set<caret>")
        assertDocs("NIL signature<caret>")
        assertDocs("NIL timestamp<caret>")
        assertDocs("NIL unit<caret>")
    }

    fun testTags(){
        assertDocs("PUSH (map int int) { Elt<caret> 123 123 }")
        assertDocs("PUSH bool False<caret>")
        assertDocs("PUSH bool True<caret>")
        assertDocs("PUSH (option int int) Left<caret> 42 42")
        assertDocs("PUSH (option int int) None<caret>")
        assertDocs("PUSH (pair int int) Pair<caret> 1 2")
        assertDocs("PUSH (or int int) Right<caret> 42")
        assertDocs("PUSH (or int int) Some<caret> 42")
        assertDocs("PUSH unit Unit<caret>")
    }

    private fun assertDocs(code: String) {
        configureByCode(code)
        val doc = findDocumentation()
        Assert.assertFalse("documentation not found, must not be null or empty: $code", doc.isNullOrEmpty())
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
}
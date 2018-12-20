package com.tezos.lang.michelson.editor.intention

import com.tezos.lang.michelson.MichelsonFixtureTest
import junit.framework.TestCase
import org.junit.Assert

/**
 * @author jansorg
 */
class WrapSimpleTypeIntentionTest : MichelsonFixtureTest() {
    fun testWrap() {
        assertWrap("PUSH <caret>int 123", "code { PUSH (int) 123 }")
        assertWrap("PUSH <caret>string 123", "code { PUSH (string) 123 }")
        assertWrap("PUSH (pair int <caret>int)", "code { PUSH (pair int (int)) }")

        assertNoWrap("PUSH int <caret>123")
        assertNoWrap("PUSH <caret>pair")
        assertNoWrap("PUSH (<caret>int)")
    }

    private fun assertNoWrap(code: String) {
        configureByCode(code)
        try {
            myFixture.findSingleIntention("Wrap in parentheses")
            Assert.fail("Intention must be unavailable on $code")
        } catch (e: AssertionError) {
            // expected
        }
    }

    private fun assertWrap(code: String, expectedCodeResult: String) {
        val (file, _) = configureByCode(code)
        val intention = myFixture.findSingleIntention("Wrap in parentheses")
        Assert.assertNotNull(intention)
        myFixture.launchAction(intention)

        Assert.assertEquals(expectedCodeResult, file.getContract()?.findCodeSection()?.text)
    }

    override fun isWriteActionRequired(): Boolean {
        return false
    }
}
package com.tezos.lang.michelson.editor.intention

import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class UnwrapComplexTypeIntentionTest: MichelsonFixtureTest(){
    fun testUnwrap() {
        assertUnwrap("PUSH (<caret>int) 123", "code { PUSH int 123 }")
        assertUnwrap("PUSH (<caret>string) 123", "code { PUSH string 123 }")
        assertUnwrap("PUSH (pair int (<caret>int))", "code { PUSH (pair int int) }")

        assertNoUnwrap("PUSH int <caret>123")
        assertNoUnwrap("PUSH <caret>pair")
        assertNoUnwrap("PUSH (<caret>pair int int) (Pair 123 123)")
        assertNoUnwrap("PUSH (pair <caret>int int) (Pair 123 123)")
        assertNoUnwrap("PUSH (int @x) 123")
    }

    private fun assertNoUnwrap(code: String) {
        configureByCode(code)
        try {
            myFixture.findSingleIntention("Unwrap type")
            Assert.fail("Intention must be unavailable on $code")
        } catch (e: AssertionError) {
            // expected
        }
    }

    private fun assertUnwrap(code: String, expectedCodeResult: String) {
        val (file, _) = configureByCode(code)
        val intention = myFixture.findSingleIntention("Unwrap type")
        Assert.assertNotNull(intention)
        myFixture.launchAction(intention)

        Assert.assertEquals(expectedCodeResult, file.getContract()?.findCodeSection()?.text)
    }

    override fun isWriteActionRequired(): Boolean {
        return false
    }
}
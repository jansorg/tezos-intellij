package com.tezos.lang.michelson.editor.intention

import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class RemoveTypeIntentionTest : MichelsonFixtureTest() {
    fun testRemove() {
        assertRemoveType("PUSH (pair int int <caret>int) (Pair 12 23)", "code { PUSH (pair int int) (Pair 12 23) }")

        assertNoRemoveType("PUSH (pair int <caret>int) (Pair 12 23)")
        assertNoRemoveType("PUSH (pair int <caret>int int) (Pair 12 23)")
    }

    private fun assertRemoveType(code: String, expectedCode: String) {
        val (file, _) = configureByCode(code)
        val action = myFixture.findSingleIntention(RemoveTypeIntention.LABEL)
        myFixture.launchAction(action)

        Assert.assertEquals(expectedCode, file.getContract()?.findCodeSection()?.text)
    }

    private fun assertNoRemoveType(code: String) {
        configureByCode(code)
        try {
            myFixture.findSingleIntention(RemoveTypeIntention.LABEL)
            Assert.fail("Intention must be unavailable on $code")
        } catch (e: AssertionError) {
            // expected
        }
    }

    override fun isWriteActionRequired(): Boolean {
        return false
    }
}

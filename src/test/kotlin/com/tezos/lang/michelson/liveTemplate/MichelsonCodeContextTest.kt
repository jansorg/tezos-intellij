package com.tezos.lang.michelson.liveTemplate

import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonCodeContextTest : MichelsonFixtureTest() {
    fun testContext() {
        val ctx = MichelsonCodeContext()
        val (file, _) = configureByCode("<caret>")

        Assert.assertTrue(ctx.isInContext(file, myFixture.caretOffset))
    }

    fun testInvalidContext() {
        val ctx = MichelsonCodeContext()
        val (file, _) = configureByCode("DROP <caret>;")

        Assert.assertFalse(ctx.isInContext(file, myFixture.caretOffset))
    }
}
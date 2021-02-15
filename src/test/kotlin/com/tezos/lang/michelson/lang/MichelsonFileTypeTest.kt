package com.tezos.lang.michelson.lang

import com.tezos.lang.michelson.MichelsonFixtureTest
import com.tezos.lang.michelson.psi.MichelsonPsiFile
import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonFileTypeTest : MichelsonFixtureTest() {
    fun testfiles() {
        Assert.assertTrue(myFixture.configureByText("file.tz", "") is MichelsonPsiFile)
        Assert.assertTrue(myFixture.configureByText("file.tez", "") is MichelsonPsiFile)
    }
}
package com.tezos.lang.michelson.psi

import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonPsiUtilTest : MichelsonFixtureTest() {
    fun testIsFirstCodeChild() {
        // caret is refering to the element right to it

        var e = configureByCode("<caret>").second?.prevSibling
        Assert.assertTrue(MichelsonPsiUtil.isFirstCodeChild(e))

        e = configureByCode("   <caret>").second?.prevSibling
        Assert.assertTrue(MichelsonPsiUtil.isFirstCodeChild(e))

        // closing }
        e = configureByCode("<caret>").second
        Assert.assertFalse(MichelsonPsiUtil.isFirstCodeChild(e))

        e = configureByCode("DROP; <caret>").second?.prevSibling
        Assert.assertFalse(MichelsonPsiUtil.isFirstCodeChild(e))
    }
}
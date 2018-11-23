package com.tezos.lang.michelson.psi

import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonPsiUtilTest : MichelsonFixtureTest() {
    fun testIsFirstCodeChild() {
        // caret is refering to the element right to it

        // {
        var e = configureByCode("<caret>", true).second
        Assert.assertTrue(MichelsonPsiUtil.isFirstCodeChild(e, myFixture.caretOffset))

        // {   <caret>}
        e = configureByCode("   <caret>", true).second
        Assert.assertTrue(MichelsonPsiUtil.isFirstCodeChild(e, myFixture.caretOffset))

        // {<caret>}
        e = configureByCode("<caret>", true).second
        Assert.assertTrue(MichelsonPsiUtil.isFirstCodeChild(e, myFixture.caretOffset))

        // {   <caret>}
        e = configureByCode("   <caret>", true).second
        Assert.assertTrue(MichelsonPsiUtil.isFirstCodeChild(e, myFixture.caretOffset))

        // {DROP; <caret>}
        e = configureByCode("DROP; <caret>", true).second
        Assert.assertFalse(MichelsonPsiUtil.isFirstCodeChild(e, myFixture.caretOffset))

        // {DIP { <caret> }}
        e = configureByCode("DIP{ <caret> }", true).second
        Assert.assertFalse(MichelsonPsiUtil.isFirstCodeChild(e, myFixture.caretOffset))
    }
}
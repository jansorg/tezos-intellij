package com.tezos.lang.michelson.psi.impl

import com.tezos.lang.michelson.MichelsonFixtureTest
import com.tezos.lang.michelson.psi.PsiBlockInstruction
import org.junit.Assert

/**
 * @author jansorg
 */
class PsiBlockInstructionImplTest : MichelsonFixtureTest() {
    fun testWhitespace() {
        val (_, psi) = configureByCode("{<caret>}")
        val block = psi?.parent as? PsiBlockInstruction
        Assert.assertNotNull(block)
        Assert.assertTrue(block!!.isWhitespaceOnly)
    }

    fun testNotWhitespace() {
        val (_, psi) = configureByCode("{ DROP; <caret>}")
        val block = psi?.parent as? PsiBlockInstruction
        Assert.assertNotNull(block)
        Assert.assertFalse(block!!.isWhitespaceOnly)
    }
}
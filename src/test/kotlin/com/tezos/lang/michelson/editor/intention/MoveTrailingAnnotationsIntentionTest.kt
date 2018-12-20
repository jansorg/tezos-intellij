package com.tezos.lang.michelson.editor.intention

import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert
import org.junit.Assert.*

/**
 * @author jansorg
 */
class MoveTrailingAnnotationsIntentionTest: MichelsonFixtureTest(){
    fun testInstruction() {
        val (file, psi) = configureByCode("PUSH int 123 @<caret>a")
        val intention = myFixture.findSingleIntention(MoveTrailingAnnotationsIntention.LABEL)
        myFixture.launchAction(intention)

        Assert.assertEquals("code { PUSH @a int 123 }", file.getContract()?.findCodeSection()?.text)
    }

    override fun isWriteActionRequired(): Boolean {
        return false
    }
}
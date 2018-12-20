package com.tezos.lang.michelson.editor.intention

import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert
import java.lang.AssertionError

/**
 * @author jansorg
 */
class RemoveAnnotationIntentionTest : MichelsonFixtureTest() {
    fun testAnnotations() {
        assertRemovedAnnotation("PUSH @a :b %<caret>c int 123", "code { PUSH @a :b int 123 }")
        assertRemovedAnnotation("DROP %<caret>c", "code { DROP }")

        assertRemovedAnnotation("PUSH @a @<caret>b", "code { PUSH @a }")

        assertUnavailable("PUSH @<caret>a int 123")
    }

    private fun assertUnavailable(code: String) {
        configureByCode(code)
        try {
            myFixture.findSingleIntention(RemoveAnnotationIntention.LABEL)
            Assert.fail("intention must be unavailable here")
        } catch (e: AssertionError) {
            //expected
        }
    }

    private fun assertRemovedAnnotation(code: String, resultCode: String) {
        val (file, _) = configureByCode(code)
        val intention = myFixture.findSingleIntention(RemoveAnnotationIntention.LABEL)
        myFixture.launchAction(intention)

        Assert.assertEquals(resultCode, file.getContract()?.findCodeSection()?.text)
    }

    override fun isWriteActionRequired(): Boolean {
        return false
    }
}
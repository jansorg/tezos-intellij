package com.tezos.lang.michelson.editor.intention

import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert
import java.lang.AssertionError

/**
 * @author jansorg
 */
class MoveTrailingAnnotationsIntentionTest : MichelsonFixtureTest() {
    fun testInstruction() {
        assertMovedAnnotations("PUSH int 123 @<caret>a", "PUSH @a int 123")
        assertMovedAnnotations("PUSH int 123 @a %<caret>b", "PUSH @a %b int 123")

        assertUnavailable("PUSH @a %<caret>b int 123")
    }

    fun testCreateContract() {
        assertMovedAnnotations("CREATE_CONTRACT { code {} } @<caret>a", "CREATE_CONTRACT @a { code {} }")

        assertUnavailable("CREATE_CONTRACT @<caret>a { code {} }")
    }

    fun testMacro() {
        assertMovedAnnotations("DIIP {} @<caret>a %a", "DIIP @a %a {}")
        assertMovedAnnotations("IFEQ {} @<caret>a :b", "IFEQ @a :b {}")

        assertUnavailable("IFEQ @<caret>a :b {}")
    }

    fun testComplexType() {
        assertMovedAnnotations("PUSH (pair int int @<caret>a :b) (Pair 123 123)", "PUSH (pair @a :b int int) (Pair 123 123)")

        assertUnavailable("PUSH (pair @a :<caret>x int int) (Pair 123 123)")
    }

    private fun assertUnavailable(code: String) {
        configureByCode(code)
        try {
            myFixture.findSingleIntention(MoveTrailingAnnotationsIntention.LABEL)
            Assert.fail("annotation must be unavailable")
        } catch (e: AssertionError) {
            // expected
        }
    }

    private fun assertMovedAnnotations(code: String, resultCode: String) {
        val (file, _) = configureByCode(code)
        val intention = myFixture.findSingleIntention(MoveTrailingAnnotationsIntention.LABEL)
        myFixture.launchAction(intention)

        Assert.assertEquals(resultCode, file.getContract()?.findCodeSection()?.instructions?.text?.removeSurrounding("{ ", " }"))
    }

    override fun isWriteActionRequired(): Boolean {
        return false
    }
}
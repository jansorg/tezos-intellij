package com.tezos.lang.michelson.stackInfo

import com.intellij.codeInsight.lookup.LookupManager
import com.intellij.testFramework.fixtures.CompletionAutoPopupTester
import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonStackAutoPopupTest : MichelsonFixtureTest() {
    private var tester: CompletionAutoPopupTester? = null

    override fun setUp() {
        super.setUp()
        tester = CompletionAutoPopupTester(myFixture)
    }

    fun testNoUpdateOnLookup() {
        val mockMgr = MockMichelsonStackInfoManager.getInstance(project)
        configureByCodeAndFocus("PUSH <caret>")

        tester!!.typeWithPauses("s")
        Assert.assertNotNull("expected an open code completion lookup", LookupManager.getActiveLookup(myFixture.editor))
        Assert.assertEquals(0, mockMgr.documentUpdateCount(myFixture.editor.document))

        tester!!.typeWithPauses("t")
        Assert.assertNotNull("expected an open code completion lookup", LookupManager.getActiveLookup(myFixture.editor))
        Assert.assertEquals(0, mockMgr.documentUpdateCount(myFixture.editor.document))
    }

    override fun runInDispatchThread(): Boolean {
        return false
    }

    override fun invokeTestRunnable(runnable: Runnable) {
        tester!!.runWithAutoPopupEnabled(runnable)
    }

    override fun isWriteActionRequired(): Boolean {
        return false
    }
}
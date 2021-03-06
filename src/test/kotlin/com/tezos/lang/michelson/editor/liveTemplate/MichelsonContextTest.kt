package com.tezos.lang.michelson.editor.liveTemplate

import com.intellij.json.JsonFileType
import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonContextTest : MichelsonFixtureTest() {
    fun testContext() {
        val ctx = MichelsonContext()
        val (file, _) = configureByCode("<caret>")
        Assert.assertTrue(ctx.isInContext(file, myFixture.caretOffset))
    }

    fun testInvalidContext() {
        val ctx = MichelsonContext()
        val file = myFixture.configureByText(JsonFileType.INSTANCE, "file.json")
        Assert.assertFalse(ctx.isInContext(file, myFixture.caretOffset))
    }
}
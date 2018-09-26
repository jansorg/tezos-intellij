package com.tezos.lang.michelson.editor.liveTemplate

import com.intellij.json.JsonFileType
import com.tezos.lang.michelson.MichelsonFileType
import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonToplevelContextTest : MichelsonFixtureTest() {
    fun testContext() {
        val ctx = MichelsonToplevelContext()

        var file = myFixture.configureByText(MichelsonFileType, "")
        Assert.assertTrue(ctx.isInContext(file, myFixture.caretOffset))

        file = myFixture.configureByText(MichelsonFileType, "#comment\n<caret>")
        Assert.assertTrue(ctx.isInContext(file, myFixture.caretOffset))

        file = myFixture.configureByText(MichelsonFileType, "<caret>\n#comment")
        Assert.assertTrue(ctx.isInContext(file, myFixture.caretOffset))
    }

    fun testInvalidContext() {
        val ctx = MichelsonToplevelContext()

        val (file, _) = configureByCode("<caret>")
        Assert.assertFalse(ctx.isInContext(file, myFixture.caretOffset))

        Assert.assertFalse(ctx.isInContext(myFixture.configureByText(JsonFileType.INSTANCE, ""), myFixture.caretOffset))
    }
}
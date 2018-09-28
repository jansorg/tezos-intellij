package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.codeInsight.completion.CompletionType
import com.tezos.lang.michelson.lang.MichelsonFileType
import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonCompletionContributorTest : MichelsonCompletionTest() {
    fun testSectionCompletion() {
        // basic
        myFixture.configureByText(MichelsonFileType, "")
        assertCompletions("parameter ", "storage ", "code ")

        myFixture.configureByText(MichelsonFileType, "parameter <caret>")
        assertCompletions()

        // smart
        myFixture.configureByText(MichelsonFileType, "parameter unit;\n<caret>")
        assertCompletions("storage ", "code ", type = CompletionType.SMART)

        myFixture.configureByText(MichelsonFileType, "parameter unit;\nstorage unit;\n<caret>")
        assertCompletions("code ", type = CompletionType.SMART)

        myFixture.configureByText(MichelsonFileType, "parameter unit;\nstorage unit;code {};\n<caret>")
        assertCompletions(type = CompletionType.SMART)

        myFixture.configureByText(MichelsonFileType, "parameter <caret>")
        assertCompletions(type = CompletionType.SMART)
    }
}
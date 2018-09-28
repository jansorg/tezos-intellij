package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.codeInsight.completion.CompletionType
import com.tezos.lang.michelson.MichelsonFileType
import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonFileCompletionContributorTest : MichelsonFixtureTest() {
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

    private fun assertCompletions(vararg items: String, type: CompletionType = CompletionType.BASIC) {
        val sortedItems = items.sorted()
        val completions = completionStrings(type)?.sorted()

        Assert.assertEquals(sortedItems, completions)
    }

    private fun completionStrings(type: CompletionType = CompletionType.BASIC): List<String>? {
        val oldBasic = CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION
        val oldSmart = CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_SMART_TYPE_COMPLETION

        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_SMART_TYPE_COMPLETION = false

        try {
            myFixture.complete(type)
            return myFixture.lookupElementStrings
        } finally {
            CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = oldBasic
            CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_SMART_TYPE_COMPLETION = oldSmart
        }
    }
}
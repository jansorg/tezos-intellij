package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.codeInsight.completion.CompletionType
import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
abstract class MichelsonCompletionTest() : MichelsonFixtureTest() {
    fun assertCompletions(vararg items: String, type: CompletionType = CompletionType.BASIC) {
        val sortedItems = items.sorted()
        val completions = completionStrings(type)?.sorted()

        Assert.assertEquals(sortedItems, completions)
    }

    fun completionStrings(type: CompletionType = CompletionType.BASIC): List<String>? {
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
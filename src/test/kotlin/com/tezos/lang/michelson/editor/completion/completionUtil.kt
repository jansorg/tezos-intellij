package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.codeInsight.completion.CompletionType
import com.tezos.client.MockTezosClient
import com.tezos.intellij.settings.TezosClientConfig
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.lang.michelson.MichelsonFixtureTest
import com.tezos.lang.michelson.stackInfo.MockMichelsonStackInfoManager
import org.junit.Assert

/**
 * @author jansorg
 */
abstract class MichelsonCompletionTest(val setupDefaultClient: Boolean = true) : MichelsonFixtureTest() {
    override fun setUp() {
        super.setUp()

        if (setupDefaultClient) {
            TezosSettingService.getSettings().setClients(listOf(TezosClientConfig("unit test client", "alphanet.sh", true)))
        }

        MockTezosClient.reset()
        MockMichelsonStackInfoManager.getInstance(project).reset()
    }

    fun assertCompletions(vararg items: String, type: CompletionType = CompletionType.BASIC) {
        val completions = completionStrings(type)?.sorted()

        Assert.assertEquals(items.sorted(), completions)
    }

    fun assertCompletionsAtLeast(items: List<String>, type: CompletionType = CompletionType.BASIC) = assertCompletionsAtLeast(*items.toTypedArray(), type = type)

    fun assertCompletionsAtLeast(vararg items: String, type: CompletionType = CompletionType.BASIC) {
        val sortedItems = items.sorted()
        val completions = completionStrings(type)?.sorted()
        if (completions == null) {
            Assert.fail("no completions available")
            return
        }

        Assert.assertTrue("Completions missing. Expected: ${items.joinToString(",")}, found: ${completions.joinToString(",")}", completions.containsAll(sortedItems))
    }

    fun assertCompletionsNoneOf(vararg items: String, type: CompletionType = CompletionType.BASIC) {
        val completions = completionStrings(type)?.sorted()
        if (completions == null || completions.isEmpty()) {
            return
        }

        val present = completions.filter { items.contains(it) }
        Assert.assertTrue("Unexpected completions: ${present.joinToString(",")}", present.isEmpty())
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
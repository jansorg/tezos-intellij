package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionType
import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonTagCompletionTest : MichelsonCompletionTest() {
    fun testCompletion() {
        val tagNames = MichelsonLanguage.TAGS_METAS.filterNot { it.isComplex() }.flatMap { it.names() }.toTypedArray()

        // basic
        configureByCode("PUSH bool <caret>")
        assertCompletionsAtLeast(*tagNames)

        configureByCode("PUSH unit <caret>")
        assertCompletionsAtLeast(*tagNames)

        configureByCode("PUSH unit T<caret>")
        assertCompletionsAtLeast(*tagNames.filter { it.contains("T") }.toTypedArray())

        configureByCode("PUSH unit <caret>T")
        assertCompletionsAtLeast(*tagNames)

        configureByCode("PUSH unit Tr<caret>")
        assertCompletionsAtLeast(*tagNames.filter { it.contains("Tr") }.toTypedArray())

        configureByCode("PUSH unit <caret>Tr")
        assertCompletionsAtLeast(*tagNames)

        configureByCode("PUSH unit Fal<caret>")
        assertCompletionsAtLeast(*tagNames.filter { it.contains("Fal") }.toTypedArray())

        configureByCode("PUSH unit (<caret>)")
        assertCompletionsAtLeast(*tagNames)

        configureByCode("PUSH unit (Tr<caret>)")
        assertCompletionsAtLeast(*tagNames.filter { it.contains("Tr") }.toTypedArray())

        configureByCode("PUSH unit (Fal<caret>)")
        assertCompletionsAtLeast(*tagNames.filter { it.contains("Fal") }.toTypedArray())

        // smart
        configureByCode("PUSH int <caret>")
        assertCompletions(*tagNames, type = CompletionType.SMART)

        configureByCode("PUSH bool True<caret>")
        assertCompletions(*tagNames.filter { it.startsWith("True") }.toTypedArray(), type = CompletionType.SMART)

        configureByCode("PUSH bool Tr<caret>ue")
        assertCompletions(*tagNames.filter { it.startsWith("Tr") }.toTypedArray(), type = CompletionType.SMART)

        configureByCode("PUSH bool <caret>True")
        assertCompletions(*tagNames, type = CompletionType.SMART)

        // for now until we do typecheck the tags against the expected type
        configureByCode("PUSH int <caret>123")
        assertCompletions(*tagNames, type = CompletionType.SMART)

        // no smart completions
        configureByCode("NIL <caret>")
        assertCompletionsNoneOf(*tagNames, type = CompletionType.SMART)

        configureByCode("PUSH <caret>")
        assertCompletionsNoneOf(*tagNames, type = CompletionType.SMART)

        configureByCode("PUSH int<caret>")
        assertCompletionsNoneOf(*tagNames, type = CompletionType.SMART)

        configureByCode("PUSH in<caret>t")
        assertCompletionsNoneOf(*tagNames, type = CompletionType.SMART)

        configureByCode("PUSH <caret> int")
        assertCompletionsNoneOf(*tagNames, type = CompletionType.SMART)

        // no completions
        configureByCode("<caret>")
        assertCompletionsNoneOf(*tagNames)

        configureByCode("PUSH (Pair <caret>)")
        assertCompletionsNoneOf(*tagNames)

        configureByCode("PUSH (Pair int <caret>)")
        assertCompletionsNoneOf(*tagNames)
    }
}
package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionType
import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonSimpleTagCompletionTest : MichelsonCompletionTest() {
    fun testCompletion() {
        val reference = MichelsonLanguage.TAGS_METAS.filterNot { it.isComplex() }.flatMap { it.names() }.toTypedArray()

        // basic
        configureByCode("PUSH bool <caret>")
        assertCompletionsAtLeast(*reference)

        configureByCode("PUSH unit <caret>")
        assertCompletionsAtLeast(*reference)

        configureByCode("PUSH unit T<caret>")
        assertCompletionsAtLeast(*reference.filter { it.contains("T") }.toTypedArray())

        configureByCode("PUSH unit <caret>T")
        assertCompletionsAtLeast(*reference)

        configureByCode("PUSH unit Tr<caret>")
        assertCompletionsAtLeast(*reference.filter { it.contains("Tr") }.toTypedArray())

        configureByCode("PUSH unit <caret>Tr")
        assertCompletionsAtLeast(*reference)

        configureByCode("PUSH unit Fal<caret>")
        assertCompletionsAtLeast(*reference.filter { it.contains("Fal") }.toTypedArray())

        configureByCode("PUSH unit (<caret>)")
        assertCompletionsAtLeast(*reference)

        configureByCode("PUSH unit (Tr<caret>)")
        assertCompletionsAtLeast(*reference.filter { it.contains("Tr") }.toTypedArray())

        configureByCode("PUSH unit (Fal<caret>)")
        assertCompletionsAtLeast(*reference.filter { it.contains("Fal") }.toTypedArray())

        // smart
        configureByCode("NIL <caret>")
        assertCompletionsNoneOf(*reference, type = CompletionType.SMART)

        configureByCode("PUSH int <caret>")
        assertCompletions(*reference, type = CompletionType.SMART)

        configureByCode("PUSH bool True<caret>")
        assertCompletionsNoneOf(*reference, type = CompletionType.SMART)

        configureByCode("PUSH bool Tr<caret>ue")
        assertCompletions(*reference, type = CompletionType.SMART)

        configureByCode("PUSH bool <caret>True")
        assertCompletions(*reference, type = CompletionType.SMART)

        configureByCode("PUSH <caret>")
        assertCompletionsNoneOf(*reference, type = CompletionType.SMART)

        configureByCode("PUSH int<caret>")
        assertCompletionsNoneOf(*reference, type = CompletionType.SMART)

        configureByCode("PUSH in<caret>t")
        assertCompletionsNoneOf(*reference, type = CompletionType.SMART)

        configureByCode("PUSH <caret> int")
        assertCompletionsNoneOf(*reference, type = CompletionType.SMART)

        configureByCode("PUSH <caret>")
        assertCompletionsNoneOf(*reference, type = CompletionType.SMART)

        // no completions
        configureByCode("<caret>")
        assertCompletionsNoneOf(*reference)

        configureByCode("PUSH (Pair <caret>)")
        assertCompletionsNoneOf(*reference)

        configureByCode("PUSH (Pair int <caret>)")
        assertCompletionsNoneOf(*reference)
    }
}
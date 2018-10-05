package com.tezos.lang.michelson.editor.completion

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

        configureByCode("<caret>")
        assertCompletionsNoneOf(*reference)
    }
}
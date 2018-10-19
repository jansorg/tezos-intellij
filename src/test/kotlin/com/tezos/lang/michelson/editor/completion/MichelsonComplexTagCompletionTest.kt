package com.tezos.lang.michelson.editor.completion

import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonComplexTagCompletionTest : MichelsonCompletionTest() {
    fun testCompletion() {
        val reference = MichelsonLanguage.TAGS_METAS.filter { it.isComplex() }.flatMap { it.names() }.toTypedArray()

        // basic
        configureByCode("PUSH bool (<caret>)")
        assertCompletionsAtLeast(*reference)

        configureByCode("PUSH unit (<caret>)")
        assertCompletionsAtLeast(*reference)

        configureByCode("PUSH unit (Pa<caret>)")
        assertCompletionsAtLeast(*reference.filter { it.contains("Pa") }.toTypedArray())

        configureByCode("PUSH unit (Op<caret>)")
        assertCompletionsAtLeast(*reference.filter { it.contains("Op") }.toTypedArray())

        // negative matches
        configureByCode("<caret>")
        assertCompletionsNoneOf(*reference)

        configureByCode("PUSH <caret>")
        assertCompletionsNoneOf(*reference)

        configureByCode("PUSH unit <caret>")
        assertCompletionsNoneOf(*reference)

        configureByCode("PUSH (Pair <caret>)")
        assertCompletionsNoneOf(*reference)

        configureByCode("PUSH (Pair int <caret>)")
        assertCompletionsNoneOf(*reference)
    }
}
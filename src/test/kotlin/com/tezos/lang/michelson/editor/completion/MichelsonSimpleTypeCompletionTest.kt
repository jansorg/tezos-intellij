package com.tezos.lang.michelson.editor.completion

import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonSimpleTypeCompletionTest : MichelsonCompletionTest() {
    fun testCompletion() {
        val reference = MichelsonLanguage.TYPES_COMPARABLE.toTypedArray()

        // basic
        configureByCode("PUSH <caret>")
        assertCompletionsAtLeast(*reference)

        configureByCode("PUSH i<caret>")
        assertCompletionsAtLeast(reference.filter { it.contains("i") })

        configureByCode("PUSH n<caret>")
        assertCompletionsAtLeast(reference.filter { it.contains("n") })

        // no completions
        configureByCode("PUSH abcde<caret>;")
        assertCompletions() // no completions here

        configureByCode("PUSH int 123<caret>;")
        assertCompletions()

        configureByCode("PUSH 123<caret>;")
        assertCompletions()

        configureByCode("PUSH (pair int int) (Pair 123<caret>);")
        assertCompletions()

        configureByCode("PUSH (Pair <caret>)")
        assertCompletionsNoneOf(*reference)

        // configureByCode("PUSH (Pair int <caret>)")
        // assertCompletionsNoneOf(*reference)
    }
}
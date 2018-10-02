package com.tezos.lang.michelson.editor.completion

import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonSimpleTypeCompletionTest : MichelsonCompletionTest() {
    fun testCompletion() {
        val reference = MichelsonLanguage.COMPARABLE_TYPES.toTypedArray()

        // basic
        configureByCode("PUSH <caret>")
        assertCompletionsAtLeast(*reference)

        configureByCode("PUSH i<caret>")
        assertCompletionsAtLeast(* reference.filter { it.contains("i") }.toTypedArray())

        configureByCode("PUSH n<caret>")
        assertCompletionsAtLeast(* reference.filter { it.contains("n") }.toTypedArray())

        // no completions
        configureByCode("PUSH abcde<caret>;")
        assertCompletions() // no completions here

        configureByCode("PUSH int 123<caret>;")
        assertCompletions()

        configureByCode("PUSH 123<caret>;")
        assertCompletions()

        configureByCode("PUSH (pair int int) (Pair 123<caret>);")
        assertCompletions()
    }
}
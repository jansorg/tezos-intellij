package com.tezos.lang.michelson.editor.completion

import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonSimpleTypeNameCompletionTest : MichelsonCompletionTest() {
    fun testCompletion() {
        val reference = MichelsonLanguage.COMPARABLE_TYPES.toTypedArray()

        // basic
        configureByCode("PUSH <caret>")
        assertCompletions(*reference)

        configureByCode("PUSH i<caret>")
        assertCompletions(* reference.filter { it.contains("i") }.toTypedArray())

        configureByCode("PUSH n<caret>")
        assertCompletions(* reference.filter { it.contains("n") }.toTypedArray())

        configureByCode("PUSH abcde<caret>;")
        assertCompletions() // no completions here

    }
}
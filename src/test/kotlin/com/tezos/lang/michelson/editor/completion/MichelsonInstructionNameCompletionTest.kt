package com.tezos.lang.michelson.editor.completion

import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonInstructionNameCompletionTest : MichelsonCompletionTest() {
    fun testCompletion() {
        val instructions = MichelsonLanguage.INSTRUCTIONS.toTypedArray()

        // basic
        configureByCode("<caret>")
        assertCompletions(*instructions)

        configureByCode("IF<caret>")
        assertCompletions(* instructions.filter { it.contains("IF") }.toTypedArray())

        configureByCode("DROP <caret>;")
        assertCompletionsNoneOf(*instructions) // no instruction completions here

        configureByCode("PUSH int F<caret>;")
        assertCompletionsNoneOf(*instructions) // no instruction completions here
    }
}
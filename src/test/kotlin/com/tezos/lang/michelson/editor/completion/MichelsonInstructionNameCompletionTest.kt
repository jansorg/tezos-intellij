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

        configureByCode("PUSH int F<caret>;")
        // we expect instruction completion here, it's the user's choice. We could avoid this in smart mode, if we want
        assertCompletionsAtLeast(instructions.filter { it.contains("F") })

        configureByCode("DROP <caret>;")
        assertCompletionsNoneOf(*instructions) // no instruction completions here
    }
}
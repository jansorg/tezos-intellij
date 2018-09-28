package com.tezos.lang.michelson.editor.completion

import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonInstructionNameCompletionTest : MichelsonCompletionTest() {
    fun testCompletion() {
        val insertedInstructions = MichelsonLanguage.INSTRUCTIONS.toTypedArray()

        // basic
        configureByCode("<caret>")
        assertCompletions(*insertedInstructions)

        configureByCode("IF<caret>")
        assertCompletions(* insertedInstructions.filter { it.contains("IF") }.toTypedArray())

        configureByCode("DROP <caret>;")
        assertCompletionsNoneOf(*insertedInstructions) // no instruction completions here
    }
}
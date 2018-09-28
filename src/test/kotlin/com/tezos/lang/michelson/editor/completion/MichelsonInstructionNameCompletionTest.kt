package com.tezos.lang.michelson.editor.completion

import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonInstructionNameCompletionTest : MichelsonCompletionTest() {
    fun testCompletion() {
        val insertedInstructions = MichelsonLanguage.INSTRUCTIONS.map { "$it " }.toTypedArray()

        // basic
        configureByCode("<caret>")
        assertCompletions(*insertedInstructions)

        configureByCode("IF<caret>")
        assertCompletions(* insertedInstructions.filter { it.startsWith("IF") }.toTypedArray())

        configureByCode("DROP <caret>;")
        assertCompletions() // no completions here

    }
}
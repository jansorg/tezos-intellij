package com.tezos.lang.michelson.editor.completion

import com.tezos.lang.michelson.lang.MichelsonFileType
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

        configureByCode("PAIR; <caret>")
        assertCompletions(* instructions)

        myFixture.configureByText(MichelsonFileType, "code {<caret>}")
        assertCompletions(* instructions)

        configureByCode("PAIR; CDR; <caret>")
        assertCompletions(*instructions)

        configureByCode("PAIR; CDR; P<caret>")
        assertCompletions(*instructions.filter { it.contains("P") }.toTypedArray())

        configureByCode("PAIR; CDR; <caret>P")
        assertCompletions(*instructions)

        // no completions
        configureByCode("PUSH int F<caret>;")
        assertCompletionsNoneOf(*instructions)

        configureByCode("PUSH int <caret>")
        assertCompletionsNoneOf(*instructions)

        configureByCode("PUSH int <caret>;")
        assertCompletionsNoneOf(*instructions)

        configureByCode("PUSH int <caret>F")
        assertCompletionsNoneOf(*instructions)

        configureByCode("DROP <caret>;")
        assertCompletionsNoneOf(*instructions)
    }
}
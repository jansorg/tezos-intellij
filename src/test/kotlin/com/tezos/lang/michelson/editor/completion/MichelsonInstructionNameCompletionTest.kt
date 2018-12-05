package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionType
import com.tezos.lang.michelson.lang.MichelsonFileType
import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonInstructionNameCompletionTest : MichelsonCompletionTest() {
    val instructions = (MichelsonLanguage.INSTRUCTION_NAMES + MichelsonLanguage.MACRO_NAMES).toTypedArray()

    fun testCompletion() {
        // basic
        configureByCode("<caret>")
        assertCompletions(*instructions)

        configureByCode("IF<caret>")
        assertCompletions(*instructions.filter { it.contains("IF") }.toTypedArray())

        configureByCode("PAIR; <caret>")
        assertCompletions(*instructions)

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

        configureByCode("# comment <caret>\n")
        assertCompletions()
    }

    fun testEmptyFile() {
        myFixture.configureByText(MichelsonFileType,
                """parameter unit;
                   <caret>storage unit;
                   code {}""".trimIndent())
        assertCompletionsNoneOf(*instructions, type = CompletionType.BASIC)
        assertCompletionsNoneOf(*instructions, type = CompletionType.SMART)
    }
}
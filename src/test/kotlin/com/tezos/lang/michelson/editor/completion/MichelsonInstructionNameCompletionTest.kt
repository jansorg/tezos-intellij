package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionType
import com.tezos.client.MockTezosClient
import com.tezos.client.stack.MichelsonStack
import com.tezos.lang.michelson.lang.LangTypes
import com.tezos.lang.michelson.lang.MichelsonFileType
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.stackInfo.MockMichelsonStackInfoManager

/**
 * @author jansorg
 */
class MichelsonInstructionNameCompletionTest : MichelsonCompletionTest() {
    private val instructions = (MichelsonLanguage.INSTRUCTIONS.map { it.name } + MichelsonLanguage.MACRO_NAMES).toTypedArray()

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

    fun testSmartPair() {
        stackAndCode("<caret>", stackOf(pair(LangTypes.INT, LangTypes.STRING)), MichelsonStack.EMPTY)
        assertCompletionsAtLeast("AMOUNT", "BALANCE", "CAR", "CAST", "CDR", "DROP", "DUP", "EMPTY_SET", "EMPTY_MAP", "FAIL", "NOW", "SELF", "SENDER", "SOURCE", "STEPS_TO_QUOTA", "UNIT", type = CompletionType.SMART)
    }

    fun testSmartEmpty() {
        stackAndCode("<caret>", MichelsonStack.EMPTY, stackOf(LangTypes.INT))
        assertCompletionsAtLeast("AMOUNT", "BALANCE", "FAIL", "NOW", "SELF", "SENDER", "SOURCE", "STEPS_TO_QUOTA", "UNIT", type = CompletionType.SMART)
        assertCompletionsNoneOf("DUP", "DIP", "CAR", "CDR", "DROP", type = CompletionType.SMART)
    }

    private fun stackAndCode(content: String, before: MichelsonStack, after: MichelsonStack) {
        val (psi, _) = configureByCode(content)
        MockTezosClient.addTypes(psi, before, after)
        MockMichelsonStackInfoManager.getInstance(project).forceUpdate(myFixture.editor)
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
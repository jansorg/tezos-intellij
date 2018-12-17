package com.tezos.lang.michelson.editor.completion.provider

import com.intellij.codeInsight.completion.CompletionType
import com.tezos.lang.michelson.editor.completion.MichelsonCompletionTest
import com.tezos.lang.michelson.lang.MichelsonFileType
import com.tezos.lang.michelson.psi.PsiSectionType

/**
 * @author jansorg
 */
class SectionCompletionTest : MichelsonCompletionTest() {
    private val names = PsiSectionType.completionValues.map { it.codeName() }.toTypedArray()

    fun testSectionCompletion() {
        // basic
        myFixture.configureByText(MichelsonFileType, "")
        assertCompletions("parameter ", "storage ", "code ")

        // smart
        myFixture.configureByText(MichelsonFileType, "parameter unit;\n<caret>")
        assertCompletions("storage ", "code ", type = CompletionType.SMART)

        myFixture.configureByText(MichelsonFileType, "parameter unit;\nstorage unit;\n<caret>")
        assertCompletions("code ", type = CompletionType.SMART)

        myFixture.configureByText(MichelsonFileType, "parameter unit;\nstorage unit;code {};\n<caret>")
        assertCompletions(type = CompletionType.SMART)

        myFixture.configureByText(MichelsonFileType, "parameter <caret>")
        assertCompletions(type = CompletionType.SMART)

        // no completions
        myFixture.configureByText(MichelsonFileType, "parameter <caret>")
        assertCompletions()

        myFixture.configureByText(MichelsonFileType, "parameter (pair int <caret>);")
        assertCompletions()
        assertCompletions(type = CompletionType.SMART)

        myFixture.configureByText(MichelsonFileType, "parameter (<caret>);")
        assertCompletionsNoneOf(*names)
        assertCompletionsNoneOf(*names, type = CompletionType.SMART)

        myFixture.configureByText(MichelsonFileType, "parameter <caret>;")
        assertCompletionsNoneOf(*names)
        assertCompletionsNoneOf(*names, type = CompletionType.SMART)
    }
}
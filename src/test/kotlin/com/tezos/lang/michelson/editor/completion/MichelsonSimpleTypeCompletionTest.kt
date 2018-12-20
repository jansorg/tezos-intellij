package com.tezos.lang.michelson.editor.completion

import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonSimpleTypeCompletionTest : MichelsonCompletionTest() {
    fun testCompletion() {
        val comparableTypes = MichelsonLanguage.TYPES.filter { it.isComparable }.map { it.name }.toTypedArray()
        val simpleTypes = MichelsonLanguage.TYPES.filter { it.subtypes.isEmpty() }.map { it.name }.toTypedArray()

        // basic
        configureByCode("PUSH <caret>")
        assertCompletionsAtLeast(*comparableTypes)

        configureByCode("PUSH i<caret>")
        assertCompletionsAtLeast(comparableTypes.filter { it.contains("i") })

        configureByCode("PUSH n<caret>")
        assertCompletionsAtLeast(comparableTypes.filter { it.contains("n") })

        // no completions
        configureByCode("PUSH abcde<caret>;")
        assertCompletions() // no completions here

        configureByCode("PUSH int 123<caret>;")
        assertCompletions()

        configureByCode("PUSH 123<caret>;")
        assertCompletions()

        configureByCode("PUSH (pair int int) (Pair 123<caret>);")
        assertCompletions()

        configureByCode("PUSH (Pair <caret>)")
        assertCompletionsNoneOf(*comparableTypes)

        configureByCode("NIL <caret>")
        assertCompletionsAtLeast(*simpleTypes)
        // fixme assert that tags are are not shown for instructions which don't support them

        configure("parameter (pair <caret>)")
        assertCompletionsAtLeast(*simpleTypes)

        configure("parameter <caret>")
        assertCompletionsAtLeast(*simpleTypes)

        configure("code <caret>")
        assertCompletionsNoneOf(*simpleTypes)
        // configureByCode("PUSH (Pair int <caret>)")
        // assertCompletionsNoneOf(*reference)
    }
}
package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionType
import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonTypeCompletionTest : MichelsonCompletionTest() {
    val comparableTypes = MichelsonLanguage.TYPES.filter { it.isComparable }.map { it.name }.toTypedArray()
    val simpleTypes = MichelsonLanguage.TYPES.filter { it.subtypes.isEmpty() }.map { it.name }.toTypedArray()

    val referenceComplex = MichelsonLanguage.TYPES.filter { it.subtypes.isNotEmpty() }.map { it.name }.toTypedArray()
    val referenceComparable = MichelsonLanguage.TYPES.filter { it.isComparable }.map { it.name }.toTypedArray()
    val allComplex = referenceComplex + referenceComparable

    fun testBasicSimpleTypeCompletion() {
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

    fun testSmartSimpleTypeCompletion() {
        configureByCode("PUSH <caret>")
        assertCompletions(*simpleTypes, type = CompletionType.SMART)
    }

    fun testBasicNestedTypeCompletion() {

        // basic
        configureByCode("PUSH <caret>")
        assertCompletionsNoneOf(*referenceComplex)

        configureByCode("PUSH (<caret>)")
        assertCompletionsAtLeast(*allComplex)

        configureByCode("MAP string (<caret>)")
        assertCompletionsAtLeast(*allComplex)

        // no completions
        configureByCode("PUSH (P<caret>)")
        assertCompletionsAtLeast(*allComplex.filter { it.contains("P") }.toTypedArray())

        configureByCode("PUSH (Pair (P<caret>) int)")
        assertCompletionsAtLeast(*allComplex.filter { it.contains("P") }.toTypedArray())

        configureByCode("PUSH (Pair int (P<caret>))")
        assertCompletionsAtLeast(*allComplex.filter { it.contains("P") }.toTypedArray())

        configureByCode("MAP (Pair (<caret>))")
        assertCompletionsNoneOf(*allComplex)

        // configureByCode("MAP (Pair int (<caret>))")
        // assertCompletionsNoneOf(*all)
    }
}
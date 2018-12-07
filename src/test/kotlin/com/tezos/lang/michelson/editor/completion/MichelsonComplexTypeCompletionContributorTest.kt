package com.tezos.lang.michelson.editor.completion

import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonComplexTypeCompletionContributorTest : MichelsonCompletionTest() {
    fun testCompletion() {
        val referenceComplex = MichelsonLanguage.TYPES.filter { it.subtypes.isNotEmpty() }.map { it.name }.toTypedArray()
        val referenceComparable = MichelsonLanguage.TYPES.filter { it.isComparable }.map { it.name }.toTypedArray()
        val all = referenceComplex + referenceComparable

        // basic
        configureByCode("PUSH <caret>")
        assertCompletionsNoneOf(*referenceComplex)

        configureByCode("PUSH (<caret>)")
        assertCompletionsAtLeast(*all)

        configureByCode("MAP string (<caret>)")
        assertCompletionsAtLeast(*all)

        // no completions
        configureByCode("PUSH (P<caret>)")
        assertCompletionsAtLeast(*all.filter { it.contains("P") }.toTypedArray())

        configureByCode("PUSH (Pair (P<caret>) int)")
        assertCompletionsAtLeast(*all.filter { it.contains("P") }.toTypedArray())

        configureByCode("PUSH (Pair int (P<caret>))")
        assertCompletionsAtLeast(*all.filter { it.contains("P") }.toTypedArray())

        configureByCode("MAP (Pair (<caret>))")
        assertCompletionsNoneOf(*all)

        // configureByCode("MAP (Pair int (<caret>))")
        // assertCompletionsNoneOf(*all)
    }
}
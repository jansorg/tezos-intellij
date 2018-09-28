package com.tezos.lang.michelson.editor.completion

import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * @author jansorg
 */
class MichelsonTypeCompletionContributorTest:MichelsonCompletionTest(){
    fun testCompletion() {
        val referenceComplex = MichelsonLanguage.COMPLEX_TYPES.toTypedArray()
        val referenceComparable = MichelsonLanguage.COMPARABLE_TYPES.toTypedArray()
        val all = referenceComplex + referenceComparable

        // basic
        configureByCode("PUSH <caret>")
        assertCompletionsNoneOf(*referenceComplex)

        configureByCode("PUSH (<caret>)")
        assertCompletions(*all)
        configureByCode("MAP string (<caret>)")
        assertCompletions(*all)
        configureByCode("MAP (Pair (<caret>))")
        assertCompletions(*all)
        configureByCode("MAP (Pair int (<caret>))")
        assertCompletions(*all)

        configureByCode("PUSH (P<caret>)")
        assertCompletions(*all.filter { it.contains("P") }.toTypedArray())
        configureByCode("PUSH (Pair (P<caret>) int)")
        assertCompletions(*all.filter { it.contains("P") }.toTypedArray())
        configureByCode("PUSH (Pair int (P<caret>))")
        assertCompletions(*all.filter { it.contains("P") }.toTypedArray())
    }
}
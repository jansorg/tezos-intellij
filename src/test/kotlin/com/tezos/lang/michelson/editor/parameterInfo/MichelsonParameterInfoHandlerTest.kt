package com.tezos.lang.michelson.editor.parameterInfo

import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonParameterInfoHandlerTest : AbstractMichelsonInstructionParameterInfoTest() {
    fun testInstructions() {
        assertParamInfo("DR<caret>OP", "DROP")
        assertParamInfo("DROP<caret>", "DROP")
        assertParamInfo("<caret>DROP", "DROP")

        assertParamInfo("<caret>PUSH int 123", "PUSH <type> <data>")
    }

    fun testTags() {
        assertParamInfo("PUSH bool Tru<caret>e", "PUSH <type> <data>")
        assertParamInfo("PUSH bool True<caret>", "PUSH <type> <data>")
        assertParamInfo("PUSH bool <caret>True", "PUSH <type> <data>")

        assertParamInfo("PUSH (pair int int) (Pa<caret>ir 123 234)", "Pair <data> <data>")
        assertParamInfo("PUSH (pair int int) (Pair<caret> 123 234)", "Pair <data> <data>")
        assertParamInfo("PUSH (pair int int) (<caret>Pair 123 234)", "Pair <data> <data>")
    }

    fun testTypes() {
        assertParamInfo("PUSH in<caret>t 123", "PUSH <type> <data>")

        assertParamInfo("PUSH (p<caret>air int bool) (Pair 123 True)", "pair <type> <type>")
        assertParamInfo("PUSH (pair<caret> int bool) (Pair 123 True)", "pair <type> <type>")
        assertParamInfo("PUSH (<caret>pair int bool) (Pair 123 True)", "pair <type> <type>")

        assertParamInfo("PUSH (pair i<caret>nt bool) (Pair 123 True)", "pair <type> <type>")
        assertParamInfo("PUSH (pair int<caret> bool) (Pair 123 True)", "pair <type> <type>")
        assertParamInfo("PUSH (pair <caret>int bool) (Pair 123 True)", "pair <type> <type>")

        assertParamInfo("PUSH (pair int b<caret>ool) (Pair 123 True)", "pair <type> <type>")
        assertParamInfo("PUSH (pair int <caret>bool) (Pair 123 True)", "pair <type> <type>")
        assertParamInfo("PUSH (pair int bool<caret>) (Pair 123 True)", "pair <type> <type>")
    }

    private fun assertParamInfo(code: String, expectedRendering: String) {
        configureByCode(code)
        val info = requestParamInfo(myFixture.caretOffset)
        Assert.assertEquals(expectedRendering, info.text)
    }

}
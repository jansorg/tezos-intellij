package com.tezos.lang.michelson.editor.parameterInfo

import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonInstructionParameterInfoTest : AbstractMichelsonInstructionParameterInfoTest() {
    fun testParams() {
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
    }

    private fun assertParamInfo(code: String, expectedRendering: String) {
        configureByCode(code)
        val info = requestParamInfo(myFixture.caretOffset)
        Assert.assertEquals(expectedRendering, info.text)
    }

}
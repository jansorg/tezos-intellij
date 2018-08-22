package com.tezos.lang.michelson.lexer

import org.junit.Test

/**
 * @author jansorg
 */
class MichelsonLexerTest {
    @Test
    fun identity() {
        TestUtil.assertLexing("identity.tz")
    }

    @Test
    fun forward() {
        TestUtil.assertLexing("forward.tz")
    }
}
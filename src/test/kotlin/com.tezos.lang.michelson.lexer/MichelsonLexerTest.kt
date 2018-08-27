package com.tezos.lang.michelson.lexer

import com.tezos.lang.michelson.MichelsonTestUtils
import org.junit.Test
import java.nio.file.Paths

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
package com.tezos.lang.michelson.parser

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author jansorg
 */
class PairMacroMetadataTest {
    @Test
    fun basics() {
        val m = PairMacroMetadata()

        assertEquals(0, m.requiredBlocks())

        assertValid(m, "PAIR")
        assertValid(m, "PAPAIR")
        assertValid(m, "PAPPAIIR")

        assertInvalid(m, "PIAR", 1)
        assertInvalid(m, "PAAR", 2)
        assertInvalid(m, "PAIAR", 3)
        assertInvalid(m, "PAIAR", 3)
        assertInvalid(m, "PAIPR", 4)
    }
}

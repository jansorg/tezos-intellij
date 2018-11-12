package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lang.macro.UnpairMacroMetadata
import org.junit.Assert.*
import org.junit.Test

/**
 * @author jansorg
 */
class UnpairMacroMetadataTest {
    @Test
    fun basics() {
        val m = UnpairMacroMetadata()

        assertEquals(0, m.requiredBlocks())

        assertValid(m, "UNPAIR")
        assertValid(m, "UNPAPAIR")
        assertValid(m, "UNPAPPAIIR")

        assertInvalid(m, "UNPIAR", 3)
        assertInvalid(m, "UNPAAR", 4)
        assertInvalid(m, "UNPAIAR", 5)
        assertInvalid(m, "UNPAIAR", 5)
        assertInvalid(m, "UNPAIPR", 6)
    }

    @Test
    fun expand() {
        val m = UnpairMacroMetadata()

        assertEquals("DUP; CAR; DIP{CDR}", m.expand("UNPAIR"))

        // stack ((a b) c) -> a, b, c
        // UNPPAIIR
        // -> UNPAIR; UNPAIR
        // -> DUP; CAR; DIP { CDR }; DUP; CAR; DIP { CDR }
        assertEquals("DUP; CAR; DIP{CDR}; DUP; CAR; DIP{CDR}", m.expand("UNPPAIIR"))

        // stack ((a b) ((c d) e)) -> a, b, c, d, e
        // UNP PAI PPAII R
        // -> UNPPAIPPAIIR
        // -> UNPAIR; UNP AI PPAII R
        // -> UNPAIR; UNPAI PPAII R
        assertEquals("", m.expand("UNPPAIPPAIIR"))
    }
}
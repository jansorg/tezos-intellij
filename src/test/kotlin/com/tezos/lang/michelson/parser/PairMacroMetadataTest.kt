package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lang.macro.PairMacroMetadata
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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

    @Test
    fun expand() {
        val m = PairMacroMetadata()

        assertNull(m.expand("PAIR"))

        // stack a,b,c -> (a (b,c))
        assertEquals("DIP{ PAIR }; PAIR", m.expand("PAPAIR"))

        // stack a,b,c -> ((a,b) c)
        assertEquals("PAIR; PAIR", m.expand("PPAIIR"))

        // PAPPAIIR -> DIP { PPAIIR }; PAIR
        // PPAIIR -> PAIR; PAIR
        // stack a,b,c,d -> (a ((b,c) d))
        assertEquals("DIP{ PAIR; PAIR }; PAIR", m.expand("PAPPAIIR"))

        assertEquals("PAIR; DIP{ PAIR }; PAIR", m.expand("PPAIPAIR"))
    }
}

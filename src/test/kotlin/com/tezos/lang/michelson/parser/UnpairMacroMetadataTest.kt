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
}
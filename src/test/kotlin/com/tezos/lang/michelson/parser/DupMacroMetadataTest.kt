package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lang.macro.DupMacroMetadata
import com.tezos.lang.michelson.lang.AnnotationType
import org.junit.Assert.*
import org.junit.Test

/**
 * @author jansorg
 */
class DupMacroMetadataTest {
    @Test
    fun basics() {
        val m = DupMacroMetadata()

        assertNull(m.validate("DUUP"))
        assertNull(m.validate("DUUUUP"))

        assertNotNull("expected validation error", m.validate("DUuuuP"))
        assertNotNull("expected validation error", m.validate("DIIP"))

        assertEquals(0, m.requiredBlocks())

        assertEquals(1, m.supportedAnnotations(AnnotationType.VARIABLE, "DUUP"))
        assertEquals(0, m.supportedAnnotations(AnnotationType.TYPE, "DUUP"))
        assertEquals(0, m.supportedAnnotations(AnnotationType.FIELD, "DUUP"))
    }

    @Test
    fun expand() {
        val m = DupMacroMetadata()
        assertNull(m.expand("DUP"))
        assertEquals("DIP{DUP}; SWAP", m.expand("DUUP"))
        assertEquals("DIP{DIP{DIP{DUP}; SWAP}; SWAP}; SWAP", m.expand("DUUUUP"))
    }
}
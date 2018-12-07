package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lang.macro.DipMacroMetadata
import com.tezos.lang.michelson.lang.AnnotationType
import org.junit.Assert.*
import org.junit.Test

/**
 * @author jansorg
 */
class DipMacroMetadataTest {
    @Test
    fun basics() {
        val m = DipMacroMetadata()

        assertNull(m.validate("DIIP"))
        assertNull(m.validate("DIIIIIP"))

        assertNotNull("expected validation error", m.validate("DIiiiP"))
        assertNotNull("expected validation error", m.validate("DUUP"))

        assertEquals(0, m.supportedAnnotations(AnnotationType.VARIABLE, "DIIP"))
        assertEquals(0, m.supportedAnnotations(AnnotationType.TYPE, "DIIP"))
        assertEquals(0, m.supportedAnnotations(AnnotationType.FIELD, "DIIP"))
    }

    @Test
    fun expand() {
        val m = DipMacroMetadata()
        assertNull(m.expand("DIP"))

        assertEquals("DIP{DIP{}}", m.expand("DIIP"))
        assertEquals("DIP{DIP{DIP{DIP{DIP{DIP{}}}}}}", m.expand("DIIIIIIP"))
    }
}
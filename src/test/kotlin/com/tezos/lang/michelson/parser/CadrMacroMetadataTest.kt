package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lang.macro.CadrMacroMetadata
import com.tezos.lang.michelson.lang.AnnotationType
import org.junit.Assert.*
import org.junit.Test

/**
 * @author jansorg
 */
class CadrMacroMetadataTest{
    @Test
    fun basics() {
        val m = CadrMacroMetadata()
        assertEquals(0, m.requiredBlocks())

        assertNull(m.validate("CADR"))
        assertNull(m.validate("CAADDR"))

        assertNotNull(m.validate("CR"))
        assertNotNull(m.validate("CADRR"))

        assertEquals(1, m.supportedAnnotations(AnnotationType.VARIABLE, "CADR"))
        assertEquals(1, m.supportedAnnotations(AnnotationType.VARIABLE, "CAADDR"))

        assertEquals(1, m.supportedAnnotations(AnnotationType.FIELD, "CADR"))
        assertEquals(1, m.supportedAnnotations(AnnotationType.FIELD, "CAADDR"))

        assertEquals(0, m.supportedAnnotations(AnnotationType.TYPE, "CADR"))
        assertEquals(0, m.supportedAnnotations(AnnotationType.TYPE, "CAADDR"))
    }

    @Test
    fun expand() {
        val m = CadrMacroMetadata()
        assertEquals(null, m.expand("CAR"))
        assertEquals(null, m.expand("CDR"))

        assertEquals("CAR; CDR", m.expand("CADR"))
        assertEquals("CAR; CAR; CAR", m.expand("CAAAR"))
        assertEquals("CAR; CDR; CAR", m.expand("CADAR"))
    }
}
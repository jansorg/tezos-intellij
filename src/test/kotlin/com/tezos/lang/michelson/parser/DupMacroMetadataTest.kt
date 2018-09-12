package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.psi.PsiAnnotationType
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

        assertEquals(1, m.supportedAnnotations(PsiAnnotationType.VARIABLE, "DUUP"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.TYPE, "DUUP"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.FIELD, "DUUP"))
    }
}
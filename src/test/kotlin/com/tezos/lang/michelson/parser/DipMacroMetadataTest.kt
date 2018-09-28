package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lang.macro.DipMacroMetadata
import com.tezos.lang.michelson.psi.PsiAnnotationType
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

        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.VARIABLE, "DIIP"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.TYPE, "DIIP"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.FIELD, "DIIP"))
    }
}
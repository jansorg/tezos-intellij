package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lang.macro.CadrMacroMetadata
import com.tezos.lang.michelson.psi.PsiAnnotationType
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

        assertEquals(1, m.supportedAnnotations(PsiAnnotationType.VARIABLE, "CADR"))
        assertEquals(1, m.supportedAnnotations(PsiAnnotationType.VARIABLE, "CAADDR"))

        assertEquals(1, m.supportedAnnotations(PsiAnnotationType.FIELD, "CADR"))
        assertEquals(1, m.supportedAnnotations(PsiAnnotationType.FIELD, "CAADDR"))

        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.TYPE, "CADR"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.TYPE, "CAADDR"))
    }
}
package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.parser.macro.SetCadrMacroMetadata
import com.tezos.lang.michelson.psi.PsiAnnotationType
import org.junit.Assert.*
import org.junit.Test

/**
 * @author jansorg
 */
class SetCadrMacroMetadataTest {
    @Test
    fun basics() {
        val m = SetCadrMacroMetadata()
        assertNull(m.validate("SET_CAR"))
        assertNull(m.validate("SET_CDR"))
        assertNull(m.validate("SET_CADR"))

        assertNotNull(m.validate("SET_CR"))
        assertNotNull(m.validate("SET_CAADRR"))

        assertEquals(0, m.requiredBlocks())

        assertEquals(1, m.supportedAnnotations(PsiAnnotationType.VARIABLE, "SET_CAR"))
        assertEquals(1, m.supportedAnnotations(PsiAnnotationType.FIELD, "SET_CAR"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.TYPE, "SET_CAR"))

        assertEquals(1, m.supportedAnnotations(PsiAnnotationType.VARIABLE, "SET_CAAADDR"))
        assertEquals(1, m.supportedAnnotations(PsiAnnotationType.FIELD, "SET_CAAADDR"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.TYPE, "SET_CAAADDR"))
    }
}
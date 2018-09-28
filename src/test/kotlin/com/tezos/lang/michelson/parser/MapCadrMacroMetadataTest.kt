package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lang.macro.MapCadrMacroMetadata
import com.tezos.lang.michelson.psi.PsiAnnotationType
import org.junit.Assert.*
import org.junit.Test

/**
 * @author jansorg
 */
class MapCadrMacroMetadataTest {
    @Test
    fun basics() {
        val m = MapCadrMacroMetadata()
        assertNull(m.validate("MAP_CAR"))
        assertNull(m.validate("MAP_CDR"))
        assertNull(m.validate("MAP_CADR"))

        assertNotNull(m.validate("MAP_CR"))
        assertNotNull(m.validate("MAP_CAADRR"))

        assertEquals(1, m.requiredBlocks())

        assertEquals(1, m.supportedAnnotations(PsiAnnotationType.VARIABLE, "MAP_CAR"))
        assertEquals(1, m.supportedAnnotations(PsiAnnotationType.FIELD, "MAP_CAR"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.TYPE, "MAP_CAR"))

        assertEquals(1, m.supportedAnnotations(PsiAnnotationType.VARIABLE, "MAP_CAAADDR"))
        assertEquals(1, m.supportedAnnotations(PsiAnnotationType.FIELD, "MAP_CAAADDR"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.TYPE, "MAP_CAAADDR"))
    }
}
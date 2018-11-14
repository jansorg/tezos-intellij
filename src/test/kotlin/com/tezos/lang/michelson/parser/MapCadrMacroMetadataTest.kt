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

    @Test
    fun expand() {
        val m = MapCadrMacroMetadata()

        assertEquals("DUP; CDR; DIP{ CAR; <em>CODE</em> }; SWAP; PAIR", m.expand("MAP_CAR"))
        assertEquals("DUP; CDR; <em>CODE</em>; SWAP; CAR; PAIR", m.expand("MAP_CDR"))

        // MAP_CADR -> MAP_CDR
        // { DUP ; DIP { CAR ; MAP_CDR } ; CDR ; SWAP ; PAIR }
        // { DUP ; DIP { CAR ; DUP ; CDR ; code ; SWAP ; CAR ; PAIR } ; CDR ; SWAP ; PAIR }
        assertEquals("{ DUP; DIP{ CAR; DUP; CDR; <em>CODE</em>; SWAP; CAR; PAIR }; CDR; SWAP; PAIR }", m.expand("MAP_CADR"))

        // MAP_CDDAR -> MAP_CDAR -> MAP_CAR
        // { DUP ; DIP { CDR ; MAP_CDAR } ; CAR ; PAIR }
        // { DUP ; DIP { CDR ;  { DUP ; DIP { CDR ; MAP_CAR } ; CAR ; PAIR } } ; CAR ; PAIR }
        // { DUP ; DIP { CDR ;  { DUP ; DIP { CDR ; DUP ; CDR ; DIP { CAR ; code } ; SWAP ; PAIR } ; CAR ; PAIR } } ; CAR ; PAIR }
        assertEquals("{ DUP; DIP{ CDR; { DUP; DIP{ CDR; DUP; CDR; DIP{ CAR; <em>CODE</em> }; SWAP; PAIR }; CAR; PAIR } }; CAR; PAIR }", m.expand("MAP_CDDAR"))
    }
}
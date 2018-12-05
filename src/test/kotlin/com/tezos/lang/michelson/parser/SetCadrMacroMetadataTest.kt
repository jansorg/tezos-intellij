package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lang.macro.SetCadrMacroMetadata
import com.tezos.lang.michelson.lang.PsiAnnotationType
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

    @Test
    fun expand() {
        val m = SetCadrMacroMetadata()
        assertEquals("CDR; SWAP; PAIR", m.expand("SET_CAR"))
        assertEquals("CAR; PAIR", m.expand("SET_CDR"))

        assertEquals("{ DUP; DIP{ CAR; CAR; PAIR }; CDR; SWAP; PAIR }", m.expand("SET_CADR"))

        // SET_CADAAR -> SET_CADAAR -> SET_CDAAR -> SET_CAAR -> SET_CAR
        // SET_CADAAR
        // -> { DUP ; DIP { CAR ; SET_CADAAR } ; CDR ; SWAP ; PAIR }
        // -> { DUP ; DIP { CAR ; { DUP ; DIP { CDR ; SET_CAAR } ; CAR ; PAIR }  } ; CDR ; SWAP ; PAIR }
        // -> { DUP ; DIP { CAR ; { DUP ; DIP { CDR ; { DUP ; DIP { CAR ; SET_CAR } ; CDR ; SWAP ; PAIR } } ; CAR ; PAIR }  } ; CDR ; SWAP ; PAIR }
        // -> { DUP ; DIP { CAR ; { DUP ; DIP { CDR ; { DUP ; DIP { CAR ; CDR; SWAP; PAIR } ; CDR ; SWAP ; PAIR } } ; CAR ; PAIR }  } ; CDR ; SWAP ; PAIR }
        val replacement = "{ DUP; DIP{ CAR; { DUP; DIP{ CDR; { DUP; DIP{ CAR; CDR; SWAP; PAIR }; CDR; SWAP; PAIR } }; CAR; PAIR } }; CDR; SWAP; PAIR }"
        assertEquals(replacement, m.expand("SET_CADAAR"))
    }
}
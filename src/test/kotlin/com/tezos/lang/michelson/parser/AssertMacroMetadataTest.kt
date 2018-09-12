package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.psi.PsiAnnotationType
import org.junit.Assert.*
import org.junit.Test

/**
 * @author jansorg
 */
class AssertMacroMetadataTest {
    @Test
    fun basics() {
        val m = AssertMacroMetadata()
        for (n in AssertMacroMetadata.NAMES) {
            assertNull(m.validate(n))
        }

        assertNotNull(m.validate("IF"))
        assertNotNull(m.validate("IFNOTHERE"))
        assertNotNull(m.validate("IFeq"))

        assertEquals(0, m.requiredBlocks())

        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.FIELD, "IFEQ"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.VARIABLE, "IFEQ"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.TYPE, "IFEQ"))
    }
}
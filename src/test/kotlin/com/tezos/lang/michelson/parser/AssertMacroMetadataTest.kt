package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lang.macro.AssertMacroMetadata
import com.tezos.lang.michelson.lang.PsiAnnotationType
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

    @Test
    fun expansion() {
        val m = AssertMacroMetadata()
        for (n in AssertMacroMetadata.NAMES) {
            val short = m.expand(n, false)
            assertNotNull(short)

            val long = m.expand(n, true)
            assertNotNull(long)

            assertNotEquals(short, long)
        }
    }
}
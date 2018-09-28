package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lang.macro.ConditionalMacroMetadata
import com.tezos.lang.michelson.psi.PsiAnnotationType
import org.junit.Assert.*
import org.junit.Test

/**
 * @author jansorg
 */
class ConditionalMacroMetadataTest {
    @Test
    fun basics() {
        val m = ConditionalMacroMetadata()
        for (n in ConditionalMacroMetadata.NAMES) {
            m.validate(n)
        }

        assertNotNull(m.validate("IF"))
        assertNotNull(m.validate("IFNOTHERE"))
        assertNotNull(m.validate("IFeq"))

        assertEquals(2, m.requiredBlocks())

        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.FIELD, "IFEQ"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.VARIABLE, "IFEQ"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.TYPE, "IFEQ"))
    }
}
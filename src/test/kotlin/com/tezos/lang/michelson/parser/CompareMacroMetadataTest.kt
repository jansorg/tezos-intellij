package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.parser.macro.CompareMacroMetadata
import com.tezos.lang.michelson.psi.PsiAnnotationType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * @author jansorg
 */
class CompareMacroMetadataTest {
    @Test
    fun basics() {
        val m = CompareMacroMetadata()
        for (n in CompareMacroMetadata.NAMES) {
            m.validate(n)
        }

        assertNotNull(m.validate("CMP"))
        assertNotNull(m.validate("CMPNOTHERE"))
        assertNotNull(m.validate("CMPeq"))

        assertEquals(0, m.requiredBlocks())

        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.FIELD, "IFEQ"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.VARIABLE, "IFEQ"))
        assertEquals(0, m.supportedAnnotations(PsiAnnotationType.TYPE, "IFEQ"))
    }
}
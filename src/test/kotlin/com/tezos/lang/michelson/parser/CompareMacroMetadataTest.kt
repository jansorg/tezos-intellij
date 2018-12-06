package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lang.macro.CompareMacroMetadata
import com.tezos.lang.michelson.lang.AnnotationType
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

        assertEquals(0, m.supportedAnnotations(AnnotationType.FIELD, "IFEQ"))
        assertEquals(0, m.supportedAnnotations(AnnotationType.VARIABLE, "IFEQ"))
        assertEquals(0, m.supportedAnnotations(AnnotationType.TYPE, "IFEQ"))
    }
}
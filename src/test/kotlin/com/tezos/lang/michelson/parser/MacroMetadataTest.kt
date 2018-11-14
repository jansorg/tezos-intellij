package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lang.MichelsonLanguage
import org.junit.Assert.assertFalse
import org.junit.Test

/**
 * @author jansorg
 */
class MacroMetadataTest {
    private val missingExpansions = setOf("IF_SOME", "DUP", "DIP", "CAR", "CDR", "PAIR")

    @Test
    fun replacements() {
        MichelsonLanguage.MACRO_NAMES.forEach { name ->
            assertContent(name)
        }

        listOf("DIIP", "DUUP", "CAAR", "CADR", "CDAR").forEach { name ->
            assertContent(name)
        }
    }

    private fun assertContent(name: String) {
        val m = MichelsonLanguage.MACROS.first { it.validate(name) == null }
        assertFalse("macro documentation must not be reported as empty by ${m.javaClass.simpleName} for $name", m.helpContentFile(name).isNullOrEmpty())

        if (name !in missingExpansions) {
            assertFalse("macro expansion must not be reported as empty by ${m.javaClass.simpleName} for $name", m.expand(name).isNullOrEmpty())
        }
    }
}
package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.parser.macro.MacroMetadata
import org.junit.Assert.assertEquals
import org.junit.Assert.fail

fun assertValid(m: MacroMetadata, macro: String) {
    val error = m.validate(macro)
    if (error != null) {
        fail("Unexpected error for macro $macro: '${error.first}' at index ${error.second}")
    }
}

fun assertInvalid(m: MacroMetadata, macro: String, errorIndex: Int? = null) {
    val error = m.validate(macro)
    if (error == null) {
        if (errorIndex != null) {
            assertEquals("Expected error at index $errorIndex", errorIndex)
        } else {
            fail("Expected error for macro $macro")
        }
    }
}
package com.tezos.client.stack

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author jansorg
 */
class MichelsonStackTypeTest{
    @Test
    fun render() {
        // nested rendering
        var type = MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("nat")), listOf(MichelsonStackAnnotation(":x")))
        assertEquals("(pair int nat)", type.asString(true))

        type = MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("nat")), emptyList())
        assertEquals("(pair int nat)", type.asString(true))

        type = MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("nat")), listOf(MichelsonStackAnnotation(":x")))), listOf(MichelsonStackAnnotation(":x")))
        assertEquals("(pair int (pair int nat))", type.asString(true))

        type = MichelsonStackType("", listOf(MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("nat")), listOf(MichelsonStackAnnotation(":x")))), listOf(MichelsonStackAnnotation(":x")))
        assertEquals("(pair int nat)", type.asString(true))

        // no nested rendering
        type = MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("nat")), listOf(MichelsonStackAnnotation(":x")))), listOf(MichelsonStackAnnotation(":x")))
        assertEquals("pair", type.asString(false))
    }
}
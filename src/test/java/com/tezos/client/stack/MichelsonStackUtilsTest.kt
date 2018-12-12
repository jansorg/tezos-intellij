package com.tezos.client.stack

import com.tezos.client.stack.MichelsonStackUtils.generateSampleList
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author jansorg
 */
class MichelsonStackUtilsTest {
    @Test
    fun samples() {
        // pair int nat
        var samples = generateSampleList(MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("nat"))))
        assertEquals(listOf("(Pair 42 42)"), samples)

        // pair int (pair int nat)
        samples = generateSampleList(MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("nat"))))))
        assertEquals(listOf("(Pair 42 (Pair 42 42))"), samples)

        // pair int (pair (or nat string) nat)
        samples = generateSampleList(MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("pair", listOf(MichelsonStackType("or", listOf(MichelsonStackType("nat"), MichelsonStackType("string"))), MichelsonStackType("nat"))))))
        assertEquals(listOf("(Pair 42 (Pair (Left 42) 42))", "(Pair 42 (Pair (Right \"foo\") 42))"), samples)

        // or int (pair int nat)
        samples = generateSampleList(MichelsonStackType("or", listOf(MichelsonStackType("int"), MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("nat"))))))
        assertEquals(listOf("(Left 42)", "(Right (Pair 42 42))"), samples)

        // or (pair int nat) (or int (pair int nat))
        samples = generateSampleList(MichelsonStackType("or", listOf(MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("nat"))), MichelsonStackType("or", listOf(MichelsonStackType("int"), MichelsonStackType("pair", listOf(MichelsonStackType("int"), MichelsonStackType("nat"))))))))
        assertEquals(listOf("(Left (Pair 42 42))", "(Right (Left 42))", "(Right (Right (Pair 42 42)))"), samples)

        // map int (or int nat))
        samples = generateSampleList(MichelsonStackType("map", listOf(MichelsonStackType("int"), MichelsonStackType("or", listOf(MichelsonStackType("int"), MichelsonStackType("nat"))))))
        assertEquals(listOf("{ Elt 42 (Left 42); }", "{ Elt 42 (Right 42); }"), samples)

        // map nat (or (option int) string))
        samples = generateSampleList(MichelsonStackType("map", listOf(MichelsonStackType("nat"), MichelsonStackType("or", listOf(MichelsonStackType("option", listOf(MichelsonStackType("int"))), MichelsonStackType("string"))))))
        assertEquals(listOf("{ Elt 42 (Left None); }", "{ Elt 42 (Left (Some 42)); }", "{ Elt 42 (Right \"foo\"); }"), samples)

        // big_map int (or int nat))
        samples = generateSampleList(MichelsonStackType("big_map", listOf(MichelsonStackType("int"), MichelsonStackType("or", listOf(MichelsonStackType("int"), MichelsonStackType("nat"))))))
        assertEquals(listOf("{ Elt 42 (Left 42); }", "{ Elt 42 (Right 42); }"), samples)

        // big_map nat (or (option int) string))
        samples = generateSampleList(MichelsonStackType("big_map", listOf(MichelsonStackType("nat"), MichelsonStackType("or", listOf(MichelsonStackType("option", listOf(MichelsonStackType("int"))), MichelsonStackType("string"))))))
        assertEquals(listOf("{ Elt 42 (Left None); }", "{ Elt 42 (Left (Some 42)); }", "{ Elt 42 (Right \"foo\"); }"), samples)

        // list (or (option int) string))
        samples = generateSampleList(MichelsonStackType("list", listOf(MichelsonStackType("or", listOf(MichelsonStackType("option", listOf(MichelsonStackType("int"))), MichelsonStackType("string"))))))
        assertEquals(listOf("{}", "{ (Left None); }", "{ (Left (Some 42)); }", "{ (Right \"foo\"); }"), samples)

        // set (or (option int) string))
        samples = generateSampleList(MichelsonStackType("set", listOf(MichelsonStackType("or", listOf(MichelsonStackType("option", listOf(MichelsonStackType("int"))), MichelsonStackType("string"))))))
        assertEquals(listOf("{}", "{ (Left None); }", "{ (Left (Some 42)); }", "{ (Right \"foo\"); }"), samples)
    }
}
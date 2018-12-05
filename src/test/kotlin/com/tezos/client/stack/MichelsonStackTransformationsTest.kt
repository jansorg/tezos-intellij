package com.tezos.client.stack

import org.junit.Assert
import org.junit.Test

/**
 * @author jansorg
 */
class MichelsonStackTransformationsTest {
    @Test
    fun testNestedTypes() {
        // make sure that type information is only returned for nested types
        val stack = MichelsonStackTransformations(listOf(
                MichelsonStackTransformation(0, 100, MichelsonStack.EMPTY, MichelsonStack.EMPTY),
                MichelsonStackTransformation(0, 10, MichelsonStack.EMPTY, MichelsonStack.EMPTY),
                MichelsonStackTransformation(4, 10, MichelsonStack.EMPTY, MichelsonStack.EMPTY),
                MichelsonStackTransformation(50, 100, MichelsonStack.EMPTY, MichelsonStack.EMPTY)
        ), emptyList())

        Assert.assertEquals(4, stack.elementAt(4)?.tokenStartOffset)
        Assert.assertEquals(4, stack.elementAt(5)?.tokenStartOffset)
        Assert.assertEquals(4, stack.elementAt(10)?.tokenStartOffset)

        Assert.assertEquals(100, stack.elementAt(11)?.tokenEndOffset)
        Assert.assertEquals(100, stack.elementAt(50)?.tokenEndOffset)
        Assert.assertEquals(100, stack.elementAt(99)?.tokenEndOffset)
        Assert.assertEquals(100, stack.elementAt(100)?.tokenEndOffset)
    }
}
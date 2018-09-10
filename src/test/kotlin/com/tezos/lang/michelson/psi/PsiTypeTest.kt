package com.tezos.lang.michelson.psi

import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class PsiTypeTest : MichelsonFixtureTest() {
    fun testFindParentType() {
        val (_, current) = configureByCode("PUSH (pair (timestamp<caret> %T) (mutez %N))")

        val currentType = current.firstParent<PsiType>()
        Assert.assertNotNull("expected a PsiType at the current offset", currentType)

        val parent = currentType!!.findComposedParentType()
        Assert.assertNotNull(parent)
        Assert.assertEquals("pair", parent!!.typeNameString)
    }

    fun testFindChildrenType() {
        val (_, current) = configureByCode("PUSH (pair<caret> (timestamp %T) (pair %N mutez string))")

        val currentType = current.firstParent<PsiType>()
        Assert.assertNotNull("expected a PsiType at the current offset", currentType)

        var children = currentType!!.findChildrenTypes()
        Assert.assertEquals("expected nested types", 2, children.size)
        Assert.assertEquals("expected nested types", 2, children.filter({ it is PsiType }).size)
        Assert.assertEquals("timestamp", children[0].typeNameString)
        Assert.assertEquals("pair", children[1].typeNameString)

        // nested types must also support findChildrenTypes when they're containing another level of types
        children = children[1].findChildrenTypes()
        Assert.assertEquals("expected nested types", 2, children.size)
        Assert.assertEquals("mutez", children[0].typeNameString)
        Assert.assertEquals("string", children[1].typeNameString)
    }
}
package com.tezos.lang.michelson.structureView

import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonStructureViewModelTest : MichelsonFixtureTest() {
    fun testStrucutureView() {
        configureByCode("CDR; NIL operation; PAIR;")
        myFixture.testStructureView { view ->
            val tree = view.treeStructure

            val root = tree.rootElement
            Assert.assertEquals("file", root.toString())

            val contracts = tree.getChildElements(root)
            Assert.assertEquals(1, contracts.size)

            val contract = contracts[0]
            Assert.assertEquals("Contract", contract.toString())

            val sections = tree.getChildElements(contract)
            Assert.assertEquals(3, sections.size)
            Assert.assertEquals("Section parameter", sections[0].toString())
            Assert.assertEquals("Section storage", sections[1].toString())
            Assert.assertEquals("Section code", sections[2].toString())
        }
    }
}
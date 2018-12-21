package com.tezos.lang.michelson.psi

import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class PsiSectionTest : MichelsonFixtureTest() {
    fun testSectionType() {
        val file = myFixture.configureByText("test.tz", """
            parameter (pair int (pair int string));
            storage (pair int string);
            code {}
        """.trimIndent())

        val contract = (file as MichelsonPsiFile).getContract()!!

        val paramSection = contract.findParameterSection()
        Assert.assertNotNull(paramSection)
        Assert.assertNotNull(paramSection!!.type)
        Assert.assertEquals("(pair int (pair int string))", paramSection.type.text)
        Assert.assertEquals("pair", paramSection.type.asStackType().asString(false))
        Assert.assertEquals("(pair int (pair int string))", paramSection.type.asStackType().asString(true))

        val storageSection = contract.findStorageSection()
        Assert.assertNotNull(storageSection)
        Assert.assertNotNull(storageSection!!.type)
        Assert.assertEquals("(pair int string)", storageSection.type.text)
        Assert.assertEquals("pair", storageSection.type.asStackType().asString(false))
        Assert.assertEquals("(pair int string)", storageSection.type.asStackType().asString(true))

        val codeSection = contract.findCodeSection()
        Assert.assertNotNull(codeSection)
    }
}
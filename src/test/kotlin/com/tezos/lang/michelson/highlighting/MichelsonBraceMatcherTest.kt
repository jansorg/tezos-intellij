package com.tezos.lang.michelson.highlighting

import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonBraceMatcherTest : MichelsonFixtureTest() {
    fun testcodeStart() {
        val matcher = MichelsonBraceMatcher()

        assertStartElement(matcher, "IF <caret>{} {}", "IF")
        assertStartElement(matcher, "IF {} <caret>{}", "IF")
        assertStartElement(matcher, "IF {<caret>} {}", "IF")
        assertStartElement(matcher, "IF {} {<caret>}", "IF")
        assertStartElement(matcher, "IF_EMPTY {<caret>} {}", "IF_EMPTY")
        assertStartElement(matcher, "DIP <caret>{}", "DIP")
        assertStartElement(matcher, "DIIIP <caret>{}", "DIIIP")

        assertStartElement(matcher, "PUSH (pair <caret>(int string)) (Pair 123 234)", "PUSH")
        assertStartElement(matcher, "PUSH (pair (<caret>int string)) (Pair 123 234)", "PUSH")
        assertStartElement(matcher, "PUSH (pair (int string)) <caret>(Pair 123 234)", "PUSH")
        assertStartElement(matcher, "PUSH <caret>(pair (int string)) (Pair 123 234)", "PUSH")
    }

    fun assertStartElement(matcher: MichelsonBraceMatcher, code: String, expectedStartText: String) {
        val (file, psiElement) = configureByCode(code)
        val start = matcher.getCodeConstructStart(file, psiElement!!.textOffset)
        Assert.assertEquals(expectedStartText, file.findElementAt(start)?.text)
    }
}
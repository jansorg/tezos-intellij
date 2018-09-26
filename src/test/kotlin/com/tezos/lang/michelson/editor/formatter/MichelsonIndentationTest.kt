package com.tezos.lang.michelson.editor.formatter

import com.tezos.lang.michelson.MichelsonFixtureTest

/**
 * @author jansorg
 */
class MichelsonIndentationTest : MichelsonFixtureTest() {
    // tests, that pressing enter automatically inserts an indentation in an empty block
    fun testIndent() {
        configureByCode("<caret>")

        myFixture.type("\b\n") //backspace to remove space in code{...}
        myFixture.type("DROP;")
        myFixture.checkResult("""
            parameter unit;
            storage unit;
            code {
                   DROP;
                 }""".trimIndent())
    }

    // tests, that pressing enter automatically inserts an indentation in a nested block
    fun testIndentInBlock() {
        configureByCode("<caret>")

        // this automatically inserts the closing brace '}' for DIP
        myFixture.type("DIP {\nDROP;")
        myFixture.checkResult("""
            parameter unit;
            storage unit;
            code { DIP {
                         DROP;
                       } }""".trimIndent())
    }

    // tests, that pressing enter automatically inserts an indentation in a nested contract
    fun testIndentInContract() {
        configureByCode("CREATE_CONTRACT<caret>")

        // this automatically inserts the closing braces
        myFixture.type("{\ncode{\nDIP{}")
        myFixture.checkResult("""
            parameter unit;
            storage unit;
            code { CREATE_CONTRACT{
                                    code{
                                          DIP{}
                                        }
                                  } }""".trimIndent())
    }
}
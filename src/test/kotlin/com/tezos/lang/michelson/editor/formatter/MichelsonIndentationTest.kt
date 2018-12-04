package com.tezos.lang.michelson.editor.formatter

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import com.tezos.lang.michelson.MichelsonFixtureTest
import org.junit.Ignore

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

    // tests, that no extra indent is added after a newline after a command token
    fun testIndentAfterComment() {
        configureByCode("<caret>")

        // this automatically inserts the closing braces
        myFixture.type("\b\n") // backspace to remove the space inserted by our setup
        myFixture.type("DROP")
        myFixture.checkResult("""
            parameter unit;
            storage unit;
            code {
                   DROP
                 }""".trimIndent())
    }

    // tests, that no extra indent is added after a newline after a command token
    // it's failing for yet unknown reasons and is working when the same steps are executed in a live IDE
    @Ignore
    fun _testIndentAfterCommentAlignBlocks() {
        try {
            WriteCommandAction.runWriteCommandAction(project) {
                val settings = CodeStyleSettingsManager.getSettings(project).clone()
                val customMichelson = settings.getCustomSettings(MichelsonCodeStyleSettings::class.java)
                customMichelson.ALIGN_BLOCKS = true

                CodeStyleSettingsManager.getInstance(project).temporarySettings = settings
            }

            configureByCode("# comment<caret>")

            // this automatically inserts the closing braces
            myFixture.type("\n")
            myFixture.type("DROP")
            myFixture.type("\n")
            myFixture.checkResult("""
                parameter unit;
                storage unit;
                code { # comment
                       DROP
                     }""".trimIndent())
        } finally {
            WriteCommandAction.runWriteCommandAction(project) {
                CodeStyleSettingsManager.getInstance().dropTemporarySettings()
            }
        }
    }
}
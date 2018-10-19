package com.tezos.client.stack

import com.tezos.lang.michelson.MichelsonTestUtils
import org.antlr.v4.runtime.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.lang.IllegalStateException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths


/**
 * @author jansorg
 */
@RunWith(Parameterized::class)
class StackParserTest(val file: String) {
    companion object {
        private val dataRootPath = MichelsonTestUtils.dataPath()

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): List<String> {
            val files = MichelsonTestUtils.locateTestDataFiles(Paths.get("tezos-client", "stack")) {
                it.fileName.toString().endsWith(".txt") && !it.fileName.toString().endsWith(".ast.txt")
            }

            return files.map { dataRootPath.relativize(it).toString() }.toList()
        }

    }

    @Test
    fun testParsing() {
        val filePath = dataRootPath.resolve(file)
        val fixedContent = MichelsonStackUtils.fixTezosClientStdout(Files.readAllBytes(filePath).toString(StandardCharsets.UTF_8))
        val input = ANTLRInputStream(fixedContent)

        val lexer = MichelsonStackLexer(input)
        val tokens = CommonTokenStream(lexer)

        val parser = MichelsonStackParser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(object:BaseErrorListener(){
            override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String?, e: RecognitionException?) {
                throw IllegalStateException("$file:$line:$charPositionInLine. Lexing failed: $msg")
            }
        })

        val stackInfo = parser.all()
        if (stackInfo.exception != null) {
            assertNull(AntlrAstPrinter.astToString(stackInfo), stackInfo.exception)
        }

        val astFile = filePath.resolveSibling(filePath.fileName.toString().replace(".txt", ".ast.txt"))
        val stack = AntlrAstPrinter.astToString(stackInfo)

        if (MichelsonTestUtils.updateReferenceData()) {
            Files.write(astFile, stack.toByteArray(StandardCharsets.UTF_8))
        } else {
            val expectedStack = if (Files.exists(astFile)) Files.readAllBytes(astFile).toString(StandardCharsets.UTF_8) else ""
            assertEquals(expectedStack, stack)
        }
    }

}

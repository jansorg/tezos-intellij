package com.tezos.lang.michelson.lexer

import com.tezos.lang.michelson.MichelsonTestUtils
import com.tezos.lang.michelson.MichelsonTestUtils.locateMichelsonFiles
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Parmeterized JUnit4 test which collects all michelson files at src/test/data/lexer and compars the tokens
 * returned by the lexer against a list of expected tokens and offset which are stored in a .tokens file.
 *
 * @author jansorg
 */
@RunWith(Parameterized::class)
class AllContractsLexerTokenTest(val michelsonFile: String) {
    companion object {
        private val dataRootPath = MichelsonTestUtils.dataPath()

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Iterable<String> {
            return locateMichelsonFiles(Paths.get("lexer")).filter(this::tokensFileExists).map({ dataRootPath.relativize(it).toString() }).toList()
        }

        fun tokensFileExists(michelsonFile: Path): Boolean {
            return Files.exists(tokensFilePath(michelsonFile))
        }

        fun tokensFilePath(michelsonFile: Path) = Paths.get(michelsonFile.toString().replace(".tz", ".tokens", true))
    }

    @Test
    fun testFile() {
        val file = dataRootPath.resolve(michelsonFile)
        MichelsonTestUtils.assertLexing(file, tokensFilePath(file))
    }
}
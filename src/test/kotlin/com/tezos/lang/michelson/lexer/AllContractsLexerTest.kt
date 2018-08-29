package com.tezos.lang.michelson.lexer

import com.tezos.lang.michelson.MichelsonTestUtils
import com.tezos.lang.michelson.MichelsonTestUtils.locateMichelsonFiles
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Parmeterized JUnit4 test which collects all michelson files at src/test/data/contracts.
 *
 * @author jansorg
 */
@RunWith(Parameterized::class)
class AllContractsLexerTest(val michelsonFile: String) {
    companion object {
        private val dataRootPath = MichelsonTestUtils.dataPath()

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Iterable<String> {
            val files = locateMichelsonFiles(Paths.get("contracts")).foldRight(mutableListOf()) { path: Path, r: MutableList<String> ->
                r += dataRootPath.relativize(path).toString()
                r
            }
            return files
        }
    }

    @Test
    fun testFile() {
        MichelsonTestUtils.assertNoLexingErrors(dataRootPath.resolve(michelsonFile))
    }
}
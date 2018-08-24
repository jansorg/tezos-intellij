package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.lexer.TestUtil
import org.junit.After
import org.junit.Before
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
class AllContractsParserTest(val michelsonFile: String) : AbstractParserTest() {
    companion object {
        private val dataRootPath = TestUtil.dataPath()

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

    @Before
    fun setupTest() = setUp()

    @After
    fun shutdownTest() = tearDown()

    @Test
    fun testFile() {
        testSingleFile(dataRootPath.resolve(michelsonFile))
    }
}
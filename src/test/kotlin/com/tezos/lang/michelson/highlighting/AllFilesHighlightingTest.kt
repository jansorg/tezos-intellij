package com.tezos.lang.michelson.highlighting

import com.intellij.testFramework.EdtTestUtil
import com.intellij.util.ThrowableRunnable
import com.tezos.lang.michelson.MichelsonFixtureTest
import com.tezos.lang.michelson.MichelsonTestUtils.dataPath
import com.tezos.lang.michelson.MichelsonTestUtils.locateMichelsonFiles
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.nio.file.Paths

/**
 * Parmeterized JUnit4 test which collects all michelson files at src/test/data/contracts.
 *
 * @author jansorg
 */
@RunWith(Parameterized::class)
class AllFilesHighlightingTest(val michelsonFile: String) : MichelsonFixtureTest() {
    companion object {
        private val dataRootPath = dataPath()

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Iterable<String> {
            return locateMichelsonFiles(Paths.get("highlighting")).map { dataRootPath.relativize(it).toString() }.toList()
        }
    }

    @Before
    fun setupTest() = setUp()

    @After
    fun shutdownTest() = tearDown()

    override fun isWriteActionRequired(): Boolean = false

    @Test
    fun testFile() {
        // run in EDT because the JUnit4 runner seems to override the Junit3 test's defaults somehow
        EdtTestUtil.runInEdtAndWait(ThrowableRunnable {
            myFixture.testHighlighting(michelsonFile)
        })
    }
}
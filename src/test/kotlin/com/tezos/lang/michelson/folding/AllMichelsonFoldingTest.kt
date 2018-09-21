package com.tezos.lang.michelson.folding

import com.intellij.openapi.command.WriteCommandAction
import com.tezos.lang.michelson.MichelsonFixtureTest
import com.tezos.lang.michelson.MichelsonTestUtils
import com.tezos.lang.michelson.MichelsonTestUtils.locateMichelsonFiles
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author jansorg
 */
@RunWith(Parameterized::class)
class AllMichelsonFoldingTest(val file: String) : MichelsonFixtureTest() {
    companion object {
        private val dataRootPath = MichelsonTestUtils.dataPath()

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Iterable<String> {
            val files = locateMichelsonFiles(Paths.get("folding")).foldRight(mutableListOf()) { path: Path, r: MutableList<String> ->
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
        testFolding(dataRootPath.resolve(file))
    }

    fun testFolding(sourceFile: Path) {
        WriteCommandAction.runWriteCommandAction(project) {
            myFixture.testFolding(sourceFile.toString())
        }
    }

    override fun runInDispatchThread(): Boolean = false
}
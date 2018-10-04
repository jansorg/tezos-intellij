package com.tezos.lang.michelson.psi

import com.intellij.psi.impl.DebugUtil
import com.tezos.lang.michelson.MichelsonFixtureTest
import com.tezos.lang.michelson.MichelsonTestUtils
import com.tezos.lang.michelson.MichelsonTestUtils.dataPath
import com.tezos.lang.michelson.MichelsonTestUtils.load
import com.tezos.lang.michelson.MichelsonTestUtils.locateMichelsonFiles
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * This tests reads all .tz files in test/data/psi_tree and comparese the dump of the PsiFile against the
 * data contained in filename.psi.txt. Whitespace is ignored.
 *
 * @author jansorg
 */
@RunWith(Parameterized::class)
class AllPsiTreeDumpTest(val michelsonFile: String) : MichelsonFixtureTest() {
    companion object {
        private val dataRootPath = dataPath()

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Iterable<String> {
            val files = locateMichelsonFiles(Paths.get("psi_tree")).foldRight(mutableListOf()) { path: Path, r: MutableList<String> ->
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
        val file = dataRootPath.resolve(michelsonFile)
        val psiDumpFile = dataRootPath.resolve(michelsonFile.replace(".tz", ".psi.txt"))

        val michselsonData = load(file)
        val expectedPsiTree = load(psiDumpFile, "")

        val psiFile = myFixture.configureByText(file.fileName.toString(), michselsonData)
        val psiTree = DebugUtil.psiTreeToString(psiFile, true)

        if (MichelsonTestUtils.updateReferenceData()) {
            Files.write(psiDumpFile, psiTree.toByteArray())
        } else {
            Assert.assertEquals("expected psi tree to match, dump file ${dataRootPath.relativize(psiDumpFile)}", expectedPsiTree, psiTree)
        }
    }
}
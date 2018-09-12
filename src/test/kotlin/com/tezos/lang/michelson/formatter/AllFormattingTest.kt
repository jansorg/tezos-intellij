package com.tezos.lang.michelson.formatter

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.util.TextRange
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.rt.execution.junit.FileComparisonFailure
import com.intellij.util.containers.ContainerUtil
import com.tezos.lang.michelson.MichelsonFixtureTest
import com.tezos.lang.michelson.MichelsonTestUtils.dataPath
import com.tezos.lang.michelson.MichelsonTestUtils.locateMichelsonFiles
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.nio.file.Path
import java.nio.file.Paths


/**
 * Test which checks the formatting of all files in "test/data/formatter/, files ending with ".formatted.tz" are skipped.
 * The reference formatting of a.tz must be in a.formatted.tz.
 *
 * @author jansorg
 */
@RunWith(Parameterized::class)
class AllFormattingTest(val michelsonFile: String) : MichelsonFixtureTest() {
    companion object {
        private val dataRootPath = dataPath()

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Iterable<String> {
            val files = locateMichelsonFiles(Paths.get("formatter")).foldRight(mutableListOf()) { path: Path, r: MutableList<String> ->
                if (!path.fileName.toString().contains(".formatted.")) {
                    r += dataRootPath.relativize(path).toString()
                }
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
        testFileFormatting(dataRootPath.resolve(michelsonFile))
    }

    fun testFileFormatting(file: Path) {
        val relativeFile = dataRootPath.relativize(file)
        myFixture.configureByFiles(relativeFile.toString())

        // CodeStyleSettingsManager.getSettings(project).SPACE_AROUND_ASSIGNMENT_OPERATORS = true
        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformatText(myFixture.file, ContainerUtil.newArrayList<TextRange>(myFixture.file.textRange))
        }

        try {
            myFixture.checkResultByFile(dataRootPath.relativize(file.resolveSibling(file.fileName.toString().replace(".tz", ".formatted.tz"))).toString())
        } catch (e: Throwable) {
            // workaround to force IntelliJ to show the "compare" link in the console output of the test case
            // this doesn't seem to work well for wrapped exceptions
            val cause = e.cause?.cause as? FileComparisonFailure
            if (cause != null) {
                TestCase.assertEquals("File $relativeFile", cause.expected, cause.actual)
            } else {
                throw e
            }
        }
    }
}
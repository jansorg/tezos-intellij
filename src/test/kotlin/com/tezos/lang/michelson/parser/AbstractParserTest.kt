package com.tezos.lang.michelson.parser

import com.google.common.collect.Lists
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.intellij.util.exists
import com.tezos.lang.michelson.lexer.TestUtil
import org.junit.Assert
import java.nio.file.FileVisitOption
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Consumer
import java.util.stream.Collectors

/**
 * @author jansorg
 */
abstract class AbstractParserTest : LightPlatformCodeInsightFixtureTestCase() {
    private val LOG = Logger.getInstance("test.parser")

    companion object {
        /**
         * Recursively locates all michelson files in directory "path".
         * @param path If path is relative then it will be reseolved to the data dir at 'src/test/data', absolute paths will be used as is.
         * @return the list of michelson files
         */
        fun locateMichelsonFiles(path: Path): List<Path> {
            val sourcePath = when (path.isAbsolute) {
                true -> path
                false -> TestUtil.dataPath().resolve(path)
            }

            if (!sourcePath.exists()) {
                throw IllegalStateException("Directory $sourcePath not found.")
            }

            val files = Files.find(sourcePath, 10, { p, a -> isMichelsonFile(p) }, arrayOf(FileVisitOption.FOLLOW_LINKS))
            return files.collect(Collectors.toList())
        }

        fun isMichelsonFile(file: Path): Boolean {
            return file.fileName.toString().endsWith(".tz") && !file.fileName.toString().contains("notParsed")
        }
    }

    fun testDirectory(path: Path, showDetails: Boolean = true) {
        val start = System.currentTimeMillis()

        var files = 0
        var filesWithErrors = 0
        var errors = 0

        for (file in locateMichelsonFiles(path)) {
            LOG.info("Checking file: " + file.toAbsolutePath().toString())

            val psiFile = myFixture.configureByText(file.fileName.toString(), String(Files.readAllBytes(file)))
            Assert.assertNotNull(psiFile)

            val errorMessages = findErrors(psiFile)
            if (!errorMessages.isEmpty()) {
                filesWithErrors++
                errors += errorMessages.size
            }

            if (showDetails) {
                errorMessages.forEach(Consumer<String> { println(it) })
            }

            files++
        }

        val duration = (System.currentTimeMillis() - start).toDouble() / 1000.0
        Assert.assertTrue(String.format("Files: %d total. %d with errors. %d without errors. Total errors: %d. Duration: %.2fs", files, filesWithErrors, files - filesWithErrors, errors, duration), errors == 0)
    }

    fun findErrors(file: PsiFile): List<String> {
        Assert.assertNotNull("File not found", file)

        val errors = Lists.newLinkedList<PsiErrorElement>()
        file.acceptChildren(object : PsiRecursiveElementVisitor() {
            override fun visitErrorElement(element: PsiErrorElement) {
                errors.add(element)
            }
        })

        return errors.stream().map { psiErrorElement -> description(file, errors) }.collect(Collectors.toList())
    }

    fun description(file: PsiFile, errors: List<PsiErrorElement>): String {
        val builder = StringBuilder()

        builder.append("\n## File: " + file.name)
        builder.append(", Errors: " + errors.size)
        for (error in errors) {
            builder.append("\n\t").append(error.errorDescription)
            builder.append(": '").append(error.text).append("'").append(", line ").append(TestUtil.getElementLineNumber(error))
        }

        builder.append("\n\n")
        return builder.toString()
    }

    fun testSingleFile(first: String, vararg path: String) {
        testSingleFile(Paths.get(first, *path))
    }

    fun testSingleFile(filePath: Path) {
        WriteCommandAction.runWriteCommandAction(project) {
            val bytes = Files.readAllBytes(TestUtil.dataPath().resolve(filePath))
            val psiFile = myFixture.configureByText(filePath.fileName.toString(), String(bytes))
            val errors = findErrors(psiFile)
            Assert.assertEquals("Errors: " + errors.stream().reduce("", { s, s2 -> s + "\n" + s2 }), 0, errors.size.toLong())
        }
    }
}
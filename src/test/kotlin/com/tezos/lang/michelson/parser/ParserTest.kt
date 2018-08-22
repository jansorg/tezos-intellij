package com.tezos.lang.michelson.parser

import com.google.common.collect.Lists
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.tezos.lang.michelson.lexer.TestUtil
import org.junit.Assert
import org.junit.Test
import java.nio.file.FileVisitOption
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.util.function.BiPredicate
import java.util.function.Consumer
import java.util.stream.Collectors

/**
 * @author jansorg
 */
class ParserTest : LightPlatformCodeInsightFixtureTestCase() {
    private val LOG = Logger.getInstance("test.parser")

    fun testParsing() {
        testSingleFile("lexer", "identity.tz")
    }

    private fun isMichelsonFile(file: Path, basicFileAttributes: BasicFileAttributes): Boolean {
        return file.fileName.toString().endsWith(".tz") && !file.fileName.toString().contains("notParsed")
    }

    private fun testDirectory(path: Path, showDetails: Boolean) {
        val start = System.currentTimeMillis()
        val fileStream = Files.find(path, 10, BiPredicate<Path, BasicFileAttributes> { file, basicFileAttributes -> isMichelsonFile(file, basicFileAttributes) }, FileVisitOption.FOLLOW_LINKS)

        var files = 0
        var filesWithErrors = 0
        var errors = 0

        for (file in fileStream.collect(Collectors.toList())) {
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

    private fun findErrors(file: PsiFile): List<String> {
        Assert.assertNotNull("File not found", file)

        val errors = Lists.newLinkedList<PsiErrorElement>()
        file.acceptChildren(object : PsiRecursiveElementVisitor() {
            override fun visitErrorElement(element: PsiErrorElement) {
                errors.add(element)
            }
        })

        return errors.stream().map { psiErrorElement -> description(file, errors) }.collect(Collectors.toList())
    }

    private fun description(file: PsiFile, errors: List<PsiErrorElement>): String {
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

    private fun testSingleFile(first: String, vararg path: String) {
        val filePath = Paths.get(first, *path)

        val bytes = Files.readAllBytes(TestUtil.dataPath().resolve(filePath))
        val psiFile = myFixture.configureByText(filePath.fileName.toString(), String(bytes))
        val errors = findErrors(psiFile)
        Assert.assertEquals("Errors: " + errors.stream().reduce("", { s, s2 -> s + "\n" + s2 }), 0, errors.size.toLong())
    }
}
package com.tezos.lang.michelson.parser

import com.google.common.collect.Lists
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.tezos.lang.michelson.MichelsonTestUtils
import org.junit.Assert
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

/**
 * @author jansorg
 */
abstract class AbstractParserTest : LightPlatformCodeInsightFixtureTestCase() {
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
            builder.append(": '").append(error.text).append("'").append(", line ").append(MichelsonTestUtils.getElementLineNumber(error))
        }

        builder.append("\n\n")
        return builder.toString()
    }

    fun testSingleFile(filePath: Path) {
        WriteCommandAction.runWriteCommandAction(project) {
            val bytes = Files.readAllBytes(MichelsonTestUtils.dataPath().resolve(filePath))
            val psiFile = myFixture.configureByText(filePath.fileName.toString(), String(bytes))
            val errors = findErrors(psiFile)
            Assert.assertEquals("Errors: " + errors.stream().reduce("", { s, s2 -> s + "\n" + s2 }), 0, errors.size.toLong())
        }
    }
}
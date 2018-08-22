package com.tezos.lang.michelson.lexer

import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.tezos.lang.michelson.MichelsonTypes
import org.junit.Assert

import java.io.IOException
import java.net.URISyntaxException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author jansorg
 */
object TestUtil {
    fun dataPath(): Path {
        val url = TestUtil::class.java.classLoader.getResource("marker.txt")
                ?: throw IllegalStateException("Unable to locate marker file")

        val subPath = Paths.get("src", "test", "data")

        try {
            var path: Path? = Paths.get(url.toURI())
            path = path!!.parent

            while (path != null && !Files.exists(path.resolve(subPath))) {
                path = path.parent
            }

            if (path != null && Files.exists(path.resolve(subPath))) {
                return path.resolve(subPath)
            }
        } catch (ignored: URISyntaxException) {
        }

        throw IllegalStateException("Unable to locate testdata directory")
    }

    @Throws(IOException::class)
    fun load(path: String, vararg more: String): String {
        val filePath = dataPath().resolve(Paths.get(path, *more))
        if (!Files.exists(filePath)) {
            throw IllegalStateException("Unable to locate file at $filePath")
        }

        return String(Files.readAllBytes(filePath))
    }

    @Throws(IOException::class)
    @JvmOverloads
    fun assertLexing(fileName: String, showWhitespace: Boolean = false) {
        val data = load("lexer", fileName)
        val expectedReport = load("lexer", fileName.replace(".tz", ".tokens"))

        val l = MichelsonLexer()
        l.start(data)

        val report = StringBuilder()

        var token = l.getTokenType()
        while (token != null) {
            if (showWhitespace || token !== TokenType.WHITE_SPACE) {
                report.append(String.format("[%d : %d] %s\n%s\n-----\n",
                        l.getTokenStart(),
                        l.getTokenEnd(),
                        token!!.toString(),
                        l.getTokenText()))
            }

            l.advance()
            token = l.getTokenType()
        }

        Assert.assertEquals("Unexpected tokens returned by lexer", expectedReport, report.toString())
    }

    fun getElementLineNumber(element: PsiElement): Int {
        val fileViewProvider = element.containingFile.viewProvider
        return if (fileViewProvider.document != null) {
            fileViewProvider.document!!.getLineNumber(element.textOffset) + 1
        } else 0

    }
}

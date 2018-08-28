package com.tezos.lang.michelson.lexer

import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import org.junit.Assert
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

    fun load(path: String, vararg more: String): String = load(dataPath().resolve(Paths.get(path, *more)))

    fun load(filePath: Path): String {
        if (!Files.exists(filePath)) {
            throw IllegalStateException("Unable to locate file at $filePath")
        }

        return String(Files.readAllBytes(filePath))
    }

    fun assertLexing(fileName: String, showWhitespace: Boolean = false) {
        assertLexing(Paths.get(fileName), showWhitespace)
    }

    fun assertLexing(file: Path, showWhitespace: Boolean = false) {
        val finalPath = if (file.isAbsolute) file else Paths.get("lexer").resolve(file)
        val data = load(finalPath)
        val expectedReport = load("lexer", file.fileName.toString().replace(".tz", ".tokens"))

        val l = MichelsonLexer()
        l.start(data)

        val report = StringBuilder()

        var token = l.getTokenType()
        while (token != null) {
            if (showWhitespace || token !== TokenType.WHITE_SPACE) {
                report.append(String.format("[%d : %d] %s\n%s\n-----\n",
                        l.getTokenStart(),
                        l.getTokenEnd(),
                        token.toString(),
                        l.getTokenText()))
            }

            l.advance()
            token = l.getTokenType()
        }

        Assert.assertEquals("Unexpected tokens returned by lexer", expectedReport, report.toString())
    }

    fun assertNoLexingErrors(file: Path) {
        val finalPath = if (file.isAbsolute) file else Paths.get("lexer").resolve(file)
        val data = load(finalPath)

        assertNoStringLexingErrors(data)
    }

    fun assertNoStringLexingErrors(data: String) {
        val l = MichelsonLexer()
        l.start(data)

        val report = StringBuilder()

        var token = l.getTokenType()
        while (token != null) {
            if (token == TokenType.BAD_CHARACTER) {
                report.append("error at offset ${l.currentPosition.offset}: ${l.tokenText}\n")
            }

            l.advance()
            token = l.getTokenType()
        }

        Assert.assertTrue("Lexing errors:" + report, report.toString().isEmpty())
    }

    fun getElementLineNumber(element: PsiElement): Int {
        val fileViewProvider = element.containingFile.viewProvider
        return if (fileViewProvider.document != null) {
            fileViewProvider.document!!.getLineNumber(element.textOffset) + 1
        } else 0

    }
}

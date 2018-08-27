package com.tezos.lang.michelson

import com.intellij.util.exists
import com.tezos.lang.michelson.lexer.TestUtil
import java.nio.file.FileVisitOption
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

/**
 * @author jansorg
 */
object MichelsonTestUtils {
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
package com.tezos.client.client

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

open class StandaloneTezosClient(protected val executable: Path) : TezosClient {
    protected open fun filename(file: Path): String {
        return file.toString()
    }

    protected open fun clientCommandArgs(vararg args: String): List<String> {
        return args.toList()
    }

    override fun typecheckScript(file: Path): String? {
        val tmpFile = Files.createTempFile("tezos-client", ".txt")
        try {
            val builder = ProcessBuilder()
                    .command(listOf(executable.toAbsolutePath().toString()) + clientCommandArgs("typecheck", "script", file.toAbsolutePath().toString(), "--emacs", "-v"))
                    .redirectOutput(tmpFile.toFile())

            val p = builder.start()
            return when {
                p.exitValue() == 0 -> Files.readAllBytes(tmpFile).toString(StandardCharsets.UTF_8)
                else -> null
            }
        } finally {
            try {
                Files.deleteIfExists(tmpFile)
            } catch (e: Exception) {
                //ignored
            }
        }
    }
}
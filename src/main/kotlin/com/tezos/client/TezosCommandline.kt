package com.tezos.client

import java.nio.file.Path

/**
 * @author jansorg
 */
object TezosCommandline {
    val DefaultEnv = mapOf(
            "TEZOS_CLIENT_UNSAFE_DISABLE_DISCLAIMER" to "Y",
            "ALPHANET_EMACS" to "true",
            "TEZOS_ALPHANET_DO_NOT_PULL" to "yes"
    )

    fun typecheckScriptContent(content: String, clientPath: Path): List<String> {
        return cmdPrefix(clientPath) + listOf("typecheck", "script", "text:$content", "--emacs", "-v")
    }

    fun executeContract(file: Path, storage: String, input: String, clientPath: Path): List<String> {
        val clientFilePath = clientFilePath(file, clientPath.toString().endsWith(".sh"))
        return cmdPrefix(clientPath) + listOf("run", "script", clientFilePath, "on", "storage", storage, "and", "input", input)
    }

    private fun cmdPrefix(clientPath: Path): List<String> {
        val cmdPath = clientPath.toString()
        return when (cmdPath.endsWith(".sh")) {
            true -> listOf(cmdPath, "client")
            false -> listOf(cmdPath)
        }
    }

    private fun clientFilePath(file: Path, scriptClient: Boolean): String {
        return when (scriptClient) {
            true -> "container:$file"
            false -> file.toString()
        }
    }
}
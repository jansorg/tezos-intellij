package com.tezos.client

import java.nio.file.Path

/**
 * Provides the command lines which are used to run a strandalone or dockerized tezos client.
 *
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
        val clientFilePath = clientFilePath(file, isScriptClient(clientPath.toString()))
        return cmdPrefix(clientPath) + listOf("run", "script", clientFilePath, "on", "storage", storage, "and", "input", input)
    }

    private fun cmdPrefix(clientPath: Path): List<String> {
        val cmdPath = clientPath.toString()
        return when (isScriptClient(cmdPath)) {
            true -> listOf(cmdPath, "client")
            false -> listOf(cmdPath)
        }
    }

    /**
     * Returns the file path send on the commandline. A dockerized client is using the container: prefix because it's a file
     * on the host.
     */
    private fun clientFilePath(file: Path, scriptClient: Boolean): String {
        return when (scriptClient) {
            true -> "container:$file"
            false -> file.toString()
        }
    }

    private fun isScriptClient(cmdPath: String) = cmdPath.endsWith(".sh")
}
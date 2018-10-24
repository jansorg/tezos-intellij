package com.tezos.client.stack

class MichelsonClientError(message: String, cause: Throwable?) : Exception(message, cause)

/**
 * @author jansorg
 */
object MichelsonStackUtils {
    fun fixTezosClientStdout(content: String): String {
        val index = content.indexOf("((types")
        return when {
            index > 0 -> content.substring(index)
            index == 0 -> content
            else -> when {
                content.contains("Node is not running") -> throw MichelsonClientError("The node is not running.", null)
                else -> throw MichelsonClientError("Invalid output. Make sure that the client is working properly.", null)
            }
        }
    }
}
package com.tezos.client.stack

/**
 * An error which occurred when running the Tezos client.
 */
open class TezosClientError(message: String, cause: Throwable?) : Exception(message, cause)

/**
 * A Tezos client error to signal that the client returned a "Node is not running" error
 */
class TezosClientNodeUnavailableError(message: String, cause: Throwable?) : TezosClientError(message, cause)

/**
 * Utilities to work with the Tezos client.
 * @author jansorg
 */
object TezosClientUtils {
    /**
     * Clean up output of Tezos clients.
     * This removes the disclaimer which is printed before the regular content.
     */
    fun fixTezosClientStdout(content: String): String {
        val index = content.indexOf("((types")
        return when {
            index > 0 -> content.substring(index)
            index == 0 -> content
            else -> when {
                content.contains("Node is not running") -> throw TezosClientNodeUnavailableError("The Tezos client's not is not running.", null)
                else -> throw TezosClientError("Invalid output. Make sure that the client is working properly.", null)
            }
        }
    }
}
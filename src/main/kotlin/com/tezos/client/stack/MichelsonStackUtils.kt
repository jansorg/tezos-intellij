package com.tezos.client.stack

open class TezosClientError(message: String, cause: Throwable?) : Exception(message, cause)

class TezosClientNodeUnavailableError(message: String, cause: Throwable?) : TezosClientError(message, cause)

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
                content.contains("Node is not running") -> throw TezosClientNodeUnavailableError("The Tezos client's not is not running.", null)
                else -> throw TezosClientError("Invalid output. Make sure that the client is working properly.", null)
            }
        }
    }
}
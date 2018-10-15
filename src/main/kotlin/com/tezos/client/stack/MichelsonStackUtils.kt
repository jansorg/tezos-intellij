package com.tezos.client.stack

/**
 * @author jansorg
 */
object MichelsonStackUtils {
    fun fixTezosClientStdout(content: String): String {
        val index = content.indexOf("((types")
        return when (index > 0) {
            true -> content.substring(index)
            false -> content
        }
    }
}
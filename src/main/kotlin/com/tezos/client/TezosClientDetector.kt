package com.tezos.client

import com.intellij.execution.configurations.PathEnvironmentVariableUtil
import com.tezos.intellij.settings.TezosClientConfig

/**
 * Detects the available Tezos clients by checking the entries of $PATH.
 * The first client is marked as the default client.
 *
 * @author jansorg
 */
class TezosClientDetector(private val names: List<String> = clientNames) {
    companion object {
        private val clientNames = listOf("tezos-client", "alphanet.sh", "mainnet.sh")
    }


    /**
     * Detects the available Tezos clients by checking the entries of $PATH.
     * @return the detected clients. the first item is marked as the default client.
     */
    fun detectClients(): List<TezosClientConfig> {
        val configs = clientNames.flatMap {
            PathEnvironmentVariableUtil.findAllExeFilesInPath(it)
        }.map {
            val path = it.toPath()
            TezosClientConfig(path.fileName.toString(), path.toString(), false)
        }

        if (configs.isNotEmpty()) {
            configs.get(0).isDefault = true
        }

        return configs
    }
}
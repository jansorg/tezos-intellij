package com.tezos.client

import com.intellij.execution.configurations.PathEnvironmentVariableUtil
import com.tezos.intellij.settings.TezosClientConfig

/**
 * @author jansorg
 */
class TezosClientDetector(private val names: List<String> = clientNames) {
    companion object {
        private val clientNames = listOf("tezos-client", "alphanet.sh", "mainnet.sh")
    }

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
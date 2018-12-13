package com.tezos.lang.michelson.runConfig

import com.intellij.execution.CantRunException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.tezos.client.TezosCommandline
import com.tezos.intellij.settings.TezosSettingService
import java.nio.file.Paths

class MichelsonCommandLineState(private val config: MichelsonRunConfiguration, environment: ExecutionEnvironment) : CommandLineState(environment) {
    override fun startProcess(): ProcessHandler {
        val processHandler = ColoredProcessHandler(createCommandLine())
        ProcessTerminatedListener.attach(processHandler)
        return processHandler
    }

    /**
     * Syntax:
     *      run script <src> on storage <storage> and input <storage> [--trace-stack]
     * Standalone client:
     *  tezos-client run script file.tz on storage ... and input ...
     * Script client:
     *  alphanet.sh client run script file.tz on storage ... and input ...
     *
     *  It expects values for storage and parameter either in the run configuration or as DataKey in the execution environment.
     *  @throws CantRunException
     */
    private fun createCommandLine(): GeneralCommandLine {
        val filePath = config.filePath ?: throw CantRunException("Missing Michelson file")

        config.checkSettingsBeforeRun()

        val paramInput = DataKeys.PARAMETER_INPUT.get(config) ?: config.inputParameter
        ?: throw CantRunException("missing input parameter value")

        val storageInput = DataKeys.STORAGE_INPUT.get(config) ?: config.inputStorage
        ?: throw CantRunException("missing input storage value")

        val client = when (config.useDefaultTezosClient) {
            true -> TezosSettingService.getSettings().getDefaultClient()?.asJavaPath
            else -> config.tezosClientPath?.let { Paths.get(it) }
        }
        client ?: throw CantRunException("Missing Tezos client path")

        val cmdLine = GeneralCommandLine(TezosCommandline.executeContract(Paths.get(filePath), storageInput, paramInput, client))
        cmdLine.environment.putAll(TezosCommandline.DefaultEnv)
        return cmdLine
    }
}

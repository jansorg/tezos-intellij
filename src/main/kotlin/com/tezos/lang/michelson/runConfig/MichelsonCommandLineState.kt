package com.tezos.lang.michelson.runConfig

import com.intellij.execution.CantRunException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.tezos.intellij.settings.TezosClientConfig
import com.tezos.intellij.settings.TezosSettingService

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
     */
    private fun createCommandLine(): GeneralCommandLine {
        val executable = when (config.useDefaultTezosClient) {
            true -> TezosSettingService.getInstance().state.getDefaultClient()
            false -> config.tezosClientPath?.let {
                TezosClientConfig("", it, false)
            }
        } ?: throw CantRunException("No tezos client configured")

        val file = config.filePath ?: throw CantRunException("Script file not configured")

        val params = when (executable.isScriptClient) {
            true -> listOf("client", "run", "script", file, "on", "storage", "...", "and", "input", "...")
            false -> listOf("run", "script", file, "on", "storage", "...", "and", "input", "...")
        }

        val cmd = GeneralCommandLine(executable.executablePath)
        cmd.addParameters(params)
        return cmd
    }
}

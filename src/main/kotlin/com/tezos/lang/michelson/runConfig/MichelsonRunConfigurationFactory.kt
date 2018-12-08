package com.tezos.lang.michelson.runConfig

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project

internal class MichelsonRunConfigurationFactory(configurationType: MichelsonRunConfigurationType) : ConfigurationFactory(configurationType) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return MichelsonRunConfiguration(project, this, name)
    }
}
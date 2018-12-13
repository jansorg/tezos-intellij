package com.tezos.lang.michelson.runConfig

import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.project.PossiblyDumbAware
import com.tezos.intellij.ui.Icons

class MichelsonRunConfigurationType : ConfigurationTypeBase("tezos.michelson", "Michelson", "Michelson run configuration", Icons.Tezos), PossiblyDumbAware {
    companion object {
        fun getInstance() = ConfigurationTypeUtil.findConfigurationType(MichelsonRunConfigurationType::class.java)
    }

    init {
        addFactory(MichelsonRunConfigurationFactory(this))
    }

    // needed to enable the run button while IntelliJ is indexing
    override fun isDumbAware(): Boolean {
        return true
    }
}

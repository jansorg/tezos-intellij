package com.tezos.lang.michelson.runConfig

import com.intellij.execution.ExecutionTarget
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.components.PathMacroManager
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializer
import com.intellij.util.xmlb.XmlSerializerUtil
import com.tezos.intellij.settings.TezosSettingService
import org.jdom.Element
import java.nio.file.Files
import java.nio.file.Paths

class MichelsonRunConfiguration(project: Project, factory: ConfigurationFactory, name: String) : RunConfigurationBase(project, factory, name), LocatableConfiguration {
    private var configBean = MichelsonRunConfigBean()

    var useDefaultTezosClient: Boolean
        get() = configBean.USE_DEFAULT_CLIENT
        set(value) {
            configBean.USE_DEFAULT_CLIENT = value
        }

    var tezosClientPath: String?
        get() = configBean.CLIENT_PATH
        set(value) {
            configBean.CLIENT_PATH = value
        }

    var filePath: String?
        get() = configBean.FILE_PATH
        set(value) {
            configBean.FILE_PATH = value
        }

    override fun canRunOn(target: ExecutionTarget): Boolean {
        return true
    }

    override fun suggestedName(): String? {
        return null
    }

    override fun isGeneratedName(): Boolean {
        return false
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return MichelsonRunSettingsEditor()
    }

    override fun checkConfiguration() {
        // errors
        if (configBean.USE_DEFAULT_CLIENT && TezosSettingService.getInstance().state.getDefaultClient() == null) {
            throw RuntimeConfigurationError("No default Tezos client configured.")
        }

        if (configBean.FILE_PATH == null) {
            throw RuntimeConfigurationError("Michelson file not configured")
        }

        if (Files.notExists(Paths.get(configBean.FILE_PATH))) {
            throw RuntimeConfigurationError("Michelson file doesn't exist")
        }

        // warnings
        if (name.isEmpty()) {
            throw RuntimeConfigurationWarning("Name is empty")
        }
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        return MichelsonCommandLineState(this, environment)
    }

    override fun readExternal(element: Element) {
        PathMacroManager.getInstance(project).expandPaths(element)
        super.readExternal(element)
        XmlSerializer.deserializeInto(configBean, element)
        // EnvironmentVariablesComponent.readExternal(element, getEnvs())
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        XmlSerializer.serializeInto(configBean, element, null)
        // EnvironmentVariablesComponent.writeExternal(element, getEnvs())
    }

    override fun clone(): RunConfiguration {
        val clone = super.clone() as MichelsonRunConfiguration
        clone.configBean = XmlSerializerUtil.createCopy(configBean)
        return clone
    }

    private class MichelsonRunConfigBean {
        @JvmField
        var FILE_PATH: String? = null
        @JvmField
        var USE_DEFAULT_CLIENT: Boolean = true
        @JvmField
        var CLIENT_PATH: String? = null
    }
}
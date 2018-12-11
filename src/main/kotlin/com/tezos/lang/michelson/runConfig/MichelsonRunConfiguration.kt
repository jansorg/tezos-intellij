package com.tezos.lang.michelson.runConfig

import com.intellij.execution.ExecutionTarget
import com.intellij.execution.Executor
import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.components.PathMacroManager
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiManager
import com.intellij.util.xmlb.XmlSerializer
import com.intellij.util.xmlb.XmlSerializerUtil
import com.tezos.client.stack.MichelsonStackUtils
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.lang.michelson.psi.MichelsonPsiFile
import org.jdom.Element
import java.nio.file.Files
import java.nio.file.Paths

class MichelsonRunConfiguration(project: Project, factory: ConfigurationFactory, name: String) : RunConfigurationBase(project, factory, name), LocatableConfiguration {
    private var configBean = MichelsonRunConfigBean()

    val env = mutableMapOf<String, String>()

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

    var promptForInput: Boolean
        get() = configBean.PROMPT_FOR_INPUT
        set(value) {
            configBean.PROMPT_FOR_INPUT = value
        }

    var inputParameter: String?
        get() = configBean.INPUT_PARAM
        set(value) {
            configBean.INPUT_PARAM = value
        }

    var inputStorage: String?
        get() = configBean.INPUT_STORAGE
        set(value) {
            configBean.INPUT_STORAGE = value
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

    override fun isCompileBeforeLaunchAddedByDefault(): Boolean {
        return false
    }

    override fun excludeCompileBeforeLaunchOption(): Boolean {
        return true
    }

    /**
     * Requests input values for parameter and storage if the run configuration has the corresponding flag set.
     */
    override fun checkSettingsBeforeRun() {
        if (promptForInput) {
            val psiFile = findPsiFile()

            val contract = psiFile?.getContract()
            val paramSection = contract?.findParameterSection()
            val storageSection = contract?.findStorageSection()

            val dialog = MichelsonInputDialog(project,
                    paramSection?.type?.asStackType(), inputParameter ?: "",
                    storageSection?.type?.asStackType(), inputStorage ?: "")
            dialog.showAndGet()

//            if (dialog.exitCode == DialogWrapper.CANCEL_EXIT_CODE) {
//                throw RuntimeConfigurationError("Execution was cancelled")
//            }

            val paramInput = dialog.paramInputValue
            val storageInput = dialog.storageInputValue

            DataKeys.PARAMETER_INPUT.set(this, paramInput)
            DataKeys.STORAGE_INPUT.set(this, storageInput)
        }
    }

    fun parameterInputSample(): String? {
        val type = findPsiFile()?.getContract()?.findParameterSection()?.type?.asStackType()
        return type?.let {
            MichelsonStackUtils.generateSampleString(it)
        }
    }

    fun storageInputSample(): String? {
        val type = findPsiFile()?.getContract()?.findStorageSection()?.type?.asStackType()
        return type?.let {
            MichelsonStackUtils.generateSampleString(it)
        }
    }

    fun findPsiFile(): MichelsonPsiFile? {
        val file = VirtualFileManager.getInstance().findFileByUrl(VfsUtilCore.pathToUrl(filePath!!))
        return file?.let {
            PsiManager.getInstance(project).findFile(file)
        } as? MichelsonPsiFile
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
        EnvironmentVariablesComponent.readExternal(element, env)
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        XmlSerializer.serializeInto(configBean, element, null)
        EnvironmentVariablesComponent.writeExternal(element, env)
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
        @JvmField
        var INPUT_PARAM: String? = null
        @JvmField
        var INPUT_STORAGE: String? = null
        @JvmField
        var PROMPT_FOR_INPUT: Boolean = true
    }
}

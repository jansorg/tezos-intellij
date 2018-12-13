package com.tezos.lang.michelson.runConfig

import com.intellij.execution.CantRunException
import com.intellij.execution.ExecutionTarget
import com.intellij.execution.Executor
import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.openapi.components.PathMacroManager
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbAwareRunnable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiManager
import com.intellij.util.xmlb.XmlSerializer
import com.intellij.util.xmlb.XmlSerializerUtil
import com.tezos.client.stack.MichelsonStackType
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.intellij.ui.Icons
import com.tezos.lang.michelson.psi.MichelsonPsiFile
import org.jdom.Element
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.Icon

/**
 * We can't extend RunConfigurationBase as this class changed a lot between in versions later than 145.x.
 */
class MichelsonRunConfiguration(private val project: Project, private val factory: ConfigurationFactory, private var name: String) : UserDataHolderBase(), RunConfiguration, LocatableConfiguration, TargetAwareRunProfile, DumbAware {
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

    override fun suggestedName(): String? {
        return null
    }

    override fun isGeneratedName(): Boolean {
        return false
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return MichelsonRunSettingsEditor()
    }

    override fun getName(): String = name

    override fun setName(name: String) {
        this.name = name
    }

    override fun getIcon(): Icon = Icons.Tezos

    override fun getProject(): Project = project

    override fun getType(): ConfigurationType = factory.type

    override fun createRunnerSettings(provider: ConfigurationInfoProvider?): ConfigurationPerRunnerSettings? = null

    override fun getUniqueID(): Int {
        return System.identityHashCode(this)
    }

    override fun getRunnerSettingsEditor(runner: ProgramRunner<*>?): SettingsEditor<ConfigurationPerRunnerSettings>? = null

    override fun getFactory(): ConfigurationFactory = factory

    override fun canRunOn(target: ExecutionTarget): Boolean {
        return target.id == "default_target"
    }

    /**
     * Requests input values for parameter and storage if the run configuration has the corresponding flag set.
     * @throws CantRunException
     */
    fun checkSettingsBeforeRun() {
        if (promptForInput) {
            val psiFile = findPsiFile()

            val contract = psiFile?.getContract()
            val paramSection = contract?.findParameterSection()
            val storageSection = contract?.findStorageSection()

            val dialog = MichelsonInputDialog(project,
                    paramSection?.type?.asStackType(), inputParameter ?: "",
                    storageSection?.type?.asStackType(), inputStorage ?: "")
            dialog.showAndGet()

            if (dialog.exitCode == DialogWrapper.CANCEL_EXIT_CODE) {
                throw CantRunException("Execution was cancelled")
            }

            val paramInput = dialog.paramInputValue
            val storageInput = dialog.storageInputValue

            DataKeys.PARAMETER_INPUT.set(this, paramInput)
            DataKeys.STORAGE_INPUT.set(this, storageInput)
        }
    }

    fun parameterInputType(): MichelsonStackType? {
        return findPsiFile()?.getContract()?.findParameterSection()?.type?.asStackType()
    }

    fun storageInputType(): MichelsonStackType? {
        return findPsiFile()?.getContract()?.findStorageSection()?.type?.asStackType()
    }

    private fun findPsiFile(): MichelsonPsiFile? {
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
        XmlSerializer.deserializeInto(configBean, element)
        EnvironmentVariablesComponent.readExternal(element, env)
    }

    override fun writeExternal(element: Element) {
        XmlSerializer.serializeInto(configBean, element, null)
        EnvironmentVariablesComponent.writeExternal(element, env)
    }

    override fun clone(): RunConfiguration {
        val clone = MichelsonRunConfiguration(project, factory, name)
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

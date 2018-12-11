package com.tezos.lang.michelson.runConfig

import com.intellij.openapi.options.SettingsEditor
import com.tezos.intellij.settings.TezosSettingService
import javax.swing.JComponent

class MichelsonRunSettingsEditor : SettingsEditor<MichelsonRunConfiguration>() {
    private val editor = MichelsonRunConfigurationSettingsForm()

    override fun createEditor(): JComponent {
        return editor.mainPanel
    }

    override fun resetEditorFrom(config: MichelsonRunConfiguration) {
        val clients = TezosSettingService.getSettings().clients
        editor.michelsonFile = config.filePath ?: ""
        editor.inputParam = config.inputParameter
        editor.inputStorage = config.inputStorage
        editor.promptForInput = config.promptForInput

        config.parameterInputSample()?.let {
            editor.setParamEmptyText(it)
        }

        config.storageInputSample()?.let {
            editor.setStorageEmptyText(it)
        }

        editor.setTezosClients(clients)

        if (config.useDefaultTezosClient) {
            editor.setSelectedClient(null)
        } else {
            editor.setSelectedClient(clients.firstOrNull { it.executablePath == config.tezosClientPath })
        }
    }

    override fun applyEditorTo(config: MichelsonRunConfiguration) {
        config.filePath = editor.michelsonFile
        config.inputParameter = editor.inputParam
        config.inputStorage = editor.inputStorage
        config.promptForInput = editor.promptForInput

        val client = editor.selectedTezosClient
        if (client == null) {
            config.useDefaultTezosClient = true
            config.tezosClientPath = null
        } else {
            config.useDefaultTezosClient = false
            config.tezosClientPath = client.executablePath
        }
    }
}

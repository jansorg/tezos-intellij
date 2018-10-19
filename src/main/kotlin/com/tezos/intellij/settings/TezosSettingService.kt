package com.tezos.intellij.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * @author jansorg
 *
 */
@State(name = "tezos", storages = arrayOf(Storage(value = "tezos.xml")))
class TezosSettingService() : PersistentStateComponent<TezosSettings> {
    @Volatile
    private var settings = TezosSettings()

    companion object {
        @JvmStatic
        fun getInstance(): TezosSettingService {
            return ServiceManager.getService(TezosSettingService::class.java)
        }

        @JvmStatic
        fun getSettings(): TezosSettings {
            return getInstance().state
        }
    }

    @Synchronized
    override fun getState(): TezosSettings {
        return settings
    }

    @Synchronized
    override fun loadState(state: TezosSettings) {
        if (state == null) {
            this.settings = TezosSettings()
        } else {
            this.settings = state
        }
    }
}
package com.tezos.intellij.settings

import java.util.*

interface TezosSettingsListener : EventListener {
    /**
     * Called when the default tezos client was changed or added for the first time.
     */
    fun defaultTezosClientChanged()

    /**
     * Called when the setting of the stack position was modified.
     */
    fun tezosStackPositionChanged()
}
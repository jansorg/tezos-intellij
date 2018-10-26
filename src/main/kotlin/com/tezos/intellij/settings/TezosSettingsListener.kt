package com.tezos.intellij.settings

import java.util.*

interface TezosSettingsListener : EventListener {
    fun defaultTezosClientChanged()
    fun tezosStackPositionChanged()
}
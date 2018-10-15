package com.tezos.intellij.settings

import com.intellij.util.xmlb.annotations.Tag

/**
 * @author jansorg
 */
class TezosSettings {
    @JvmField
    @Tag("clients")
    val clients = mutableListOf<TezosClientConfig>()

    var showStackVisualization = true
    var showStackSplitHorizontal = true

    fun setClients(values: List<TezosClientConfig>) {
        clients.clear()
        clients.addAll(values)
    }

    fun getDefaultClient(): TezosClientConfig? {
        return clients.first { it.isDefault }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TezosSettings

        if (clients != other.clients) return false

        return true
    }

    override fun hashCode(): Int {
        return clients.hashCode()
    }

    override fun toString(): String {
        return "TezosSettings(clients=$clients)"
    }

    fun copy(): TezosSettings {
        val v = TezosSettings()
        v.setClients(clients.map { TezosClientConfig().applyFrom(it) })
        return v
    }
}
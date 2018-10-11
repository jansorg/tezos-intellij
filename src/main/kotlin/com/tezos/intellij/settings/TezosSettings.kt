package com.tezos.intellij.settings

import com.intellij.util.xmlb.annotations.Tag

/**
 * @author jansorg
 */
class TezosSettings {
    @JvmField
    @Tag("clients")
    val clients = mutableListOf<TezosClientConfig>()

    fun setClients(values: List<TezosClientConfig>) {
        clients.clear()
        clients.addAll(values)
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
}
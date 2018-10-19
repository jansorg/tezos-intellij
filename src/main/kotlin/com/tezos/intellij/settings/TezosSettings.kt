package com.tezos.intellij.settings

import com.intellij.util.xmlb.annotations.Tag

enum class StackVisualizationPosition {
    HIDDEN, RIGHT, BOTTOM;

    override fun toString(): String {
        return when (this) {
            HIDDEN -> "Don't show"
            RIGHT -> "Right of the editor"
            BOTTOM -> "Below the editor"
        }
    }

    fun isVerticalSplit(): Boolean {
        return this == BOTTOM
    }
}

/**
 * @author jansorg
 */
class TezosSettings {
    @JvmField
    @Tag("clients")
    val clients = mutableListOf<TezosClientConfig>()

    @JvmField
    @Tag("stackPosition")
    var stackPanelPosition: StackVisualizationPosition = StackVisualizationPosition.RIGHT

    fun setClients(values: List<TezosClientConfig>) {
        clients.clear()
        clients.addAll(values)
    }

    val showStackVisualization: Boolean
        get() {
            return stackPanelPosition != StackVisualizationPosition.HIDDEN
        }

    fun getDefaultClient(): TezosClientConfig? {
        return clients.first { it.isDefault }
    }

    fun copy(): TezosSettings {
        val v = TezosSettings()
        v.setClients(clients.map { TezosClientConfig().applyFrom(it) })
        v.stackPanelPosition = stackPanelPosition
        return v
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TezosSettings

        if (clients != other.clients) return false
        if (stackPanelPosition != other.stackPanelPosition) return false

        return true
    }

    override fun hashCode(): Int {
        var result = clients.hashCode()
        result = 31 * result + stackPanelPosition.hashCode()
        return result
    }

    override fun toString(): String {
        return "TezosSettings(clients=$clients, stackPanelPosition=$stackPanelPosition)"
    }
}
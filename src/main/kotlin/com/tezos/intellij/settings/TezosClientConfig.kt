package com.tezos.intellij.settings

import com.intellij.util.xmlb.annotations.Tag

/**
 * @author jansorg
 */
@Tag("client")
class TezosClientConfig {
    @JvmField
    var name: String? = null
    @JvmField
    var executablePath: String? = null
    @JvmField
    var isDockerScript: Boolean = false

    fun applyFrom(c: TezosClientConfig): TezosClientConfig {
        this.name = c.name
        this.executablePath = c.executablePath
        this.isDockerScript = c.isDockerScript
        return this
    }

    override fun toString(): String {
        return "TezosClientConfig(name=$name, executablePath=$executablePath, isDockerScript=$isDockerScript)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TezosClientConfig

        if (name != other.name) return false
        if (executablePath != other.executablePath) return false
        if (isDockerScript != other.isDockerScript) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (executablePath?.hashCode() ?: 0)
        result = 31 * result + isDockerScript.hashCode()
        return result
    }
}

package com.tezos.intellij.settings

import com.intellij.util.xmlb.annotations.Tag
import org.jetbrains.annotations.NotNull

/**
 * @author jansorg
 */
@Tag("client")
class TezosClientConfig(@JvmField @NotNull var name: String = "",
                        @JvmField @NotNull var executablePath: String = "",
                        @JvmField @NotNull var isDefault: Boolean = false) {

    val isScriptClient: Boolean
        get() {
            return executablePath.endsWith(".sh")
        }

    fun applyFrom(c: TezosClientConfig): TezosClientConfig {
        this.name = c.name
        this.executablePath = c.executablePath
        this.isDefault = c.isDefault
        return this
    }

    override fun toString(): String {
        return "TezosClientConfig(name=$name, executablePath=$executablePath, isDefault=$isDefault)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TezosClientConfig

        if (name != other.name) return false
        if (executablePath != other.executablePath) return false
        if (isDefault != other.isDefault) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + executablePath.hashCode()
        result = 31 * result + isDefault.hashCode()
        return result
    }
}

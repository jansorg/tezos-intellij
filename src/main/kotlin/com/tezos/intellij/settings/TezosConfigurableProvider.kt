package com.tezos.intellij.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurableProvider

/**
 * @author jansorg
 */
class TezosConfigurableProvider : ConfigurableProvider() {
    override fun createConfigurable(): Configurable? {
        return TezosConfigurable()
    }
}
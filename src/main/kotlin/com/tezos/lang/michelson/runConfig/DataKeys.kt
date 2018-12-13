package com.tezos.lang.michelson.runConfig

import com.intellij.openapi.util.Key

/**
 * @author jansorg
 */
object DataKeys {
    val STORAGE_INPUT = Key.create<String>("tezos.storageInput")!!
    val PARAMETER_INPUT = Key.create<String>("tezos.paramterInput")!!
}
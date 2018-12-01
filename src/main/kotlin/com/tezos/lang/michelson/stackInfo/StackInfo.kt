package com.tezos.lang.michelson.stackInfo

import com.tezos.client.stack.MichelsonStackTransformations

/**
 * Cache entry for a given file content.
 * @author jansorg
 */
data class StackInfo(val stack: MichelsonStackTransformations?, val error: Exception?) {
    constructor(stack: MichelsonStackTransformations) : this(stack, null)
    constructor(error: Exception) : this(null, error)

    init {
        if (stack == null && this.error == null) {
            throw IllegalArgumentException("both argument can't be null")
        }
    }

    val isStack: Boolean
        get() = this.stack != null

    fun getStackOrThrow(): MichelsonStackTransformations {
        if (error != null) {
            throw error
        }
        return stack!!
    }

    val isError: Boolean
        get() = this.error != null
}
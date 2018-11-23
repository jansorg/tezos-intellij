package com.tezos.lang.michelson.lang.macro

import com.tezos.client.stack.MichelsonStackFrame
import com.tezos.client.stack.MichelsonStackType

/**
 * @author jansorg
 */
object Comparables {
    val BOOL = MichelsonStackType("bool")

    fun isSame(a: MichelsonStackType?, b: MichelsonStackType?): Boolean {
        return a != null && b != null && a.isComparable && a.name == b.name
    }

    fun isSame(a: MichelsonStackFrame?, b: MichelsonStackFrame?): Boolean {
        return isSame(a?.type, b?.type)
    }
}
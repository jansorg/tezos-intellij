package com.tezos.lang.michelson.lang.macro

import com.tezos.client.stack.MichelsonStackFrame
import com.tezos.client.stack.MichelsonStackType

/**
 * @author jansorg
 */
object Comparables {
    val BOOL = MichelsonStackType("bool")
    val INT = MichelsonStackType("int")
    val OPTION = MichelsonStackType("option")

    fun isSame(a: MichelsonStackType?, b: MichelsonStackType?): Boolean {
        return a != null && b != null && a.name == b.name
    }

    fun isSame(a: MichelsonStackFrame?, b: MichelsonStackFrame?): Boolean {
        return isSame(a?.type, b?.type)
    }

    fun isSame(a: MichelsonStackType?, b: MichelsonStackFrame?): Boolean {
        return isSame(a, b?.type)
    }

    fun isSame(a: MichelsonStackFrame?, b: MichelsonStackType?): Boolean {
        return isSame(a?.type, b)
    }
}
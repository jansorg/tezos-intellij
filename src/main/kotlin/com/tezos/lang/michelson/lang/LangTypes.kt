package com.tezos.lang.michelson.lang

import com.tezos.client.stack.MichelsonStackType

object LangTypes {
    private fun String.asType(vararg subTypes: MichelsonStackType) = MichelsonStackType(this, subTypes.toList())

    // this is a special type which is used in transformations as a placeholder
    val ANY = "*".asType()
    // a special type to signal that the rest of the program will always fail
    val FAILED = "[FAILED]".asType()

    val INT = "int".asType()
    val NAT = "nat".asType()
    val STRING = "string".asType()
    val BYTES = "bytes".asType()
    val BOOL = "bool".asType()
    val UNIT = "unit".asType()

    val TIMESTAMP = "timestamp".asType()
    val MUTEZ = "mutez".asType()
    val ADDRESS = "address".asType()
    val OPERATION = "operation".asType()
    val KEY = "key".asType()
    val KEY_HASH = "key_hash".asType()
    val SIGNATURE = "signature".asType()

    fun CONTRACT(type: MichelsonStackType) = "contract".asType(type)
    fun PAIR(left: MichelsonStackType, right: MichelsonStackType) = "pair".asType(left, right)
    fun OPTION(type: MichelsonStackType) = "option".asType(type)
    fun LIST(type: MichelsonStackType) = "list".asType(type)
    fun SET(type: MichelsonStackType) = "set".asType(type)
    fun OR(a: MichelsonStackType, b: MichelsonStackType) = "or".asType(a, b)
    fun LAMBDA(a: MichelsonStackType, b: MichelsonStackType) = "lambda".asType(a, b)
    fun MAP(a: MichelsonStackType, b: MichelsonStackType) = "map".asType(a, b)
    fun BIG_MAP(a: MichelsonStackType, b: MichelsonStackType) = "map".asType(a, b)
}
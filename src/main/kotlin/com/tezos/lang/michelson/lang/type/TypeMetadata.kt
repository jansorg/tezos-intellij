package com.tezos.lang.michelson.lang.type

import com.tezos.lang.michelson.lang.ParameterType

/**
 * Metadata about a single type, e.g. about `int` or 'pair'.
 *
 * @author jansorg
 */
interface TypeMetadata {
    val name: String

    val type: ParameterType

    val subtypes: List<ParameterType>

    val isComparable: Boolean
        get() = type == ParameterType.COMPARABLE_TYPE

    /**
     * 'int` is a simmple type.
     * 'pair' is not a simple type, but a nesting type.
     */
    val isSimple: Boolean
        get() = subtypes.isEmpty()

    /**
     * 'int` is a simmple type.
     * 'pair' is not a simple type, but a nesting type.
     */
    val isNesting: Boolean
        get() = subtypes.isNotEmpty()
}

data class SimpleTypeMetadata(override val name: String, override val type: ParameterType, override val subtypes: List<ParameterType> = emptyList()) : TypeMetadata
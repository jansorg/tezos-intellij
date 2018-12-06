package com.tezos.lang.michelson.lang.type

import com.tezos.lang.michelson.lang.ParameterType

/**
 * @author jansorg
 */
interface TypeMetadata {
    val isComparable: Boolean
        get() = type == ParameterType.COMPARABLE_TYPE

    val isSimple: Boolean
        get() = subtypes.isEmpty()

    val isNesting: Boolean
        get() = subtypes.isNotEmpty()

    val name: String

    val type: ParameterType

    val subtypes: List<ParameterType>
}

data class SimpleTypeMetadata(override val name: String, override val type: ParameterType, override val subtypes: List<ParameterType> = emptyList()) : TypeMetadata
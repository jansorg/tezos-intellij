package com.tezos.lang.michelson.lang.instruction

import com.tezos.lang.michelson.lang.ParameterType

/**
 * @author jansorg
 */
interface InstructionMetadata {
    /**
     * The name of this instruction
     */
    val name: String

    /**
     * The list of supported parameters
     */
    val parameters: List<ParameterType>
}

data class SimpleInstruction(override val name: String, override val parameters: List<ParameterType>) : InstructionMetadata {
    constructor(name: String, vararg params: ParameterType) : this(name, params.toList())
}

package com.tezos.lang.michelson.lang.instruction

import com.tezos.lang.michelson.lang.AnnotationType
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

    fun count(type: ParameterType): Int {
        return parameters.count { it == type }
    }

    fun supportsAnnotations(): Boolean

    fun supportedAnnotations(type: AnnotationType): Short

    val predefinedAnnotations: List<NamedAnnotation>
}

data class NamedAnnotation(val type: AnnotationType, val name: String)

data class SimpleInstruction(override val name: String, override val parameters: List<ParameterType>, val annotations: Map<AnnotationType, Short>, override val predefinedAnnotations: List<NamedAnnotation>) : InstructionMetadata {
    constructor(name: String, vararg params: ParameterType) : this(name, params.toList(), emptyMap(), emptyList())

    override fun supportsAnnotations(): Boolean {
        return annotations.isNotEmpty() && annotations.filterValues { it > 0 }.isNotEmpty()
    }

    override fun supportedAnnotations(type: AnnotationType): Short {
        return annotations.getOrDefault(type, 0)
    }
}

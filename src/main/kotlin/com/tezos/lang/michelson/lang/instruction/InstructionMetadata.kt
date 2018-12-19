package com.tezos.lang.michelson.lang.instruction

import com.tezos.client.stack.MichelsonStack
import com.tezos.client.stack.MichelsonStackType
import com.tezos.lang.michelson.lang.AnnotationType
import com.tezos.lang.michelson.lang.ParameterType
import com.tezos.lang.michelson.lang.StackTransformation
import com.tezos.lang.michelson.lang.UnsupportedTransformation

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

    fun isAvailable(stack: MichelsonStack): Boolean

    /**
     * Returns the result of this instruction applied to the input stack.
     *
     * @throws UnsupportedOperationException Thrown when the input stack isn't fulfilling the requirements to perform this operation
     */
    fun transformStack(stack: MichelsonStack, argTypes: List<MichelsonStackType>): MichelsonStack
}

data class NamedAnnotation(val type: AnnotationType, val name: String)

data class SimpleInstruction(override val name: String, override val parameters: List<ParameterType>, private val supportedAnnotations: Map<AnnotationType, Short>, override val predefinedAnnotations: List<NamedAnnotation>, private val transformation: StackTransformation = UnsupportedTransformation) : InstructionMetadata {
    constructor(name: String, vararg params: ParameterType) : this(name, params.toList(), emptyMap(), emptyList())

    override fun supportsAnnotations(): Boolean {
        return supportedAnnotations.isNotEmpty() && supportedAnnotations.filterValues { it > 0 }.isNotEmpty()
    }

    override fun supportedAnnotations(type: AnnotationType): Short {
        return supportedAnnotations.getOrDefault(type, 0)
    }

    override fun isAvailable(stack: MichelsonStack): Boolean {
        return transformation.supports(stack)
    }

    override fun transformStack(stack: MichelsonStack, argTypes: List<MichelsonStackType>): MichelsonStack {
        return transformation.transform(this, stack, argTypes)
    }

    fun with(transformation: StackTransformation) = copy(transformation = transformation)
}

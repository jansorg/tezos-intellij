package com.tezos.client.stack

/**
 * Represents all stack transformations and errors for a single file.
 */
data class MichelsonStackTransformations(val transformations: List<MichelsonStackTransformation>, val errors: List<MichelsonStackError>) {
    val hasErrors: Boolean
        get() = errors.isNotEmpty()

    fun elementAt(offset: Int): MichelsonStackTransformation? {
        return transformations.lastOrNull {
            offset >= it.tokenStartOffset && offset <= it.tokenEndOffset
        }
    }
}

/**
 * Represents a single stack transformation. It contains the offset of instruction and the stacks before and after the instruction was executed on the stack.
 */
data class MichelsonStackTransformation(val tokenStartOffset: Int, val tokenEndOffset: Int, val before: MichelsonStack, val after: MichelsonStack)

/**
 * Represents a state of the stack. Each frame represents one item on the stack. The first item is the top of the stack.
 */
data class MichelsonStack(val frames: List<MichelsonStackFrame>) {
    val size: Int
        get() = frames.size
}

/**
 * Represents a single element on a stack.
 */
data class MichelsonStackFrame(val type: MichelsonStackType) {
    fun equals(other: MichelsonStackFrame, withAnnotations: Boolean): Boolean {
        return type.equals(other.type, withAnnotations)
    }
}

/**
 * Represents a type which is usually a stack frame.
 * @param name The name of the type
 * @param arguments The type arguments if this type is a parametrized type, e.g. "map int string".
 * @param annotations Optional list of annotations which contains the annotations attached to this type.
 */
data class MichelsonStackType(val name: String, val arguments: List<MichelsonStackType>, val annotations: List<MichelsonStackAnnotation>) {
    fun equals(other: MichelsonStackType, withAnnotations: Boolean): Boolean {
        if (this === other) {
            return true
        }

        if (name != other.name) {
            return false
        }

        if (this.arguments != other.arguments) {
            return false
        }

        if (withAnnotations && annotations != other.annotations) {
            return false
        }

        return true
    }
}

/**
 * Represents a single annotation.
 */
data class MichelsonStackAnnotation(val value: String)

/**
 * Represents an error returned by the Tezos client.
 */
data class MichelsonStackError(val startOffset: Int, val endOffset: Int, val msg: String)
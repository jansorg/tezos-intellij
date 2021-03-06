package com.tezos.client.stack

import com.tezos.lang.michelson.lang.MichelsonLanguage

/**
 * Represents all stack transformations and errors for a single file.
 * This data structure requires a sorted list of transformations. It must be sorted by start offset. Elements may be nested but must not overlap.
 * This means that earlier elements may cover the range of elements later in the list.
 */
data class MichelsonStackTransformations(val transformations: List<MichelsonStackTransformation>, val errors: List<MichelsonStackError>) {
    val hasErrors: Boolean
        get() = errors.isNotEmpty()

    /**
     * Returns true if the offset is only matched by the first element and not by an actual element in the list
     */
    fun isOnWhitespace(offset: Int): Boolean {
        val matchingIndex = elementIndexAt(offset)
        return matchingIndex == -1 || matchingIndex != transformations.indexOfLast { it.tokenStartOffset <= offset }
    }

    fun elementAt(offset: Int): MichelsonStackTransformation? {
        return transformations.lastOrNull {
            offset >= it.tokenStartOffset && offset <= it.tokenEndOffset
        }
    }

    private fun elementIndexAt(offset: Int): Int {
        return transformations.indexOfLast {
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
    companion object {
        val EMPTY = MichelsonStack(emptyList())
    }

    val isEmpty: Boolean
        get() = frames.isEmpty()

    val size: Int
        get() = frames.size

    val top: MichelsonStackFrame?
        get() = frames.getOrNull(0)

    fun drop(count: Int) = copy(frames = frames.drop(count))

    fun push(newFrame: MichelsonStackFrame): MichelsonStack = copy(frames = listOf(newFrame) + frames)
    fun push(newType: MichelsonStackType): MichelsonStack = copy(frames = listOf(MichelsonStackFrame(newType)) + frames)

    fun push(newFrames: List<MichelsonStackFrame>): MichelsonStack = copy(frames = newFrames + frames)
    fun pushTypes(newFrames: List<MichelsonStackType>): MichelsonStack = copy(frames = newFrames.map { MichelsonStackFrame(it) } + frames)
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
data class MichelsonStackType(val name: String, val arguments: List<MichelsonStackType> = emptyList(), val annotations: List<MichelsonStackAnnotation> = emptyList()) {
    val isComparable: Boolean
        get() {
            return MichelsonLanguage.TYPES.firstOrNull { it.name == name }?.isComparable ?: false
        }

    /**
     * Renders the type as a string.
     * This does not include annotations.
     */
    fun asString(showNested: Boolean): String {
        if (arguments.isEmpty() || !showNested) {
            return name
        }

        val (prefix, suffix) = when (name.isNotEmpty()) {
            true -> "($name" to ")"
            false -> "" to ""
        }

        val result = StringBuilder()
        result.append(prefix)
        for (a in arguments) {
            if (result.isNotEmpty()) {
                result.append(" ")
            }
            result.append(a.asString(true))
        }
        result.append(suffix)
        return result.toString()
    }

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
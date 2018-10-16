package com.tezos.client.stack

data class MichelsonStackTransformations(val transformations: List<MichelsonStackTransformation>, val errors: List<MichelsonStackError>) {
    val hasErrors: Boolean
        get() = errors.isNotEmpty()

    fun elementAt(offset: Int): MichelsonStackTransformation? {
        return transformations.lastOrNull {
            offset >= it.tokenStartOffset && offset <= it.tokenEndOffset
        }
    }
}

data class MichelsonStackTransformation(val tokenStartOffset: Int, val tokenEndOffset: Int, val before: MichelsonStack, val after: MichelsonStack)

data class MichelsonStack(val frames: List<MichelsonStackFrame>) {
    val size: Int
        get() = frames.size
}

data class MichelsonStackFrame(val type: MichelsonStackType)

data class MichelsonStackType(val name: String, val arguments: List<MichelsonStackType>, val annotations: List<MichelsonStackAnnotation>) {
    fun asString(showAnnotations: Boolean = false): String {
        val wrap = arguments.size >= 1 && name.isNotEmpty()

        val (prefix, suffix) = when (wrap) {
            true -> arrayOf("(", ")")
            false -> arrayOf("", "")
        }

        var out = prefix + name + " " + arguments.map { it.asString(showAnnotations) }.joinToString(" ")
        if (showAnnotations) {
            out += annotations.map { it.value }.joinToString(" ")
        }

        return out.trim() + suffix
    }
}

data class MichelsonStackAnnotation(val value: String)

data class MichelsonStackError(val startOffset: Int, val endOffset: Int, val msg: String)
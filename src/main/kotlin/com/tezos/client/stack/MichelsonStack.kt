package com.tezos.client.stack

data class MichelsonStackTransformations(val transformations: List<MichelsonStackTransformation>) {
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

data class MichelsonStackType(val name: String, val arguments: List<MichelsonStackType>, val annotations: List<MichelsonStackAnnotation>)

data class MichelsonStackAnnotation(val value: String)
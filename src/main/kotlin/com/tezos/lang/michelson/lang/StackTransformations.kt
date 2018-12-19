package com.tezos.lang.michelson.lang

import com.tezos.client.stack.MichelsonStack
import com.tezos.client.stack.MichelsonStackFrame
import com.tezos.client.stack.MichelsonStackType
import com.tezos.lang.michelson.lang.instruction.InstructionMetadata

/**
 * A function transforming the current stack. The returned function may throw UnsupportedOperationException
 * when the transformation of the input stack is not supported.
 * A stack is immutable, thus a new stack is returned and the input stack is never modified when the returned function is applied to it.
 */

interface StackTransformation {
    fun supports(stack: MichelsonStack): Boolean
    /**
     * @param stack The input stack which is passed to the instruction doing the transformation
     * @param argTypes The types of the arguments passed to this instruction. The type of instruction blocks is the type of its last instruction. Arguemtns which don't specify a type, e.g. a literal, are not contained in this list.
     * @throws UnsupportedOperationException
     */
    fun transform(meta: InstructionMetadata, stack: MichelsonStack, argTypes: List<MichelsonStackType>): MichelsonStack
}

object UnsupportedTransformation : StackTransformation {
    override fun supports(stack: MichelsonStack): Boolean {
        return false
    }

    override fun transform(meta: InstructionMetadata, stack: MichelsonStack, argTypes: List<MichelsonStackType>): MichelsonStack {
        throw UnsupportedOperationException("unsupported")
    }
}

/**
 * Transforms a pair with a configurable operation.
 */
data class DroppingTransformation(private val count: Int) : StackTransformation {
    override fun supports(stack: MichelsonStack): Boolean {
        return stack.size > 0
    }

    override fun transform(meta: InstructionMetadata, stack: MichelsonStack, argTypes: List<MichelsonStackType>): MichelsonStack {
        return stack.drop(count)
    }
}

/**
 * Transforms a pair with a configurable operation.
 */
data class PairTransformation(private val op: MichelsonStackType.() -> MichelsonStackType) : StackTransformation {
    override fun supports(stack: MichelsonStack): Boolean {
        return stack.size >= 1 && stack.top!!.type.name == "pair"
    }

    override fun transform(meta: InstructionMetadata, stack: MichelsonStack, argTypes: List<MichelsonStackType>): MichelsonStack {
        val pair = stack.top!!.type
        val result = op(pair)
        return stack.drop(1).push(result)
    }
}

/**
 * Transforms the top item of the stack.
 */
data class TopItemTransformation(private val op: (item: MichelsonStackType, argTypes: List<MichelsonStackType>) -> List<MichelsonStackType>) : StackTransformation {
    override fun supports(stack: MichelsonStack): Boolean {
        return stack.size >= 1
    }

    override fun transform(meta: InstructionMetadata, stack: MichelsonStack, argTypes: List<MichelsonStackType>): MichelsonStack {
        val pair = stack.top!!.type
        val result = op(pair, argTypes)
        return stack.drop(1).pushTypes(result)
    }
}

/**
 * Transforms the top item of the stack.
 */
data class NoItemTransformation(private val op: (argTypes: List<MichelsonStackType>) -> List<MichelsonStackType>) : StackTransformation {
    override fun supports(stack: MichelsonStack): Boolean {
        return true
    }

    override fun transform(meta: InstructionMetadata, stack: MichelsonStack, argTypes: List<MichelsonStackType>): MichelsonStack {
        val result = op(argTypes)
        return stack.pushTypes(result)
    }
}

/**
 * Transforms the top item of the stack.
 */
data class TwoTopItemsTransformation(private val op: (first: MichelsonStackType, second: MichelsonStackType, argTypes: List<MichelsonStackType>) -> List<MichelsonStackType>) : StackTransformation {
    override fun supports(stack: MichelsonStack): Boolean {
        return stack.size >= 2
    }

    override fun transform(meta: InstructionMetadata, stack: MichelsonStack, argTypes: List<MichelsonStackType>): MichelsonStack {
        val first = stack.frames[0].type
        val second = stack.frames[1].type
        val result = op(first, second, argTypes)
        return stack.drop(2).pushTypes(result)
    }
}

/**
 * Transforms the top item of the stack.
 */
data class TopThreeItemsTransformation(private val op: (first: MichelsonStackType, second: MichelsonStackType, third: MichelsonStackType, argTypes: List<MichelsonStackType>) -> List<MichelsonStackType>) : StackTransformation {
    override fun supports(stack: MichelsonStack): Boolean {
        return stack.size >= 3
    }

    override fun transform(meta: InstructionMetadata, stack: MichelsonStack, argTypes: List<MichelsonStackType>): MichelsonStack {
        val first = stack.frames[0].type
        val second = stack.frames[1].type
        val third = stack.frames[2].type
        val result = op(first, second, third, argTypes)
        return stack.drop(2).pushTypes(result)
    }
}

data class SimpleStackTransformation(private val definitions: List<Pair<List<MichelsonStackType>, List<MichelsonStackType>>>) : StackTransformation {
    private val minSize = definitions.firstOrNull()?.first?.size ?: 0

    init {
        // we can safely assume that all definitions have the same size
//        for (definition in definitions) {
//            if (definition.first.size != minSize || definition.second.size != minSize) {
//                throw IllegalArgumentException("transformation definition are not all of the same size")
//            }
//        }
    }

    override fun supports(stack: MichelsonStack): Boolean {
        return stack.size >= minSize
    }

    override fun transform(meta: InstructionMetadata, stack: MichelsonStack, argTypes: List<MichelsonStackType>): MichelsonStack {
        val stackItems = stack.frames.subList(0, minSize).map { it.type }
        for (d in definitions) {
            if (!matching(stackItems, d.first)) {
                continue
            }

            return stack.drop(minSize).push(d.second.map { MichelsonStackFrame(it) })
        }

        throw UnsupportedOperationException("stack transformation unsupported")
    }

    private fun matching(topItems: List<MichelsonStackType>, expected: List<MichelsonStackType>): Boolean {
        if (topItems.size < expected.size) {
            return false
        }

        topItems.forEachIndexed { i, frame ->
            if (frame != expected[i]) {
                return false
            }
        }

        return true
    }
}

object StackTransformations {
    fun dropping(count: Int): StackTransformation = DroppingTransformation(count)

    fun adding(newType: String): StackTransformation = adding(MichelsonStackType(newType))
    fun adding(newType: MichelsonStackType): StackTransformation = transforming(emptyList<MichelsonStackType>() to listOf(newType))

    @JvmName("transformingNames")
    fun transforming(vararg pairs: Pair<String, String>) = transforming(*pairs.map { Pair(MichelsonStackType(it.first), MichelsonStackType(it.second)) }.toTypedArray())

    @JvmName("transformingNameLists")
    fun transforming(vararg pairs: Pair<List<String>, List<String>>) = transforming(* pairs.map {
        it.first.map { MichelsonStackType(it) } to it.second.map { MichelsonStackType(it) }
    }.toTypedArray())

    @JvmName("transformingNameListsToSingle")
    fun transforming(vararg pairs: Pair<List<String>, String>) = transforming(* pairs.map {
        it.first.map { MichelsonStackType(it) } to listOf(MichelsonStackType(it.second))
    }.toTypedArray())

    @JvmName("transformingNameListsTyoes")
    fun transforming(vararg pairs: Pair<List<MichelsonStackType>, MichelsonStackType>) = transforming(* pairs.map {
        it.first to listOf(it.second)
    }.toTypedArray())

    fun transforming(from: MichelsonStackType, to: MichelsonStackType) = transforming(from to to)

    @JvmName("transformingSingles")
    fun transforming(vararg transformations: Pair<MichelsonStackType, MichelsonStackType>): StackTransformation = transforming(* transformations.map {
        listOf(it.first) to listOf(it.second)
    }.toTypedArray())

    /**
     * Returns a stack transformation function defined by a list of oldType->newType applied to the current top of the stack
     * If no pair is supporting the current stack then UnsupportedOperationException is thrown
     */
    fun transforming(vararg transformations: Pair<List<MichelsonStackType>, List<MichelsonStackType>>): StackTransformation = SimpleStackTransformation(transformations.toList())
}


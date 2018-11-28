package com.tezos.lang.michelson.lang.macro

import com.tezos.client.stack.MichelsonStackFrame
import com.tezos.client.stack.MichelsonStackType

/**
 * @author jansorg
 */
object Comparables {
    val BOOL = MichelsonStackType("bool")
    val INT = MichelsonStackType("int")
    val OPTION = MichelsonStackType("option")
    val PAIR = MichelsonStackType("pair")

    fun isSame(a: MichelsonStackType?, b: MichelsonStackType?): Boolean {
        return a != null && b != null && a.name == b.name
    }

    fun isSame(a: MichelsonStackFrame?, b: MichelsonStackFrame?): Boolean {
        return isSame(a?.type, b?.type)
    }

    fun isSame(a: MichelsonStackType?, b: MichelsonStackFrame?): Boolean {
        return isSame(a, b?.type)
    }

    fun isSame(a: MichelsonStackFrame?, b: MichelsonStackType?): Boolean {
        return isSame(a?.type, b)
    }
}

fun MichelsonStackType.isType(type: MichelsonStackType): Boolean {
    return Comparables.isSame(this, type)
}

fun MichelsonStackFrame.isType(type: MichelsonStackType): Boolean {
    return Comparables.isSame(this.type, type)
}

object Pairs {
    fun addNestedPairAccessors(element: MichelsonStackType, prefix: String, result: MutableList<DynamicMacroName>, resultType: (selector: CharSequence) -> MichelsonStackType?) {
        assert(element.isType(Comparables.PAIR))
        assert(element.arguments.size == 2)
        val (left, right) = element.arguments

        val leftSelector = prefix + "A"
        result += DynamicMacroName(leftSelector, resultType(leftSelector))
        if (left.isType(Comparables.PAIR)) {
            addNestedPairAccessors(left, leftSelector, result, resultType)
        }

        val rightSelector = prefix + "D"
        result += DynamicMacroName(rightSelector, resultType(rightSelector))
        if (right.isType(Comparables.PAIR)) {
            addNestedPairAccessors(right, rightSelector, result, resultType)
        }
    }

    fun transform(selectors: CharSequence,
                  startNode: MichelsonStackType,
                  parents: List<ParentPair> = emptyList(),
                  leafTransform: (selector: Char, parents: List<ParentPair>, pair: MichelsonStackType) -> MichelsonStackType): MichelsonStackType {

        val selectorType = selectors.first()
        assert(selectorType == 'A' || selectorType == 'D')

        return when (selectors.length > 1) {
            true -> {
                assert(startNode.isType(Comparables.PAIR))

                val isLeft = selectorType == 'A'
                val selected = if (isLeft) startNode.arguments[0] else startNode.arguments[1]
                val newParents = parents + ParentPair(startNode, isLeft)

                transform(selectors.subSequence(1, selectors.length), selected, newParents, leafTransform)
            }

            false -> leafTransform(selectorType, parents, startNode)
        }
    }
}

data class ParentPair(val node: MichelsonStackType, val childIsLeft: Boolean) {
    fun wrapChild(child: MichelsonStackType): MichelsonStackType {
        val (left, right) = node.arguments
        return when (childIsLeft) {
            true -> node.copy(arguments = listOf(child, right))
            false -> node.copy(arguments = listOf(left, child))
        }
    }
}
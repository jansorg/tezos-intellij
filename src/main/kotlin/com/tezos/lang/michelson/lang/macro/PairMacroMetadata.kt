package com.tezos.lang.michelson.lang.macro

import com.tezos.client.stack.MichelsonStack
import com.tezos.client.stack.MichelsonStackFrame
import com.tezos.client.stack.MichelsonStackType
import com.tezos.lang.michelson.lang.AnnotationType
import java.util.regex.Pattern

/**
 * Supports the dynamic PAIR macro, as defined at http://tezos.gitlab.io/betanet/whitedoc/michelson.html#syntactic-conveniences.
 * PAIR is "A syntactic sugar for building nested pairs".
 *
 * A good way to quickly figure which macro to use is to mentally parse the macro as
 *  P for pair constructor,
 *  A for left leaf and
 *  I for right leaf.
 * The macro takes as many elements on the stack as there are leaves and constructs a nested pair with the shape given by its name.
 */
class PairMacroMetadata : MacroMetadata {
    internal companion object {
        val regexp = Pattern.compile("P[AIP]+R")!!

        internal fun validateMacro(macro: String, startIndex: Int): Pair<String, Int>? {
            var reqLeft = 0
            var reqRight = 0
            var nextIsLeft = false
            val chars = when {
                startIndex == 0 -> macro.toCharArray()
                else -> macro.substring(startIndex).toCharArray()
            }

            var index = 0
            LOOP@ for (c in chars) {
                when (c) {
                    'P' -> {
                        if (index > 0 && nextIsLeft) {
                            // used as left leaf
                            reqLeft--
                        } else if (index > 0) {
                            // used as right leaf
                            reqRight--
                        }

                        reqLeft++
                        reqRight++
                        nextIsLeft = true // next is a left leaf
                    }
                    'A' -> {
                        if (!nextIsLeft) {
                            return "Left leaf 'A' used where right leaf 'I' or 'P' was expected" to index
                        }
                        reqLeft--
                        nextIsLeft = false // next must be a right leaf or a new pair
                    }
                    'I' -> {
                        if (nextIsLeft) {
                            return "Right leaf 'I' used where left leaf 'A' or 'P' was expected" to index
                        }
                        reqRight--
                    }
                    'R' -> {
                        index++
                        break@LOOP
                    }
                }

                // quit early when too many leafs were used
                when {
                    reqLeft == -1 -> return "Unexpected 'A'" to index
                    reqRight == -1 -> return "Unexpected 'I'" to index
                }
                index++
            }

            return when {
                index < chars.size - 1 -> "Incomplete macro" to index - 1
                reqLeft > 0 -> "Missing left leaf 'A'" to index - 1
                reqRight > 0 -> "Missing right leaf 'I'" to index - 1
                else -> null
            }
        }
    }

    override fun staticNames(): Collection<String> = emptyList()

    override fun dynamicNames(stack: MichelsonStack): Collection<DynamicMacroName> {
        if (stack.size < 2) {
            return emptyList()
        }

        val result = mutableSetOf<String>()
        addNames("", "R", stack.frames, result)
        return result.map {
            DynamicMacroName(it, stackType(it.toMutableList(), stack.frames.toMutableList()))
        }
    }

    /**
     * Adds possible macro names to result which are compatible with the given stack.
     * @return The remaining stack depth
     */
    private fun addNames(prefix: String, suffix: String, stack: List<MichelsonStackFrame>, result: MutableSet<String>) {
        if (stack.size < 2) {
            return
        }

        result += prefix + "PAI" + suffix
        addNames(prefix + "PA", suffix, stack.subList(1, stack.size), result)
        addNames(prefix + "P", "I$suffix", stack.subList(0, stack.size - 1), result)

        // fixme add nested macros for the left and right sides, e.g. PPAIPAIR
    }

    /**
     * @return The result type of the given pair macro. macro and stack are mutable and are modified by this call
     * to simplify the recursive implementation.
     */
    private fun stackType(macro: MutableList<Char>, stack: MutableList<MichelsonStackFrame>): MichelsonStackType {
        assert(macro[0] == 'P')
        macro.removeAt(0)

        val left: MichelsonStackType = when {
            macro[0] == 'A' -> {
                macro.removeAt(0)
                stack.removeAt(0).type
            }
            else -> {
                stackType(macro, stack)
            }
        }

        val right = when {
            macro[0] == 'I' -> {
                macro.removeAt(0)
                stack.removeAt(0).type
            }
            else -> {
                stackType(macro, stack)
            }
        }

        return MichelsonStackType("pair", listOf(left, right))
    }

    override fun validate(macro: String): Pair<String, Int>? {
        if (!regexp.matcher(macro).matches()) {
            return "Invalid PAIR macro. It must match 'P[AIP]+R'. " to 0
        }

        return validateMacro(macro, 0)
    }

    override fun requiredBlocks(): Int = 0

    override fun supportedAnnotations(type: AnnotationType, macro: String): Int {
        return when (type) {
            // one variable annotation allowed for the top-level pair put on the stack
            AnnotationType.VARIABLE -> 1
            // Field annotations for PAIR give names to leaves of the constructed nested pair
            AnnotationType.FIELD -> macro.count { it == 'A' || it == 'I' }
            // one type annotations (unclear in the spec), probably for the top-most value on the stack
            AnnotationType.TYPE -> 1 //fixme not clearly defined in the spec
        }
    }

    override fun helpContentFile(name: String): String? {
        return when (name) {
            "PAIR" -> "pair.txt"
            else -> "pair_macro.txt"
        }
    }

    override fun expand(macro: String, deepExpansion: Boolean): String? {
        if (validate(macro) != null) {
            return null
        }

        if (macro == "PAIR") {
            return null
        }

        return doExpand(macro)
    }

    private fun doExpand(macro: String): String {
        if (macro == "PAIR") {
            return "PAIR"
        }

        val rest = macro.substring(0, macro.length - 1)

        // PA(\right)R / S => DIP ((\right)R) ; PAIR / S
        if (rest.startsWith("PA")) {
            val rightPart = rest.substring(2)
            val right = readRight(rightPart)
            if (right == rightPart.length) {
                val inner = doExpand(rightPart + "R")
                return "DIP{ $inner }; PAIR"
            }
        }

        // P(\left)IR / S => PAIR ; (\left)R / S
        if (rest.endsWith("I")) {
            val leftPart = rest.substring(1, rest.length - 1)
            val left = readLeft(leftPart)
            if (left == leftPart.length) {
                return "PAIR; ${doExpand(leftPart + "R")}"
            }
        }

        // fix rule
        // P(\left)(\right)R => (\right)R ; (\left)R ; PAIR / S

        val left = readLeft(rest.substring(1))
        if (left == -1) {
            return ""
        }

        val right = readRight(rest.substring(1 + left))
        if (right == -1 || left + right + 1 != rest.length) {
            return ""
        }

        val leftEx = doExpand(rest.substring(1, 1 + left) + "R")
        val rightEx = doExpand(rest.substring(1 + left) + "R")
        return "$leftEx; DIP{ $rightEx }; PAIR"
    }

    private fun readLeft(macro: String): Int {
        val c = macro.first()
        if (c == 'A') {
            return 1
        }
        if (c == 'P') {
            return readPair(macro)
        }
        return -1
    }

    private fun readRight(macro: String): Int {
        val c = macro.first()
        if (c == 'I') {
            return 1
        }
        if (c == 'P') {
            return readPair(macro)
        }
        return -1
    }

    private fun readPair(macro: String): Int {
        val c = macro.first()
        if (c != 'P') {
            return -1
        }

        val left = readLeft(macro.substring(1))
        if (left == -1) {
            return -1
        }
        return 1 + left + readRight(macro.substring(1 + left))
    }
}
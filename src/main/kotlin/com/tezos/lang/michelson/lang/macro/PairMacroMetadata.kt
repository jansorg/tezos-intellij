package com.tezos.lang.michelson.lang.macro

import com.tezos.lang.michelson.psi.PsiAnnotationType
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
        val regexp = Pattern.compile("P[AIP]+R")

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
                        index++;
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

    override fun staticMacroName(): Collection<String> = listOf("PAIR")

    override fun validate(macro: String): Pair<String, Int>? {
        if (!regexp.matcher(macro).matches()) {
            return "Invalid PAIR macro. It must match 'P[AIP]+R'. " to 0
        }

        return validateMacro(macro, 0)
    }

    override fun requiredBlocks(): Int = 0

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int {
        val chars = macro.toCharArray()
        return when (type) {
            // one variable annotation allowed for the top-level pair put on the stack
            PsiAnnotationType.VARIABLE -> 1
            // Field annotations for PAIR give names to leaves of the constructed nested pair
            PsiAnnotationType.FIELD -> chars.count { it == 'A' || it == 'I' }
            // one type annotations (unclear in the spec), probably for the top-most value on the stack
            PsiAnnotationType.TYPE -> 1 //fixme not clearly defined in the spec
        }
    }
}
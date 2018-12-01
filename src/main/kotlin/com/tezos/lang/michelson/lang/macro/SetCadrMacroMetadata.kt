package com.tezos.lang.michelson.lang.macro

import com.tezos.client.stack.MichelsonStack
import com.tezos.lang.michelson.psi.PsiAnnotationType
import java.util.regex.Pattern

/**
 * Supports the dynamic SET_CADR macro, as defined at http://tezos.gitlab.io/betanet/whitedoc/michelson.html#syntactic-conveniences.
 * SET_CADR is syntactic sugar for setting fields in nested pairs.
 *
 * The variable annotation on SET_C\[AD]+R and MAP_C\[AD]+R annotates the resulting toplevel pair
 * while its field annotation is used to check that the modified field is the expected one.
 */
class SetCadrMacroMetadata : MacroMetadata {
    private companion object {
        val regexp = Pattern.compile("SET_C[AD]+R")
    }

    override fun staticNames(): Collection<String> = listOf("SET_CDR", "SET_CAR")

    override fun dynamicNames(stack: MichelsonStack): Collection<DynamicMacroName> {
        if (stack.size < 2 || !stack.top!!.isType(Comparables.PAIR)) {
            return emptyList()
        }

        val top = stack.top!!
        val second = stack.frames[1]

        val result = mutableListOf<DynamicMacroName>()
        Pairs.addNestedPairAccessors(top.type, "", result) {
            Pairs.transform(it, top.type) { selector, parents, pair ->
                val (left, right) = when (selector) {
                    'A' -> listOf(second.type, pair.arguments[1])
                    else -> listOf(pair.arguments[0], second.type)
                }

                var n = pair.copy(arguments = listOf(left, right))
                for (p in parents.reversed()) {
                    n = p.wrapChild(n)
                }
                n
            }
        }
        return result.map { it.copy(name = "SET_C${it.name}R") }
    }

    override fun validate(macro: String): Pair<String, Int>? {
        if (!regexp.matcher(macro).matches()) {
            return "Invalid SET_CADR macro. It must match 'SET_C[AD]+R'. " to 0
        }

        // validation against the current stack isn't possible atm, so there's no further validation to do here
        return null
    }

    override fun requiredBlocks(): Int = 0

    override fun helpContentFile(name: String): String? {
        return when (name) {
            "SET_CAR" -> "set_car.txt"
            "SET_CDR" -> "set_cdr.txt"
            else -> "set_cadr_macro.txt"
        }
    }

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int {
        return when (type) {
            PsiAnnotationType.VARIABLE -> 1
            PsiAnnotationType.FIELD -> 1
            PsiAnnotationType.TYPE -> 0
        }
    }

    override fun expand(macro: String, deepExpansion: Boolean): String? {
        return doExpand(macro)
    }

    private fun doExpand(macro: String): String {
        return when {
            macro == "SET_CAR" -> "CDR; SWAP; PAIR"
            macro == "SET_CDR" -> "CAR; PAIR"
            macro.startsWith("SET_C") -> {
                val inner = doExpand("SET_C${macro.substring(6, macro.length - 1)}R")

                if (macro.startsWith("SET_CA")) {
                    "{ DUP; DIP{ CAR; $inner }; CDR; SWAP; PAIR }"
                } else {
                    "{ DUP; DIP{ CDR; $inner }; CAR; PAIR }"
                }
            }
            else -> throw IllegalStateException("unsupported macro $macro")
        }
    }
}
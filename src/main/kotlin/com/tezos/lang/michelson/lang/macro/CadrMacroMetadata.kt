package com.tezos.lang.michelson.lang.macro

import com.tezos.client.stack.MichelsonStack
import com.tezos.client.stack.MichelsonStackType
import com.tezos.lang.michelson.psi.PsiAnnotationType
import java.util.regex.Pattern

/**
 * Supports the dynamic CADR macro, as defined at http://tezos.gitlab.io/betanet/whitedoc/michelson.html#syntactic-conveniences.
 * CADR A syntactic sugar for accessing fields in nested pairs.
 */
class CadrMacroMetadata : MacroMetadata {
    private companion object {
        val regexp = Pattern.compile("C[AD]+R")
    }

    override fun staticNames(): Collection<String> = listOf("CAR", "CDR")

    override fun dynamicNames(stack: MichelsonStack): Collection<DynamicMacroName> {
        if (stack.isEmpty || stack.top!!.type.name != "pair") {
            return emptyList()
        }

        val result = mutableListOf<DynamicMacroName>()
        addPairs(stack.top!!.type, "", result)
        return result.map { it.copy(name = "C${it.name}R") }
    }

    private fun addPairs(element: MichelsonStackType, prefix: String, result: MutableList<DynamicMacroName>) {
        assert(element.name == "pair")
        assert(element.arguments.size == 2)
        val (left, right) = element.arguments

        result += DynamicMacroName(prefix + "A", left)
        if (left.name == "pair") {
            addPairs(left, prefix + "A", result)
        }

        result += DynamicMacroName(prefix + "D", right)
        if (right.name == "pair") {
            addPairs(right, prefix + "D", result)
        }
    }

    override fun validate(macro: String): Pair<String, Int>? {
        if (!regexp.matcher(macro).matches()) {
            return "Invalid CADR macro. It must match 'C[AD]+R'. " to 0
        }

        // validation against the current stack isn't possible atm, so there's no further validation to do here
        return null
    }

    override fun requiredBlocks(): Int = 0

    override fun helpContentFile(name: String): String? {
        return when (name) {
            "CAR" -> "car.txt"
            "CDR" -> "cdr.txt"
            else -> "car_macro.txt"
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
        return when (macro) {
            "CAR", "CDR" -> null
            else -> doExpand(macro)
        }
    }

    private fun doExpand(macro: String): String {
        return when {
            macro == "CAR" -> "CAR"
            macro == "CDR" -> "CDR"
            macro.startsWith("CA") -> "CAR; ${doExpand("C${macro.substring(2)}")}"
            macro.startsWith("CD") -> "CDR; ${doExpand("C${macro.substring(2)}")}"
            else -> throw IllegalStateException("unsupported macro $macro")
        }
    }
}
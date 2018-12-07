package com.tezos.lang.michelson.lang.macro

import com.tezos.client.stack.MichelsonStack
import com.tezos.lang.michelson.lang.AnnotationType
import java.util.regex.Pattern

/**
 * Supports the dynamic CADR macro, as defined at http://tezos.gitlab.io/betanet/whitedoc/michelson.html#syntactic-conveniences.
 * CADR A syntactic sugar for accessing fields in nested pairs.
 */
class CadrMacroMetadata : MacroMetadata {
    private companion object {
        val regexp = Pattern.compile("C[AD]+R")
    }

    override fun staticNames(): Collection<String> = emptyList()

    override fun dynamicNames(stack: MichelsonStack): Collection<DynamicMacroName> {
        if (stack.isEmpty || stack.top!!.type.name != "pair") {
            return emptyList()
        }

        val topType = stack.top!!.type

        val result = mutableListOf<DynamicMacroName>()
        Pairs.addNestedPairAccessors(topType, "", result) {
            Pairs.transform(it, topType) { _, _, pair ->
                pair
            }
        }
        return result.map { it.copy(name = "C${it.name}R") }
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

    override fun supportedAnnotations(type: AnnotationType, macro: String): Int {
        return when (type) {
            AnnotationType.VARIABLE -> 1
            AnnotationType.FIELD -> 1
            AnnotationType.TYPE -> 0
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
            macro == "CAR" || macro == "CDR" -> macro
            macro.startsWith("CA") -> "CAR; ${doExpand("C${macro.substring(2)}")}"
            macro.startsWith("CD") -> "CDR; ${doExpand("C${macro.substring(2)}")}"
            else -> throw IllegalStateException("unsupported macro $macro")
        }
    }
}
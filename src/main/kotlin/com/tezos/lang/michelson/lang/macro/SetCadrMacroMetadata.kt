package com.tezos.lang.michelson.lang.macro

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

    override fun staticMacroName(): Collection<String> = listOf("SET_CDR", "SET_CAR")

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
        return null
    }
}
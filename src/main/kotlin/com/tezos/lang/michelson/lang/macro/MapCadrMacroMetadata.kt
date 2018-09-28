package com.tezos.lang.michelson.lang.macro

import com.tezos.lang.michelson.psi.PsiAnnotationType
import java.util.regex.Pattern

/**
 * Supports the dynamic MAP_CADR macro, as defined at http://tezos.gitlab.io/betanet/whitedoc/michelson.html#syntactic-conveniences.
 * MAP_CADR is syntactic sugar for setting fields in nested pairs.
 *
 * MAP_CAR code: Transform the left value of a pair.
 * MAP_CDR code: Transform the right value of a pair.
 * MAP_C\[AD]+R code: A syntactic sugar for transforming fields in nested pairs.
 *
 * The variable annotation on SET_C\[AD]+R and MAP_C\[AD]+R annotates the resulting toplevel pair
 * while its field annotation is used to check that the modified field is the expected one.
 */
class MapCadrMacroMetadata : MacroMetadata {
    private companion object {
        val regexp = Pattern.compile("MAP_C[AD]+R")
    }

    override fun staticMacroName(): Collection<String> = listOf("MPA_CDR", "MAP_CAR")

    override fun validate(macro: String): Pair<String, Int>? {
        if (!regexp.matcher(macro).matches()) {
            return "Invalid MAP_CADR macro. It must match 'MAP_C[AD]+R'. " to 0
        }

        // validation against the current stack isn't possible atm, so there's no further validation to do here
        return null
    }

    override fun requiredBlocks(): Int = 1

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int {
        return when (type) {
            PsiAnnotationType.VARIABLE -> 1
            PsiAnnotationType.FIELD -> 1
            PsiAnnotationType.TYPE -> 0
        }
    }
}
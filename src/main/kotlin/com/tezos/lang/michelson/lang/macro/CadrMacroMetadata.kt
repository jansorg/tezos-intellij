package com.tezos.lang.michelson.lang.macro

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

    override fun staticMacroName(): Collection<String> = listOf("CAR", "CDR")

    override fun validate(macro: String): Pair<String, Int>? {
        if (!regexp.matcher(macro).matches()) {
            return "Invalid CADR macro. It must match 'C[AD]+R'. " to 0
        }

        // validation against the current stack isn't possible atm, so there's no further validation to do here
        return null
    }

    override fun requiredBlocks(): Int = 0

    override fun helpContentFile(name: String): String? = null

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
package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.psi.PsiAnnotationType
import java.util.*
import java.util.regex.Pattern

/**
 * Supports the DUU+p macro, as defined at http://tezos.gitlab.io/betanet/whitedoc/michelson.html#syntactic-conveniences.
 * Duup is "A syntactic sugar for duplicating the nth element of the stack.".
 */
class DupMacroMetadata : MacroMetadata {
    private companion object {
        val regexp = Pattern.compile("DU+P")
    }

    override fun validate(macro: String): Pair<String, Int>? {
        return if (regexp.matcher(macro).matches()) {
            null
        } else {
            "Macro doesn't match DU+P" to 0
        }
    }

    override fun requiredBlocks(): Int = 0

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int {
        return when (type) {
            // macros that produce n values on the stack accept n variable annotations, DUUP produces one
            PsiAnnotationType.VARIABLE -> 1
            else -> 0
        }
    }
}
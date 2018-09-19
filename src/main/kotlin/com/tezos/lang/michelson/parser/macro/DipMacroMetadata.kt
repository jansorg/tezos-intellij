package com.tezos.lang.michelson.parser.macro

import com.tezos.lang.michelson.psi.PsiAnnotationType
import java.util.regex.Pattern

/**
 * Supports the Dii+p macro, as defined at http://tezos.gitlab.io/betanet/whitedoc/michelson.html#syntactic-conveniences.
 * Diip is "A syntactic sugar for working deeper in the stack".
 */
class DipMacroMetadata : MacroMetadata {
    private companion object {
        val regexp = Pattern.compile("DI+P")
    }

    override fun validate(macro: String): Pair<String, Int>? {
        return if (regexp.matcher(macro).matches()) {
            null
        } else {
            "Macro doesn't match DI+P" to 0
        }
    }

    override fun requiredBlocks(): Int = 1

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int = 0
}
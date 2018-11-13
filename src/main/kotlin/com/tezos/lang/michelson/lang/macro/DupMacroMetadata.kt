package com.tezos.lang.michelson.lang.macro

import com.tezos.lang.michelson.psi.PsiAnnotationType
import java.util.regex.Pattern

/**
 * Supports the DUU+p macro, as defined at http://tezos.gitlab.io/betanet/whitedoc/michelson.html#syntactic-conveniences.
 * Duup is "A syntactic sugar for duplicating the nth element of the stack.".
 */
class DupMacroMetadata : MacroMetadata {
    private companion object {
        val regexp = Pattern.compile("DUU+P")
    }

    override fun staticMacroName(): Collection<String> = emptyList()

    override fun validate(macro: String): Pair<String, Int>? {
        return if (regexp.matcher(macro).matches()) {
            null
        } else {
            "Macro doesn't match DUU+P" to 0
        }
    }

    override fun requiredBlocks(): Int = 0

    override fun helpContentFile(name: String): String? = "dup.txt"

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int {
        return when (type) {
            // macros that produce n values on the stack accept n variable annotations, DUUP produces one
            PsiAnnotationType.VARIABLE -> 1
            else -> 0
        }
    }

    override fun expand(macro: String, deepExpansion: Boolean): String? {
        if (validate(macro) != null) {
            return null
        }

        // DUP is an instruction
        if (macro == "DUP") {
            return null
        }

        val levels = macro.count { it == 'U' }

        val result = StringBuilder("DUP")
        for (i in 0 until levels - 1) {
            result.insert(0, "DIP{")
            result.append("}; SWAP")
        }
        return result.toString()
    }
}
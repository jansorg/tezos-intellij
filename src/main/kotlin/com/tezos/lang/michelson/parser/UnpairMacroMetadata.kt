package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.psi.PsiAnnotationType
import java.util.regex.Pattern

/**
 * Supports the dynamic UNPAIR macro, as defined at http://tezos.gitlab.io/betanet/whitedoc/michelson.html#syntactic-conveniences.
 * UNPAIR is "A syntactic sugar for destructing nested pairs. These macros follow the same convention as the previous one (PAIR)".
 *
 * A good way to quickly figure which macro to use is to mentally parse the macro as
 *  P for pair constructor,
 *  A for left leaf and
 *  I for right leaf.
 * The macro takes as many elements on the stack as there are leaves and constructs a nested pair with the shape given by its name.
 */
class UnpairMacroMetadata : MacroMetadata {
    private companion object {
        val regexp = Pattern.compile("UNP[AIP]+R")
    }

    override fun validate(macro: String): Pair<String, Int>? {
        if (!regexp.matcher(macro).matches()) {
            return "Invalid PAIR macro. It must match 'P[AIP]+R'. " to 0
        }

        // valid the part after UN with the same logic as with PAIR, i.e. UNPAPAIR -> validate PAPAIR
        return PairMacroMetadata.validateMacro(macro, 2)
    }

    override fun requiredBlocks(): Int = 0

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int {
        val chars = macro.toCharArray()
        return when (type) {
            // variable annotations for each variable put on the stack by deconstruction the pairs
            PsiAnnotationType.VARIABLE -> chars.count { it == 'A' || it == 'I' }
            // fixme It's unclear wh FIELD annotations are supported for UNPAIR,
            // fixme the spec doesn't mention this, but test/data/contracts/tezos-repo/unpair_macro.tz contains this and is successfully validated by the tezos client
            PsiAnnotationType.FIELD -> chars.count { it == 'A' || it == 'I' }
            // no type annotations are supported by UNPAIR
            PsiAnnotationType.TYPE -> 0
        }
    }
}
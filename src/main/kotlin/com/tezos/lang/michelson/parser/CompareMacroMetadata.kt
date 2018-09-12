package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.psi.PsiAnnotationType

class CompareMacroMetadata : MacroMetadata {
    internal companion object {
        val NAMES = setOf("CMPEQ", "CMPNEQ", "CMPLT", "CMPGT", "CMPLE", "CMPGE")
    }

    override fun validate(macro: String): Pair<String, Int>? {
        return if (macro in NAMES) {
            null
        } else {
            "Unsupported macro" to 0
        }
    }

    override fun requiredBlocks(): Int = 0

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int = 0
}
package com.tezos.lang.michelson.parser

import com.tezos.lang.michelson.psi.PsiAnnotationType

class ConditionalMacroMetadata : MacroMetadata {
    companion object {
        val NAMES = setOf("IFEQ", "IFNEQ", "IFLT", "IFGT", "IFLE", "IFGE",
                "IFCMPEQ", "IFCMPNEQ", "IFCMPLT", "IFCMPGT", "IFCMPLE", "IFCMPGE",
                "IF_SOME")
    }

    override fun validate(macro: String): Pair<String, Int>? {
        return if (macro in NAMES) {
            null
        } else {
            "Unsupported macro" to 0
        }
    }

    override fun requiredBlocks(): Int = 2

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int = 0
}
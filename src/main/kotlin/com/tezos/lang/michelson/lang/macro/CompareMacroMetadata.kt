package com.tezos.lang.michelson.lang.macro

import com.tezos.lang.michelson.psi.PsiAnnotationType

class CompareMacroMetadata : MacroMetadata {
    companion object {
        val NAMES = setOf("CMPEQ", "CMPNEQ", "CMPLT", "CMPGT", "CMPLE", "CMPGE")
    }

    override fun staticNames(): Collection<String> = NAMES

    override fun validate(macro: String): Pair<String, Int>? {
        return if (macro in NAMES) {
            null
        } else {
            "Unsupported macro" to 0
        }
    }

    override fun requiredBlocks(): Int = 0

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int = 0

    override fun helpContentFile(name: String): String? = "cmp.txt"

    override fun expand(macro: String, deepExpansion: Boolean): String? {
        if (validate(macro) != null) {
            return null
        }

        val name = macro.substring("CMP".length)
        return "COMPARE; $name"
    }
}
package com.tezos.lang.michelson.lang.macro

import com.tezos.lang.michelson.psi.PsiAnnotationType

class ConditionalMacroMetadata : MacroMetadata {
    companion object {
        val NAMES = setOf("IFEQ", "IFNEQ", "IFLT", "IFGT", "IFLE", "IFGE",
                "IFCMPEQ", "IFCMPNEQ", "IFCMPLT", "IFCMPGT", "IFCMPLE", "IFCMPGE",
                "IF_SOME")
    }

    override fun staticNames(): Collection<String> = NAMES

    override fun validate(macro: String): Pair<String, Int>? {
        return if (macro in NAMES) {
            null
        } else {
            "Unsupported macro" to 0
        }
    }

    override fun requiredBlocks(): Int = 2

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int = 0

    override fun helpContentFile(name: String): String? {
        return when {
            name == "IF_SOME" -> "if_some.txt"
            name.startsWith("IFCMP")->"if_cmp.txt"
            else -> "if_cond.txt"
        }
    }

    override fun expand(macro: String, deepExpansion: Boolean): String? {
        if (validate(macro) != null) {
            return null
        }

        if (macro == "IF_SOME") {
            // IF_SOME checks the current type on the stack
            // we can't expand it without access to the PSI
            return null
        }

        val name = macro.substring("IF".length)
        return when {
            name.startsWith("CMP") -> "COMPARE ; ${name.substring("CMP".length)} ; IF {} {}"
            else -> "$name; IF {} {}"
        }
    }
}
package com.tezos.lang.michelson.lang.macro

import com.tezos.client.stack.MichelsonStack
import com.tezos.lang.michelson.psi.PsiAnnotationType

class ConditionalMacroMetadata : MacroMetadata {
    companion object {
        val NAMES_INT = setOf("IFEQ", "IFNEQ", "IFLT", "IFGT", "IFLE", "IFGE")
        val NAMES_CMP = setOf("IFCMPEQ", "IFCMPNEQ", "IFCMPLT", "IFCMPGT", "IFCMPLE", "IFCMPGE")
        val NAMES = setOf("IF_SOME") + NAMES_INT + NAMES_CMP
    }

    override fun staticNames(): Collection<String> = NAMES

    override fun dynamicNames(stack: MichelsonStack): Collection<DynamicMacroName> {
        val top = stack.top
        val second = stack.frames.getOrNull(1)

        val result = mutableListOf<DynamicMacroName>()
        if (Comparables.isSame(Comparables.INT, top)) {
            for (n in NAMES_INT) {
                result += DynamicMacroName(n)
            }
        }

        if (top != null && top.type.isComparable && Comparables.isSame(top, second)) {
            for (n in NAMES_CMP) {
                result += DynamicMacroName(n)
            }
        }

        if (Comparables.isSame(top, Comparables.OPTION)) {
            result += DynamicMacroName("IF_SOME")
        }

        return result
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

    override fun helpContentFile(name: String): String? {
        return when {
            name == "IF_SOME" -> "if_some.txt"
            name.startsWith("IFCMP") -> "if_cmp.txt"
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
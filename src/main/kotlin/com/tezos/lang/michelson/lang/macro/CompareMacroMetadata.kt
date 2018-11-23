package com.tezos.lang.michelson.lang.macro

import com.tezos.client.stack.MichelsonStack
import com.tezos.client.stack.MichelsonStackType
import com.tezos.lang.michelson.psi.PsiAnnotationType

class CompareMacroMetadata : MacroMetadata {
    companion object {
        val NAMES = setOf("CMPEQ", "CMPNEQ", "CMPLT", "CMPGT", "CMPLE", "CMPGE")
    }

    override fun staticNames(): Collection<String> = NAMES

    /**
     * CMP{X} expands to COMPARE; X
     * COMPARE is available when the top two elements are comparable and of the same type, e.g. "int int"
     */
    override fun dynamicNames(stack: MichelsonStack): Collection<DynamicMacroName> {
        if (stack.size < 2) {
            return emptyList()
        }

        // fixme compare annotations?
        val (top, second) = stack.frames
        return when {
            top.type.isComparable && top.type.name == second.type.name -> NAMES.map {
                DynamicMacroName(it, Comparables.BOOL)
            }

            else -> emptyList()
        }
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

    override fun helpContentFile(name: String): String? = "cmp.txt"

    override fun expand(macro: String, deepExpansion: Boolean): String? {
        if (validate(macro) != null) {
            return null
        }

        val name = macro.substring("CMP".length)
        return "COMPARE; $name"
    }
}
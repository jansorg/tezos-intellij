package com.tezos.lang.michelson.lang.macro

import com.tezos.client.stack.MichelsonStack
import com.tezos.lang.michelson.lang.PsiAnnotationType

class FailMacroMetadata : MacroMetadata {
    private companion object {
        // FAIL is always available
        val NAMES = listOf("FAIL")
        val DYNAMIC_NAMES = listOf(DynamicMacroName("FAIL"))
    }

    override fun staticNames() = NAMES

    override fun dynamicNames(stack: MichelsonStack): Collection<DynamicMacroName> {
        return DYNAMIC_NAMES
    }

    override fun validate(macro: String): Pair<String, Int>? {
        return when (macro == "FAIL") {
            true -> null
            else -> "unsupported macro" to 0
        }
    }

    override fun requiredBlocks(): Int = 0

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int = 0

    override fun expand(macro: String, deepExpansion: Boolean): String? = "UNIT; FAILWITH"

    override fun helpContentFile(name: String): String? {
        return "fail.txt"
    }
}
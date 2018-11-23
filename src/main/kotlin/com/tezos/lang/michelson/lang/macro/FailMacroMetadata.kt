package com.tezos.lang.michelson.lang.macro

import com.tezos.lang.michelson.psi.PsiAnnotationType

class FailMacroMetadata : MacroMetadata {
    private companion object {
        val NAMES = listOf("FAIL")
    }

    override fun staticNames() = NAMES

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
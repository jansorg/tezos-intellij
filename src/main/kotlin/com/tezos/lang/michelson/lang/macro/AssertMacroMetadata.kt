package com.tezos.lang.michelson.lang.macro

import com.tezos.lang.michelson.psi.PsiAnnotationType

class AssertMacroMetadata : MacroMetadata {
    companion object {
        val NAMES = setOf("ASSERT", "ASSERT_EQ", "ASSERT_NEQ", "ASSERT_LT", "ASSERT_LE", "ASSERT_GT",
                "ASSERT_GE", "ASSERT_CMPEQ", "ASSERT_CMPNEQ", "ASSERT_CMPLT", "ASSERT_CMPLE",
                "ASSERT_CMPGT", "ASSERT_CMPGE", "ASSERT_NONE", "ASSERT_SOME", "ASSERT_LEFT",
                "ASSERT_RIGHT")
    }

    override fun staticMacroName(): Collection<String> = NAMES

    override fun validate(macro: String): Pair<String, Int>? {
        return if (macro in NAMES) {
            null
        } else {
            "Unsupported macro" to 0
        }
    }

    override fun requiredBlocks(): Int = 0

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int {
        return 0
    }
}
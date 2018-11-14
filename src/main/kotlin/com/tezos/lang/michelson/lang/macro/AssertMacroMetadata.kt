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

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int = 0

    override fun helpContentFile(name: String): String? {
        if (name == "ASSERT") {
            return "assert.txt"
        }

        val suffix = name.substring("ASSERT_".length)
        return when (suffix) {
            "CMPEQ", "CMPNEQ", "CMPLT", "CMPLE", "CMPGT", "CMPGE" -> "assert_cmp.txt"
            "EQ", "NEQ", "LT", "LE", "GT", "GE" -> "assert_cond.txt"
            else -> "${name.toLowerCase()}}.txt"
        }
    }

    override fun expand(macro: String, deepExpansion: Boolean): String? {
        if (validate(macro) != null) {
            return null
        }

        if (macro == "ASSERT") {
            return when (deepExpansion) {
                false -> "IF {} {FAIL}"
                true -> "IF {} { UNIT; FAILWITH }"
            }
        }

        val name = macro.substring("ASSERT_".length)
        return when (name) {
            "EQ", "NEQ", "LT", "LE", "GT", "GE" -> when (deepExpansion) {
                false -> "IF$name {} { FAIL }"
                true -> "$name; IF {} { UNIT; FAILWITH }"
            }

            "CMPEQ", "CMPNEQ", "CMPLT", "CMPLE", "CMPGT", "CMPGE" -> when (deepExpansion) {
                false -> "IF$name {} { FAIL }"
                true -> "COMPARE; $name; IF {} { UNIT; FAILWITH }"
            }

            "NONE" -> when (deepExpansion) {
                false -> "IF_NONE {} {FAIL}"
                true -> "IF_NONE {} { UNIT; FAILWITH }"
            }

            "SOME" -> when (deepExpansion) {
                false -> "IF_NONE {FAIL} {}"
                true -> "IF_NONE { UNIT; FAILWITH } {}"
            }

            "LEFT" -> when (deepExpansion) {
                false -> "IF_LEFT {} {FAIL}"
                true -> "IF_LEFT {} { UNIT; FAILWITH }"
            }

            "RIGHT" -> when (deepExpansion) {
                false -> "IF_LEFT {FAIL} {}"
                true -> "IF_LEFT { UNIT; FAILWITH } {}"
            }

            else -> null
        }
    }
}
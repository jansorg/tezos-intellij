package com.tezos.lang.michelson.lang.macro

import com.tezos.client.stack.MichelsonStack
import com.tezos.lang.michelson.lang.PsiAnnotationType

class AssertMacroMetadata : MacroMetadata {
    companion object {
        val EQ_NAMES = setOf("ASSERT_EQ", "ASSERT_NEQ", "ASSERT_LT", "ASSERT_LE", "ASSERT_GT", "ASSERT_GE")
        val CMPEQ_NAMES = setOf("ASSERT_CMPEQ", "ASSERT_CMPNEQ", "ASSERT_CMPLT", "ASSERT_CMPLE", "ASSERT_CMPGT", "ASSERT_CMPGE")

        val NAMES = setOf("ASSERT") + EQ_NAMES + CMPEQ_NAMES + setOf("ASSERT_NONE", "ASSERT_SOME", "ASSERT_LEFT", "ASSERT_RIGHT")
    }

    override fun staticNames(): Collection<String> = NAMES

    override fun dynamicNames(stack: MichelsonStack): Collection<DynamicMacroName> {
        if (stack.isEmpty) {
            return emptyList()
        }

        val result = mutableListOf<DynamicMacroName>()

        val top = stack.top!!
        val second = stack.frames.getOrNull(1)

        // ASSERT is available for bool
        // ASSERT_{EQ|NEQ|LT|LE|GT|GE} are available for bool
        if (top.type.name == "bool") {
            result += DynamicMacroName("ASSERT", top.type)

            for (n in EQ_NAMES) {
                result += DynamicMacroName(n, top.type)
            }
        }

        // ASSERT_CMP{EQ|NEQ|LT|LE|GT|GE} are available for two comparable types of the same name
        if (stack.size >= 2 && top.type.isComparable && Comparables.isSame(top, second)) {
            for (n in CMPEQ_NAMES) {
                result += DynamicMacroName(n, top.type)
            }
        }

        // ASSERT_NONE and ASSERT_SOME
        if (top.type.name == "option") {
            val subtype = top.type.arguments.getOrNull(0)
            result += DynamicMacroName("ASSERT_SOME", subtype)
            result += DynamicMacroName("ASSERT_NONE", second?.type)
        }

        // ASSERT_LEFT and ASSERT_RIGHT are available of or-types
        // the new top type is the type of the left or right side
        if (top.type.name == "or") {
            val (left, right) = top.type.arguments
            result += DynamicMacroName("ASSERT_LEFT", left)
            result += DynamicMacroName("ASSERT_RIGHT", right)
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
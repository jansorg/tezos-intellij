package com.tezos.lang.michelson.lang.macro

import com.tezos.client.stack.MichelsonStack
import com.tezos.lang.michelson.psi.PsiAnnotationType
import java.util.regex.Pattern

/**
 * Supports the Dii+p macro, as defined at http://tezos.gitlab.io/betanet/whitedoc/michelson.html#syntactic-conveniences.
 * Diip is "A syntactic sugar for working deeper in the stack".
 */
class DipMacroMetadata : MacroMetadata {
    private companion object {
        val regexp: Pattern = Pattern.compile("DII+P")
    }

    override fun staticNames(): Collection<String> = emptyList()

    override fun dynamicNames(stack: MichelsonStack): Collection<DynamicMacroName> {
        if (stack.size <= 1) {
            return emptyList()
        }

        val top = stack.top!!.type

        val result = mutableListOf<DynamicMacroName>()
        // stack depth 2 results in DIP
        // stack depth n results in D I{n-1} P
        for ((index, frame) in stack.frames.withIndex()) {
            result += DynamicMacroName("D" + "I".repeat(index+1) + "P", top, frame.type)
        }
        return result
    }

    override fun validate(macro: String): Pair<String, Int>? {
        return if (regexp.matcher(macro).matches()) {
            null
        } else {
            "Macro doesn't match DII+P" to 0
        }
    }

    override fun requiredBlocks(): Int = 1

    override fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int = 0

    override fun helpContentFile(name: String): String? = "dip.txt"

    override fun expand(macro: String, deepExpansion: Boolean): String? {
        if (validate(macro) != null) {
            return null
        }

        val result = StringBuilder()

        val levels = macro.count { it == 'I' }
        for (i in 0 until levels) {
            result.insert(0, "DIP{")
            result.append("}")
        }

        return result.toString()
    }
}
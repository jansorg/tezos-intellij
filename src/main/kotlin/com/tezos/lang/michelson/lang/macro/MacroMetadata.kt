package com.tezos.lang.michelson.lang.macro

import com.tezos.client.stack.MichelsonStack
import com.tezos.client.stack.MichelsonStackType
import com.tezos.lang.michelson.psi.PsiAnnotationType

data class DynamicMacroName(val name: String, val stackType: MichelsonStackType? = null, val accessedType: MichelsonStackType? = null)

/**
 * Provides information about one macro or a set of similar macros.
 * @author jansorg
 */
interface MacroMetadata {
    /**
     * @return the static names which belong to this macro definition.
     */
    fun staticNames(): Collection<String>

    /** Returns the macro names which are available in addition to the static names and which are supported for the given stack information.
     * @return maco names compatible with the current stack
     */
    fun dynamicNames(stack: MichelsonStack): Collection<DynamicMacroName> {
        return emptyList()
    }

    /**
     * @return None when valid, an error message and the offset where that error was detected when invalid.
     */
    fun validate(macro: String): Pair<String, Int>?

    /**
     * @return The number of supported code blocks.
     */
    fun requiredBlocks(): Int

    /**
     * @return The number of supported annotations for the given type, in the range [0,]
     */
    fun supportedAnnotations(type: PsiAnnotationType, macro: String): Int

    /**
     * @return The expanded (i.e. de-sugared) form of the macro. If the macro or the expansion is unsupported then null is returned.
     */
    fun expand(macro: String, deepExpansion: Boolean = false): String?

    /**
     * @return the URL pointing to the description as a classpath resource
     */
    fun helpContentFile(name: String): String?
}
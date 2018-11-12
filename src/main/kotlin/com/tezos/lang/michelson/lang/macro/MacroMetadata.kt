package com.tezos.lang.michelson.lang.macro

import com.tezos.lang.michelson.psi.PsiAnnotationType

/**
 * Provides information about one macro or a set of similar macros.
 * @author jansorg
 */
interface MacroMetadata {
    fun staticMacroName(): Collection<String>

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
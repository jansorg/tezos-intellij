package com.tezos.lang.michelson.parser.macro

import com.tezos.lang.michelson.psi.PsiAnnotationType

/**
 * Provides information about a dynamic macro, i.e. about a macro with dynamic length, e.g. DIIIP or DUUP.
 * @author jansorg
 */
interface MacroMetadata {
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
}
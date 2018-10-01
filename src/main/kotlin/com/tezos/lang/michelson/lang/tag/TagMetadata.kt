package com.tezos.lang.michelson.lang.tag

/**
 * Definition of a supported tag.
 *
 * @author jansorg
 */
interface TagMetadata {
    /**
     * The name of the macro, e.g. "Pair".
     */
    fun names(): Set<String>

    /**
     * The number of supported values, e.g. 2 for "Pair".
     */
    fun supportedValues(): Short

    fun isComplex(): Boolean = false
}
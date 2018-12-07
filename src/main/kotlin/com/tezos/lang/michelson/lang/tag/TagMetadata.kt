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

    fun isComplex(): Boolean
}

data class SimpleTagMetadata(private val names: Set<String>, private val supportedDataValues: Short, private val isComplex: Boolean) : TagMetadata {
    constructor(supportedDataValues: Short, vararg names: String) : this(names.toSet(), supportedDataValues, false)

    override fun names(): Set<String> = names

    override fun supportedValues(): Short = supportedDataValues

    override fun isComplex(): Boolean = isComplex
}
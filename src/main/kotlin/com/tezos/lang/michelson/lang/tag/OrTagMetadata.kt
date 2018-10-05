package com.tezos.lang.michelson.lang.tag

class OrTagMetadata : TagMetadata {
    override fun names(): Set<String> = setOf("Left", "Right")

    override fun supportedValues(): Short = 1

    override fun isComplex(): Boolean = true
}
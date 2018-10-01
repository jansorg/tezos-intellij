package com.tezos.lang.michelson.lang.tag

class SomeMetadata : TagMetadata {
    override fun names(): Set<String> = setOf("Some")

    override fun supportedValues(): Short = 1

    override fun isComplex(): Boolean = true
}
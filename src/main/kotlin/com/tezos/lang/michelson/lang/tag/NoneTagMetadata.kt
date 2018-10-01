package com.tezos.lang.michelson.lang.tag

class NoneTagMetadata : TagMetadata {
    override fun names(): Set<String> = setOf("None")

    override fun supportedValues(): Short = 0
}
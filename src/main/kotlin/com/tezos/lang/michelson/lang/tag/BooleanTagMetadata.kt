package com.tezos.lang.michelson.lang.tag

class BooleanTagMetadata : TagMetadata {
    override fun names(): Set<String> = setOf("True", "False")

    override fun supportedValues(): Short = 0
}
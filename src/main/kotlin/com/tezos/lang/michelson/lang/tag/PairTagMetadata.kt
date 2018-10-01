package com.tezos.lang.michelson.lang.tag

class PairTagMetadata : TagMetadata {
    override fun names(): Set<String> = setOf("Pair")

    override fun supportedValues(): Short = 2
}
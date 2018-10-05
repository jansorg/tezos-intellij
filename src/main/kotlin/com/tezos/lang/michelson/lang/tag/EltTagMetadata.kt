package com.tezos.lang.michelson.lang.tag

class EltTagMetadata : TagMetadata {
    override fun names(): Set<String> = setOf("Elt")

    override fun supportedValues(): Short = 2
}
package com.tezos.lang.michelson.lang.tag

class UnitTagMetadata : TagMetadata {
    override fun names(): Set<String> = setOf("Unit", "None")

    override fun supportedValues(): Short = 0
}
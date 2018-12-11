package com.tezos.client.stack

import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.lang.type.TypeMetadata

/**
 * @author jansorg
 */
object MichelsonStackUtils {
    fun generateSampleString(type: MichelsonStackType, simpleTypeValueGenerator: (TypeMetadata) -> String = this::simpleTypeSampleString): String {
        val meta = MichelsonLanguage.findTypeMetadata(type.name) ?: return "<unknown>"

        if (meta.isSimple) {
            return simpleTypeValueGenerator(meta)
        }

        val subSamples = type.arguments.map { generateSampleString(it, simpleTypeValueGenerator) }
        return when (type.name) {
            "option" -> "(Some ${subSamples.joinToString(" ")})"
            "list" -> "{ ${subSamples[0]} }"
            "set" -> "{ ${subSamples[0]} }"
            "pair" -> "(Pair ${subSamples.joinToString(" ")})"
            "or" -> "(Left ${subSamples[0]})"
            "lambda" -> "lambda"
            "map" -> "{ Elt ${subSamples.joinToString(" ")} ; }"
            "big_map" -> "{ Elt ${subSamples.joinToString(" ")} ; }"
            else -> "<unknown type ${type.name}>"
        }
    }

    public fun simpleTypeSampleString(type: TypeMetadata): String {
        val name = type.name
        return when (name) {
            "int" -> "42"
            "nat" -> "42"
            "string" -> "\"foo\""
            "bytes" -> "0x1A"
            "mutez" -> "<mutez>"
            "bool" -> "True"
//            "key_hash" -> "42"
//            "timestamp" -> "42"
//            "address" -> "42"
//            "operation" -> "42"
//            "key" -> "42"
//            "unit" -> "42"
//            "signature" -> "42"
            else -> "<$name>"
        }
    }
}
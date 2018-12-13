package com.tezos.client.stack

import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.lang.type.TypeMetadata

/**
 * @author jansorg
 */
object MichelsonStackUtils {
    /**
     * Generates a single sample string.
     */
    fun generateSampleString(type: MichelsonStackType, simpleTypeValueGenerator: (TypeMetadata) -> String = this::simpleTypeSampleString): String {
        val meta = MichelsonLanguage.findTypeMetadata(type.name) ?: return "<unknown>"

        if (meta.isSimple) {
            return simpleTypeValueGenerator(meta)
        }

        val subSamples = type.arguments.map { generateSampleString(it, simpleTypeValueGenerator) }
        return when (type.name) {
            "option" -> "(Some ${subSamples.joinToString(" ")})"
            "list", "set" -> "{ ${subSamples[0]}; }"
            "pair" -> "(Pair ${subSamples.joinToString(" ")})"
            "or" -> "(Left ${subSamples[0]})"
            "lambda" -> "lambda"
            "map", "big_map" -> "{ Elt ${subSamples.joinToString(" ")}; }"
            else -> "<unknown type ${type.name}>"
        }
    }

    /**
     * Generates all possible sample strings, i.e. if there are multiple results per type then all are used.
     */
    fun generateSampleList(type: MichelsonStackType, simpleTypeValueGenerator: (TypeMetadata) -> String = this::simpleTypeSampleString): List<String> {
        val meta = MichelsonLanguage.findTypeMetadata(type.name) ?: return emptyList()

        if (meta.isSimple) {
            return listOf(simpleTypeValueGenerator(meta))
        }

        val subSampleLists = type.arguments.map { generateSampleList(it, simpleTypeValueGenerator) }
        val results = mutableListOf<String>()
        when (type.name) {
            "option" -> {
                assert(subSampleLists.size == 1)

                results += "None"
                for (x in subSampleLists[0]) {
                    results += "(Some $x)"
                }
            }
            "list", "set" -> {
                assert(subSampleLists.size == 1)

                results += "{}"
                for (x in subSampleLists[0]) {
                    results += "{ $x; }"
                }
            }
            "pair" -> {
                for (l in subSampleLists[0]) {
                    for (r in subSampleLists[1]) {
                        results += "(Pair $l $r)"
                    }
                }
            }
            "or" -> {
                assert(subSampleLists.size == 2)
                for (x in subSampleLists[0]) {
                    results += "(Left $x)"
                }
                for (x in subSampleLists[1]) {
                    results += "(Right $x)"
                }
            }
            "map", "big_map" -> {
                assert(subSampleLists.size == 2)
                for (key in subSampleLists[0]) {
                    for (value in subSampleLists[1]) {
                        results += "{ Elt $key $value; }"
                    }
                }
            }
            else -> results += "<unknown type ${type.name}>"
        }

        return results
    }

    public fun simpleTypeSampleString(type: TypeMetadata): String {
        val name = type.name
        return when (name) {
            "int" -> "42"
            "nat" -> "42"
            "string" -> "\"foo\""
            "bytes" -> "0x1A"
            "mutez" -> "100"
            "bool" -> "True"
            "key_hash" -> "\"tz1KqTpEZ7Yob7QbPE4Hy4Wo8fHG8LhKxZSx\"" // this is a sample value in the main tezos repo
            "timestamp" -> "\"1970-01-01T00:03:20Z\""
            "address" -> "\"tz1KqTpEZ7Yob7QbPE4Hy4Wo8fHG8LhKxZSx\""
            "operation" -> "Nil"
            "key" -> "\"edpkuBknW28nW72KG6RoHtYW7p12T6GKc7nAbwYX5m8Wd9sDVC9yav\""
            "unit" -> "Unit"
            "signature" -> "\"edsigthTzJ8X7MPmNeEwybRAvdxS1pupqcM5Mk4uCuyZAe7uEk68YpuGDeViW8wSXMrCi5CwoNgqs8V2w8ayB5dMJzrYCHhD8C7\""
            else -> "<$name>"
        }
    }
}
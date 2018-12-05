package com.tezos.lang.michelson.lang

/**
 * The possible types of parameters of instrcuctions.
 * @author jansorg
 */
enum class ParameterType {
    /**
     * A block of instructions, i.e. {<instruction>...}
     */
    INSTRUCTION_BLOCK,
    /**
     * A comparable type as defined in MichelsonLanguage
     */
    COMPARABLE_TYPE,
    /**
     * Any type
     */
    TYPE,
    /**
     * A data literal
     */
    DATA;

    fun asPlaceholder(): String {
        return when (this) {
            COMPARABLE_TYPE -> "<comparable type>"
            TYPE -> "<type>"
            DATA -> "<data>"
            INSTRUCTION_BLOCK -> "{ <instruction> ... }"
        }
    }
}
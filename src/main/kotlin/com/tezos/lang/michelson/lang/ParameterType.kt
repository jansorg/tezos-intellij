package com.tezos.lang.michelson.lang

/**
 * The possible types of parameters of instrcuctions.
 * @author jansorg
 */
enum class ParameterType {
    /**
     * An optional block of instructions, i.e. {<instruction>...}
     * This is only used by the CREATE_CONTRACT instruction
     */
    OPTIONAL_INSTRUCTION_BLOCK,
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
            OPTIONAL_INSTRUCTION_BLOCK -> "[optional] { <instruction> ... }"
            INSTRUCTION_BLOCK -> "{ <instruction> ... }"
        }
    }
}
@file:Suppress("MemberVisibilityCanBePrivate")

package com.tezos.lang.michelson.lang

import com.intellij.lang.Language
import com.tezos.lang.michelson.lang.instruction.InstructionMetadata
import com.tezos.lang.michelson.lang.instruction.SimpleInstruction
import com.tezos.lang.michelson.lang.macro.*
import com.tezos.lang.michelson.lang.tag.*

/**
 * @author jansorg
 */
object MichelsonLanguage : Language("Michelson") {
    override fun getDisplayName(): String = "Michelson"

    // PAIR, Pair and pair are different tokens
    override fun isCaseSensitive(): Boolean = true

    // contract is handled by the parser in a special way
    val TYPES_COMPARABLE = setOf("int", "nat", "string", "bytes", "mutez", "bool", "key_hash", "timestamp")
    val TYPES_SIMPLE = setOf("address", "operation", "key", "unit", "signature")
    val TYPES_NESTED = setOf("option", "list", "set", "pair", "or", "lambda", "map", "big_map")
    val TYPES_ALL = TYPES_COMPARABLE + TYPES_SIMPLE + TYPES_NESTED
    val TYPES_ALL_SIMPLE = TYPES_SIMPLE + TYPES_COMPARABLE

    val TYPE_COMPONENTS_WITH_FIELD_ANNOTATIONS = setOf("pair", "option", "or")

    // tag names
    val TAG_UNIT: TagMetadata = UnitTagMetadata()
    val TAG_NONE: TagMetadata = NoneTagMetadata()
    val TAG_BOOL: TagMetadata = BooleanTagMetadata()
    val TAG_PAIR: TagMetadata = PairTagMetadata()
    val TAG_SOME: TagMetadata = SomeMetadata()
    val TAG_OR: TagMetadata = OrTagMetadata()
    val TAG_ELT: TagMetadata = EltTagMetadata()
    val TAGS_METAS = listOf(TAG_UNIT, TAG_NONE, TAG_BOOL, TAG_PAIR, TAG_SOME, TAG_OR, TAG_ELT)
    val TAG_NAMES = TAGS_METAS.flatMap { it.names() }.toSet()
    val TAG_OPTION_NAMES = setOf("None", "Some")

    // macros
    val FAIL_MACRO: MacroMetadata = FailMacroMetadata()
    val ASSERT_MACROS: MacroMetadata = AssertMacroMetadata()
    val COMPARE_MACROS: MacroMetadata = CompareMacroMetadata()
    val IF_MACROS: MacroMetadata = ConditionalMacroMetadata()
    val DUUP_MACRO: MacroMetadata = DupMacroMetadata()
    val DIIP_MACRO: MacroMetadata = DipMacroMetadata()
    val PAIR_MACRO: MacroMetadata = PairMacroMetadata()
    val UNPAIR_MACRO: MacroMetadata = UnpairMacroMetadata()
    val CADR_MACRO: MacroMetadata = CadrMacroMetadata()
    val SET_CADR_MACRO: MacroMetadata = SetCadrMacroMetadata()
    val MAP_CADR_MACRO: MacroMetadata = MapCadrMacroMetadata()
    val MACROS = listOf(FAIL_MACRO, ASSERT_MACROS, COMPARE_MACROS, IF_MACROS, DUUP_MACRO, DIIP_MACRO, PAIR_MACRO, UNPAIR_MACRO, CADR_MACRO, SET_CADR_MACRO, MAP_CADR_MACRO)
    // all available static macro names, dynamic macros like DIIIP or PAPAIR are not part of this list
    val MACRO_NAMES = MACROS.flatMap { it.staticNames() }

    // instructions
    val INSTRUCTIONS: List<InstructionMetadata> = listOf(
            "ABS".with(),
            "ADD".with(),
            "ADDRESS".with(),
            "AMOUNT".with(),
            "AND".with(),
            "BALANCE".with(),
            "BLAKE2B".with(),
            "CAR".with(),
            "CAST".with(),
            "CDR".with(),
            "CHECK_SIGNATURE".with(),
            "COMPARE".with(),
            "CONCAT".with(),
            "CONS".with(),
            "CREATE_ACCOUNT".with(),
            "CREATE_CONTRACT".with(),
            "DIV".with(),
            "DROP".with(),
            "DUP".with(),
            "EDIV".with(),
            "EQ".with(),
            "EXEC".with(),
            "FAILWITH".with(),
            "GE".with(),
            "GET".with(),
            "GT".with(),
            "HASH_KEY".with(),
            "IMPLICIT_ACCOUNT".with(),
            "LE".with(),
            "LSL".with(),
            "LSR".with(),
            "LT".with(),
            "MEM".with(),
            "MUL".with(),
            "NEG".with(),
            "NEQ".with(),
            "NOT".with(),
            "NOW".with(),
            "OR".with(),
            "PACK".with(),
            "PAIR".with(),
            "RENAME".with(),
            "SELF".with(),
            "SENDER".with(),
            "SET_DELEGATE".with(),
            "SHA256".with(),
            "SHA512".with(),
            "SIZE".with(),
            "SLICE".with(),
            "SOME".with(),
            "SOURCE".with(),
            "STEPS_TO_QUOTA".with(),
            "SUB".with(),
            "SWAP".with(),
            "TRANSFER_TOKENS".with(),
            "UNIT".with(),
            "UPDATE".with(),
            "XOR".with(),
            "INT".with(),
            "MOD".with(),
            // one block
            "DIP".with(ParameterType.INSTRUCTION_BLOCK),
            "ITER".with(ParameterType.INSTRUCTION_BLOCK),
            "LOOP".with(ParameterType.INSTRUCTION_BLOCK),
            "LOOP_LEFT".with(ParameterType.INSTRUCTION_BLOCK),
            "MAP".with(ParameterType.INSTRUCTION_BLOCK),
            "CREATE_CONTRACT".with(ParameterType.INSTRUCTION_BLOCK),
            // two blocks
            "IF".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            "IF_CONS".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            "IF_LEFT".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            "IF_RIGHT".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            "IF_NONE".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            // one type
            "CONTRACT".with(ParameterType.TYPE),
            "EMPTY_SET".with(ParameterType.TYPE),
            "LEFT".with(ParameterType.TYPE),
            "NIL".with(ParameterType.TYPE),
            "NONE".with(ParameterType.TYPE),
            "RIGHT".with(ParameterType.TYPE),
            "UNPACK".with(ParameterType.TYPE),
            // one data
            "FAILWITH".with(ParameterType.DATA),
            // other
            "PUSH".with(ParameterType.TYPE, ParameterType.DATA),
            "LAMBDA".with(ParameterType.TYPE, ParameterType.TYPE, ParameterType.INSTRUCTION_BLOCK),
            // questionable
            "ISNAT".with(),
            "IS_NAT".with()
    )

    val INSTRUCTIONS_NO_ARGS = INSTRUCTIONS.filter { it.parameters.isEmpty() }.map { it.name }.toSet()

    // instructions which are not fully explained in the whitepaper
    val QUESTIONABLE_INSTRUCTIONS = INSTRUCTIONS.filter { it.name == "ISNAT" || it.name == "IS_NAT" }.map { it.name }.toSet()

    // instructions which expect one instruction block
    val INSTRUCTIONS_ONE_BLOCK = INSTRUCTIONS.filter { it.parameters.size == 1 && it.parameters[0] == ParameterType.INSTRUCTION_BLOCK }.map { it.name }.toSet()
    // instructions which expect two instruction blocks
    val INSTRUCTIONS_TWO_BLOCKS = INSTRUCTIONS.filter { it.parameters.size == 2 && it.parameters[0] == ParameterType.INSTRUCTION_BLOCK && it.parameters[1] == ParameterType.INSTRUCTION_BLOCK }.map { it.name }.toSet()

    // instructions which expect one type as argument
    val INSTRUCTIONS_ONE_TYPE = INSTRUCTIONS.filter { it.parameters.size == 1 && it.parameters[0] == ParameterType.TYPE }.map { it.name }.toSet()

    val INSTRUCTIONS_NO_ANNOTATION = setOf("DROP", "SWAP", "IF_NONE", "IF_LEFT", "IF_CONS", "ITER", "IF", "LOOP", "LOOP_LEFT", "DIP", "FAILWITH")

    val INSTRUCTIONS_ONE_VAR_ANNOTATION = setOf("DUP", "PUSH", "UNIT", "SOME", "NONE", "PAIR", "CAR", "CDR", "LEFT", "RIGHT", "NIL", "CONS", "SIZE",
            "MAP", "MEM", "EMPTY_SET", "EMPTY_MAP", "UPDATE", "GET", "LAMBDA", "EXEC", "ADD", "SUB", "CONCAT", "MUL", "OR", "AND", "XOR", "NOT",
            "ABS", "IS_NAT", "INT", "NEG", "EDIV", "LSL", "LSR", "COMPARE", "EQ", "NEQ", "LT", "GT", "LE", "GE",
            "ADDRESS", "CONTRACT", "SET_DELEGATE", "IMPLICIT_ACCOUNT", "NOW", "AMOUNT", "BALANCE", "HASH_KEY",
            "CHECK_SIGNATURE", "BLAKE2B", "STEPS_TO_QUOTA", "SOURCE", "SENDER", "SELF", "CAST", "RENAME")
    val INSTRUCTIONS_ONE_VAR_ANNOTATION_QUESTIONABLE = setOf("TRANSFER_TOKENS", "SLICE", "UNIT")
    val INSTRUCTIONS_TWO_VAR_ANNOTATIONS = setOf("CREATE_ACCOUNT", "CREATE_CONTRACT")

    val INSTRUCTIONS_ONE_TYPE_ANNOTATION = setOf("UNIT", "PAIR", "SOME", "NONE", "LEFT", "RIGHT", "NIL", "EMPTY_SET", "EMPTY_MAP")

    val INSTRUCTIONS_ONE_FIELD_ANNOTATION = setOf("NONE", "SOME")
    val INSTRUCTIONS_TWO_FIELD_ANNOTATIONS = setOf("PAIR", "LEFT", "RIGHT")

    val INSTRUCTION_NAMES: Set<String> = INSTRUCTIONS_NO_ARGS + QUESTIONABLE_INSTRUCTIONS + INSTRUCTIONS_ONE_BLOCK + INSTRUCTIONS_TWO_BLOCKS + INSTRUCTIONS_ONE_TYPE + INSTRUCTIONS_NO_ANNOTATION +
            INSTRUCTIONS_ONE_VAR_ANNOTATION + INSTRUCTIONS_ONE_VAR_ANNOTATION_QUESTIONABLE + INSTRUCTIONS_TWO_VAR_ANNOTATIONS + INSTRUCTIONS_ONE_TYPE_ANNOTATION +
            INSTRUCTIONS_ONE_FIELD_ANNOTATION + INSTRUCTIONS_TWO_FIELD_ANNOTATIONS

    val INSTRUCTIONS_SKIP_ANNOTATIONS: Set<String> = setOf("CREATE_CONTRACT")

    private fun String.with(vararg params: ParameterType): InstructionMetadata = SimpleInstruction(this, params.toList())
}
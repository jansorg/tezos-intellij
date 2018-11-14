package com.tezos.lang.michelson.lang

import com.intellij.lang.Language
import com.tezos.lang.michelson.lang.macro.*
import com.tezos.lang.michelson.lang.tag.*

/**
 * @author jansorg
 */
object MichelsonLanguage : Language("Michelson") {
    override fun getDisplayName(): String = "Michelson"

    // Pair and pair are different tokens
    override fun isCaseSensitive(): Boolean = true

    val TYPES_OTHER = setOf("key", "unit", "signature")
    val TYPES_COMPARABLE = setOf("int", "nat", "string", "tez", "bool", "key_hash", "timestamp", "bytes", "mutez", "address")
    val TYPES_COMPLEX = setOf("option", "list", "set", "operation", "pair", "or", "lambda", "map", "big_map")
    val TYPES_ALL = TYPES_OTHER + TYPES_COMPARABLE + TYPES_COMPLEX

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
    val MACRO_NAMES = MACROS.flatMap { it.staticMacroName() }

    // instructions which do not support arguments
    val INSTRUCTIONS_NO_ARGS = setOf("ABS", "ADD", "ADDRESS", "AMOUNT", "AND", "BALANCE", "BLAKE2B",
            "CAR", "CAST", "CDR", "CHECK_SIGNATURE", "COMPARE", "CONCAT", "CONS",
            "CREATE_ACCOUNT", "CREATE_CONTRACT", "DIV", "DROP", "DUP",
            "EDIV", "EQ", "EXEC", "FAILWITH", "GE", "GET", "GT",
            "HASH_KEY", "IMPLICIT_ACCOUNT", "LE", "LSL", "LSR", "LT", "MEM", "MUL", "NEG", "NEQ", "NOT", "NOW",
            "OR", "PACK", "PAIR", "RENAME",
            "SELF", "SENDER", "SET_DELEGATE", "SHA256", "SHA512", "SIZE", "SLICE", "SOME", "SOURCE", "STEPS_TO_QUOTA", "SUB", "SWAP",
            "TRANSFER_TOKENS", "UNIT", "UPDATE", "XOR",
            "INT", "MOD" //fixme int and mod are not in the whitepaper, but we're pretty sure that they don't accept arguments
    )

    // instructions which are not fully explained in the whitepaper
    val QUESTIONABLE_INSTRUCTIONS = setOf("ISNAT", "IS_NAT", "REDUCE")

    // instructions which expect one instruction block
    val INSTRUCTIONS_ONE_BLOCK = setOf("DIP", "ITER", "LOOP", "LOOP_LEFT", "MAP")
    // instructions which expect two instruction blocks
    val INSTRUCTIONS_TWO_BLOCKS = setOf("IF", "IF_CONS", "IF_LEFT", "IF_RIGHT", "IF_NONE")
    // instructions which expect one type as argument
    val INSTRUCTIONS_ONE_TYPE = setOf("CONTRACT", "EMPTY_SET", "LEFT", "NIL", "NONE", "RIGHT", "UNPACK")

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

    val INSTRUCTIONS: Set<String> = INSTRUCTIONS_NO_ARGS + QUESTIONABLE_INSTRUCTIONS + INSTRUCTIONS_ONE_BLOCK + INSTRUCTIONS_TWO_BLOCKS + INSTRUCTIONS_ONE_TYPE + INSTRUCTIONS_NO_ANNOTATION +
            INSTRUCTIONS_ONE_VAR_ANNOTATION + INSTRUCTIONS_ONE_VAR_ANNOTATION_QUESTIONABLE + INSTRUCTIONS_TWO_VAR_ANNOTATIONS + INSTRUCTIONS_ONE_TYPE_ANNOTATION +
            INSTRUCTIONS_ONE_FIELD_ANNOTATION + INSTRUCTIONS_TWO_FIELD_ANNOTATIONS
}
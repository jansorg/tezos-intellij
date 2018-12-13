@file:Suppress("MemberVisibilityCanBePrivate")

package com.tezos.lang.michelson.lang

import com.intellij.lang.Language
import com.tezos.lang.michelson.lang.AnnotationType.*
import com.tezos.lang.michelson.lang.instruction.InstructionMetadata
import com.tezos.lang.michelson.lang.instruction.NamedAnnotation
import com.tezos.lang.michelson.lang.instruction.SimpleInstruction
import com.tezos.lang.michelson.lang.macro.*
import com.tezos.lang.michelson.lang.tag.SimpleTagMetadata
import com.tezos.lang.michelson.lang.tag.TagMetadata
import com.tezos.lang.michelson.lang.type.SimpleTypeMetadata
import com.tezos.lang.michelson.lang.type.TypeMetadata

/**
 * @author jansorg
 */
object MichelsonLanguage : Language("Michelson") {
    override fun getDisplayName(): String = "Michelson"

    // PAIR, Pair and pair are different tokens
    override fun isCaseSensitive(): Boolean = true

    // fixme contract is handled by the parser in a special way
    val TYPES: List<TypeMetadata> = listOf(
            "int".type(ParameterType.COMPARABLE_TYPE),
            "nat".type(ParameterType.COMPARABLE_TYPE),
            "string".type(ParameterType.COMPARABLE_TYPE),
            "bytes".type(ParameterType.COMPARABLE_TYPE),
            "mutez".type(ParameterType.COMPARABLE_TYPE),
            "bool".type(ParameterType.COMPARABLE_TYPE),
            "key_hash".type(ParameterType.COMPARABLE_TYPE),
            "timestamp".type(ParameterType.COMPARABLE_TYPE),
            "address".type(ParameterType.TYPE),
            "operation".type(ParameterType.TYPE),
            "key".type(ParameterType.TYPE),
            "unit".type(ParameterType.TYPE),
            "signature".type(ParameterType.TYPE),
            "option".type(ParameterType.TYPE, listOf(ParameterType.TYPE)),
            "list".type(ParameterType.TYPE, listOf(ParameterType.TYPE)),
            "set".type(ParameterType.TYPE, listOf(ParameterType.COMPARABLE_TYPE)),
            "pair".type(ParameterType.TYPE, listOf(ParameterType.TYPE, ParameterType.TYPE)),
            "or".type(ParameterType.TYPE, listOf(ParameterType.TYPE, ParameterType.TYPE)),
            "lambda".type(ParameterType.TYPE, listOf(ParameterType.TYPE, ParameterType.TYPE)),
            "map".type(ParameterType.TYPE, listOf(ParameterType.COMPARABLE_TYPE, ParameterType.TYPE)),
            "big_map".type(ParameterType.TYPE, listOf(ParameterType.COMPARABLE_TYPE, ParameterType.TYPE))
    )

    val TYPE_NAMES = TYPES.map { it.name }.toSet()

    fun findTypeMetadata(name: String) = TYPES.firstOrNull { it.name == name }

    private fun String.type(type: ParameterType, subtypes: List<ParameterType> = emptyList()): TypeMetadata {
        return SimpleTypeMetadata(this, type, subtypes)
    }

    val TYPE_COMPONENTS_WITH_FIELD_ANNOTATIONS = setOf("pair", "option", "or")

    // tag names
    val TAG_UNIT: TagMetadata = SimpleTagMetadata(0, "Unit")
    val TAG_NONE: TagMetadata = SimpleTagMetadata(0, "None")
    val TAG_BOOL: TagMetadata = SimpleTagMetadata(0, "True", "False")
    val TAG_PAIR: TagMetadata = SimpleTagMetadata(setOf("Pair"), 2, true)
    val TAG_SOME: TagMetadata = SimpleTagMetadata(setOf("Some"), 1, true)
    val TAG_OR: TagMetadata = SimpleTagMetadata(setOf("Left", "Right"), 1, true)
    val TAG_ELT: TagMetadata = SimpleTagMetadata(2, "Elt")
    val TAGS_METAS = listOf(
            TAG_UNIT,
            TAG_NONE,
            TAG_BOOL,
            TAG_PAIR,
            TAG_SOME,
            TAG_OR,
            TAG_ELT)
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
    val MACROS = listOf(
            FAIL_MACRO,
            ASSERT_MACROS,
            COMPARE_MACROS,
            IF_MACROS,
            DUUP_MACRO,
            DIIP_MACRO,
            PAIR_MACRO,
            UNPAIR_MACRO,
            CADR_MACRO,
            SET_CADR_MACRO,
            MAP_CADR_MACRO)
    // all available static macro names, dynamic macros like DIIIP or PAPAIR are not part of this list
    val MACRO_NAMES = MACROS.flatMap { it.staticNames() }

    // instructions
    val INSTRUCTIONS: List<InstructionMetadata> = listOf(
            "ABS".with(VARIABLE to 1),
            "ADD".with(VARIABLE to 1),
            "ADDRESS".with(VARIABLE to 1),
            "AMOUNT".with(VARIABLE to 1),
            "AND".with(VARIABLE to 1),
            "BALANCE".with(VARIABLE to 1),
            "BLAKE2B".with(VARIABLE to 1),
            "CAR".with(VARIABLE to 1),
            "CAST".with(VARIABLE to 1),
            "CDR".with(VARIABLE to 1),
            "CHECK_SIGNATURE".with(VARIABLE to 1),
            "COMPARE".with(VARIABLE to 1),
            "CONCAT".with(VARIABLE to 1),
            "CONS".with(VARIABLE to 1),
            "CREATE_ACCOUNT".with(VARIABLE to 2),
            "DIV".with(),
            "DROP".with(),
            "DUP".with(VARIABLE to 1),
            "EDIV".with(VARIABLE to 1),
            "EQ".with(VARIABLE to 1),
            "EXEC".with(VARIABLE to 1),
            "FAILWITH".with(),
            "GE".with(VARIABLE to 1),
            "GET".with(VARIABLE to 1),
            "GT".with(VARIABLE to 1),
            "HASH_KEY".with(VARIABLE to 1),
            "IMPLICIT_ACCOUNT".with(VARIABLE to 1),
            "INT".with(VARIABLE to 1),
            "LE".with(VARIABLE to 1),
            "LSL".with(VARIABLE to 1),
            "LSR".with(VARIABLE to 1),
            "LT".with(VARIABLE to 1),
            "MEM".with(VARIABLE to 1),
            "MOD".with(),
            "MUL".with(VARIABLE to 1),
            "NEG".with(VARIABLE to 1),
            "NEQ".with(VARIABLE to 1),
            "NOT".with(VARIABLE to 1),
            "NOW".with(VARIABLE to 1),
            "OR".with(VARIABLE to 1),
            "PACK".with(),
            "PAIR".with(VARIABLE to 1, TYPE to 1, FIELD to 2),
            "RENAME".with(VARIABLE to 1),
            "SELF".with(VARIABLE to 1),
            "SENDER".with(VARIABLE to 1),
            "SET_DELEGATE".with(VARIABLE to 1),
            "SHA256".with(),
            "SHA512".with(),
            "SIZE".with(VARIABLE to 1),
            "SLICE".with(VARIABLE to 1), // fixme questionable
            "SOME".with(VARIABLE to 1, TYPE to 1, FIELD to 1),
            "SOURCE".with(VARIABLE to 1),
            "STEPS_TO_QUOTA".with(VARIABLE to 1),
            "SUB".with(VARIABLE to 1),
            "SWAP".with(),
            "TRANSFER_TOKENS".with(VARIABLE to 1), // fixme questionable
            "UNIT".with(VARIABLE to 1, TYPE to 1), // fixme questionable var
            "UPDATE".with(VARIABLE to 1),
            "XOR".with(VARIABLE to 1),
            // one block
            "CREATE_CONTRACT".with(ParameterType.OPTIONAL_INSTRUCTION_BLOCK).and(VARIABLE to 2),
            "DIP".with(ParameterType.INSTRUCTION_BLOCK),
            "ITER".with(ParameterType.INSTRUCTION_BLOCK),
            "LOOP".with(ParameterType.INSTRUCTION_BLOCK),
            "LOOP_LEFT".with(ParameterType.INSTRUCTION_BLOCK),
            "MAP".with(ParameterType.INSTRUCTION_BLOCK).and(VARIABLE to 1),
            // two blocks
            "IF".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            "IF_CONS".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            "IF_LEFT".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            "IF_NONE".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            "IF_RIGHT".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            // one type
            "CONTRACT".with(ParameterType.TYPE).and(VARIABLE to 1),// fixme questionable annotation
            "EMPTY_SET".with(ParameterType.TYPE).and(VARIABLE to 1, TYPE to 1),
            "LEFT".with(ParameterType.TYPE).and(VARIABLE to 1, TYPE to 1, FIELD to 2),
            "NIL".with(ParameterType.TYPE).and(VARIABLE to 1, TYPE to 1),
            "NONE".with(ParameterType.TYPE).and(VARIABLE to 1, TYPE to 1, FIELD to 1),
            "RIGHT".with(ParameterType.TYPE).and(VARIABLE to 1, TYPE to 1, FIELD to 2),
            "UNPACK".with(ParameterType.TYPE),
            // other
            "EMPTY_MAP".with(ParameterType.COMPARABLE_TYPE, ParameterType.TYPE).and(VARIABLE to 1, TYPE to 1),
            "LAMBDA".with(ParameterType.TYPE, ParameterType.TYPE, ParameterType.INSTRUCTION_BLOCK).and(VARIABLE to 1),
            "PUSH".with(ParameterType.TYPE, ParameterType.DATA).and(VARIABLE to 1),
            // questionable
            "IS_NAT".with(VARIABLE to 1), // fixme qustionable
            "ISNAT".with(VARIABLE to 1) // fixme questionable
    )

    // creates instruction metadata from an instruction name
    private fun String.with(vararg params: ParameterType, annotations: Map<AnnotationType, Short> = emptyMap(), predefinedAnnotations: List<NamedAnnotation> = emptyList()) = SimpleInstruction(this, params.toList(), annotations, predefinedAnnotations)

    private fun String.with(vararg annotations: Pair<AnnotationType, Int>): SimpleInstruction {
        val intMap = annotations.map { (k, v) -> k to v.toShort() }.toMap()
        return SimpleInstruction(this, emptyList(), intMap, emptyList())
    }

    private fun SimpleInstruction.and(vararg values: Pair<AnnotationType, Int>): SimpleInstruction {
        return this.copy(annotations = values.map { (k, v) -> k to v.toShort() }.toMap())
    }
}
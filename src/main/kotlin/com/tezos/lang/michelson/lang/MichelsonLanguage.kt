package com.tezos.lang.michelson.lang

import com.intellij.lang.Language
import com.tezos.client.stack.MichelsonStack
import com.tezos.client.stack.MichelsonStackType
import com.tezos.lang.michelson.lang.AnnotationType.*
import com.tezos.lang.michelson.lang.LangTypes.ADDRESS
import com.tezos.lang.michelson.lang.LangTypes.BOOL
import com.tezos.lang.michelson.lang.LangTypes.BYTES
import com.tezos.lang.michelson.lang.LangTypes.CONTRACT
import com.tezos.lang.michelson.lang.LangTypes.FAILED
import com.tezos.lang.michelson.lang.LangTypes.INT
import com.tezos.lang.michelson.lang.LangTypes.KEY
import com.tezos.lang.michelson.lang.LangTypes.KEY_HASH
import com.tezos.lang.michelson.lang.LangTypes.LAMBDA
import com.tezos.lang.michelson.lang.LangTypes.LIST
import com.tezos.lang.michelson.lang.LangTypes.MAP
import com.tezos.lang.michelson.lang.LangTypes.MUTEZ
import com.tezos.lang.michelson.lang.LangTypes.NAT
import com.tezos.lang.michelson.lang.LangTypes.OPERATION
import com.tezos.lang.michelson.lang.LangTypes.OPTION
import com.tezos.lang.michelson.lang.LangTypes.OR
import com.tezos.lang.michelson.lang.LangTypes.PAIR
import com.tezos.lang.michelson.lang.LangTypes.SET
import com.tezos.lang.michelson.lang.LangTypes.SIGNATURE
import com.tezos.lang.michelson.lang.LangTypes.STRING
import com.tezos.lang.michelson.lang.LangTypes.TIMESTAMP
import com.tezos.lang.michelson.lang.LangTypes.UNIT
import com.tezos.lang.michelson.lang.StackTransformations.adding
import com.tezos.lang.michelson.lang.StackTransformations.dropping
import com.tezos.lang.michelson.lang.StackTransformations.transforming
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
            "ABS".with(VARIABLE to 1).with(transforming("int" to "nat")),
            "ADD".with(VARIABLE to 1).with(transforming(
                    // numbers
                    listOf(INT, INT) to INT,
                    listOf(INT, INT) to INT,
                    listOf(NAT, NAT) to INT,
                    listOf(NAT, NAT) to NAT,
                    // timestamps
                    listOf(TIMESTAMP, INT) to TIMESTAMP,
                    listOf(INT, TIMESTAMP) to TIMESTAMP,
                    // mutez
                    listOf(MUTEZ, MUTEZ) to MUTEZ
            )),
            "ADDRESS".with(VARIABLE to 1).with(transforming(CONTRACT(LangTypes.ANY) to ADDRESS)),
            "AMOUNT".with(VARIABLE to 1).with(adding(MUTEZ)),
            "AND".with(VARIABLE to 1).with(transforming(
                    listOf(BOOL, BOOL) to BOOL,
                    listOf(NAT, NAT) to NAT,
                    listOf(INT, NAT) to NAT
            )),
            "BALANCE".with(VARIABLE to 1).with(adding(MUTEZ)),
            "BLAKE2B".with(VARIABLE to 1).with(transforming(BYTES to BYTES)),
            "CAR".with(VARIABLE to 1).with(PairTransformation { arguments[0] }),
            // fixme we could add a warning when cast is called on a incompatible input type
            "CAST".with(ParameterType.TYPE).and(VARIABLE to 1).with(TopItemTransformation { _, argTypes -> listOf(argTypes[0]) }),
            "CDR".with(VARIABLE to 1).with(PairTransformation { arguments[1] }),
            "CHECK_SIGNATURE".with(VARIABLE to 1).with(transforming(listOf(KEY, SIGNATURE, BYTES) to BOOL)),
            "COMPARE".with(VARIABLE to 1).with(transforming(KEY_HASH to KEY_HASH)),
            "CONCAT".with(VARIABLE to 1).with(transforming(
                    // string + string -> string
                    listOf(STRING, STRING) to STRING,
                    // (list string) -> string
                    listOf(LIST(STRING)) to STRING,
                    // bytes + bytes -> bytes
                    listOf(BYTES, BYTES) to BYTES,
                    // (list bytes) -> bytes
                    listOf(LIST(BYTES)) to BYTES
            )),
            "CONS".with(VARIABLE to 1).with(TwoTopItemsTransformation { first, second, argTypes ->
                if (second.name != "list" || second.arguments.size < 1 || second.arguments[0] != first) {
                    throw UnsupportedOperationException()
                }
                listOf(second)
            }),
            "CREATE_ACCOUNT".with(VARIABLE to 2).with(transforming(
                    listOf(KEY_HASH, OPTION(KEY_HASH), BOOL, MUTEZ) to listOf(OPERATION, CONTRACT(UNIT))
            )),
            "DIV".with(),//fixme not in spec anymore?
            "DROP".with().with(dropping(1)),
            "DUP".with(VARIABLE to 1).with(TopItemTransformation { item, argTypes -> listOf(item, item) }),
            "EDIV".with(VARIABLE to 1).with(transforming(
                    // numbers
                    listOf(INT, INT) to OPTION(PAIR(INT, NAT)),
                    listOf(INT, NAT) to OPTION(PAIR(INT, NAT)),
                    listOf(NAT, INT) to OPTION(PAIR(INT, NAT)),
                    listOf(NAT, NAT) to OPTION(PAIR(INT, NAT)),
                    //mutez
                    listOf(MUTEZ, NAT) to OPTION(PAIR(MUTEZ, MUTEZ)),
                    listOf(MUTEZ, MUTEZ) to OPTION(PAIR(NAT, MUTEZ))
            )),
            "EQ".with(VARIABLE to 1).with(transforming(INT to BOOL)),
            "EXEC".with(VARIABLE to 1).with(TwoTopItemsTransformation { first, second, _ ->
                if (second.name != "lambda" || second.arguments.size != 2 || second.arguments[0].isType(first)) {
                    throw UnsupportedOperationException()
                }
                listOf(second.arguments[1])
            }),
            "FAILWITH".with().with(TopItemTransformation { item, argTypes ->
                listOf(FAILED)
            }),
            "GE".with(VARIABLE to 1).with(transforming(INT to BOOL)),
            "GET".with(VARIABLE to 1).with(TwoTopItemsTransformation { key, map, argTypes ->
                if (map.name != "map" || map.arguments.size != 2 || map.arguments[0].isType(key)) {
                    throw UnsupportedOperationException()
                }
                listOf(OPTION(map.arguments[1]))
            }),
            "GT".with(VARIABLE to 1).with(transforming(INT to BOOL)),
            "HASH_KEY".with(VARIABLE to 1).with(transforming(KEY to KEY_HASH)),
            "IMPLICIT_ACCOUNT".with(VARIABLE to 1).with(transforming(KEY_HASH to CONTRACT(UNIT))),
            "INT".with(VARIABLE to 1), //fixme not found in spec
            "LE".with(VARIABLE to 1).with(transforming(INT to BOOL)),
            "LSL".with(VARIABLE to 1).with(transforming(listOf(NAT, NAT) to NAT)),
            "LSR".with(VARIABLE to 1).with(transforming(listOf(NAT, NAT) to NAT)),
            "LT".with(VARIABLE to 1).with(transforming(INT to BOOL)),
            "MEM".with(VARIABLE to 1).with(TwoTopItemsTransformation { first, second, argTypes ->
                if (second.name != "set" || second.arguments.size != 1 || !second.arguments[0].isType(first)) {
                    throw UnsupportedOperationException()
                }
                listOf(BOOL)
            }),
            "MOD".with(),//fixme not in mainner grammar any more
            "MUL".with(VARIABLE to 1).with(transforming(
                    // numbers
                    listOf(INT, INT) to INT,
                    listOf(INT, INT) to INT,
                    listOf(NAT, NAT) to INT,
                    listOf(NAT, NAT) to NAT,
                    // mutez
                    listOf(MUTEZ, NAT) to MUTEZ,
                    listOf(MUTEZ, MUTEZ) to MUTEZ
            )),
            "NEG".with(VARIABLE to 1).with(transforming(
                    INT to INT,
                    NAT to INT
            )),
            "NEQ".with(VARIABLE to 1).with(transforming(INT to BOOL)),
            "NOT".with(VARIABLE to 1).with(transforming(
                    BOOL to BOOL,
                    NAT to NAT,
                    INT to INT
            )),
            "NOW".with(VARIABLE to 1).with(adding(TIMESTAMP)),
            "OR".with(VARIABLE to 1).with(transforming(BOOL to BOOL)),
            "PACK".with().with(TopItemTransformation { item, argTypes ->
                listOf(BYTES)
            }),
            "PAIR".with(VARIABLE to 1, TYPE to 1, FIELD to 2).with(TwoTopItemsTransformation { first, second, argTypes ->
                listOf(PAIR(first, second))
            }),
            "RENAME".with(VARIABLE to 1).with(TopItemTransformation { item, argTypes ->
                listOf(item) //fixme annotations are not yet handled here
            }),
            "SELF".with(VARIABLE to 1).with(adding(CONTRACT(LangTypes.ANY))), //fixme find out type of current contract
            "SENDER".with(VARIABLE to 1).with(adding(ADDRESS)),
            "SET_DELEGATE".with(VARIABLE to 1).with(transforming(OPTION(KEY_HASH) to OPERATION)),
            "SHA256".with().with(transforming(
                    BYTES to BYTES
            )),
            "SHA512".with().with(transforming(
                    BYTES to BYTES
            )),
            "SIZE".with(VARIABLE to 1).with(transforming(
                    STRING to NAT,
                    SET(LangTypes.ANY) to NAT,
                    MAP(LangTypes.ANY, LangTypes.ANY) to NAT,
                    LIST(LangTypes.ANY) to NAT,
                    BYTES to NAT
            )),
            // fixme questionable
            "SLICE".with(VARIABLE to 1).with(transforming(
                    listOf(NAT, NAT, STRING) to OPTION(STRING),
                    listOf(NAT, NAT, BYTES) to OPTION(BYTES)
            )),
            "SOME".with(VARIABLE to 1, TYPE to 1, FIELD to 1).with(TopItemTransformation { item, argTypes ->
                listOf(OPTION(item))
            }),
            "SOURCE".with(VARIABLE to 1).with(adding(ADDRESS)),
            "STEPS_TO_QUOTA".with(VARIABLE to 1).with(adding(NAT)),
            "SUB".with(VARIABLE to 1).with(transforming(
                    // numbers
                    listOf(INT, INT) to INT,
                    listOf(INT, INT) to INT,
                    listOf(NAT, NAT) to INT,
                    listOf(NAT, NAT) to INT,
                    // timestamps
                    listOf(TIMESTAMP, INT) to TIMESTAMP,
                    listOf(TIMESTAMP, TIMESTAMP) to TIMESTAMP,
                    // mutez
                    listOf(MUTEZ, MUTEZ) to MUTEZ
            )),
            "SWAP".with().with(TwoTopItemsTransformation { first, second, argTypes ->
                listOf(second, first)
            }),
            "TRANSFER_TOKENS".with(VARIABLE to 1).with(TopThreeItemsTransformation { first, second, third, argTypes ->
                if (!second.isType(MUTEZ) || third.name != "contract" || third.arguments.size != 1 || !third.arguments[0].isType(first)) {
                    throw UnsupportedOperationException()
                }
                listOf(OPERATION)
            }), // fixme questionable
            "UNIT".with(VARIABLE to 1, TYPE to 1).with(adding(UNIT)), // fixme questionable var
            "UPDATE".with(VARIABLE to 1).with(TopThreeItemsTransformation { first, second, third, argTypes ->
                if (!second.isType(BOOL) || third.name != "set" || third.arguments.size != 1 || !third.arguments[0].isType(first)) {
                    throw UnsupportedOperationException()
                }
                listOf(SET(first)) //fixme annotations, reuse third here?
            }),
            "XOR".with(VARIABLE to 1).with(transforming(
                    listOf(BOOL, BOOL) to BOOL,
                    listOf(NAT, NAT) to NAT
            )),
            // one block
            // :: key_hash : option key_hash : bool : bool : mutez : lambda (pair 'p 'g) (pair (list operation) 'g) : 'g : 'S -> operation : address : 'S
            // the last lambda stack element is optional
            "CREATE_CONTRACT".with(ParameterType.OPTIONAL_INSTRUCTION_BLOCK).and(VARIABLE to 2).with(object : StackTransformation {
                override fun supports(stack: MichelsonStack): Boolean {
                    return stack.size >= 5
                }

                override fun transform(meta: InstructionMetadata, stack: MichelsonStack, argTypes: List<MichelsonStackType>): MichelsonStack {
                    val (items, expectLambda) = when (stack.size >= 6) {
                        true -> stack.frames.subList(0, 6) to true
                        false -> stack.frames.subList(0, 5) to false
                    }

                    if (!items[0].isType(KEY_HASH) || !items[1].isType(OPTION(KEY_HASH)) || !items[2].isType(BOOL) || !items[3].isType(BOOL) || !items[4].isType(MUTEZ)) {
                        throw UnsupportedOperationException()
                    }
                    if (expectLambda && items[5].type.name != "lambda") {
                        throw UnsupportedOperationException()
                    }

                    val result = if (expectLambda) stack.drop(6) else stack.drop(5)
                    return result.pushTypes(listOf(OPERATION, ADDRESS))
                }
            }),
            //fixme
            "DIP".with(ParameterType.INSTRUCTION_BLOCK),
            // fixme hard to understand spec (set, map, lists)
            "ITER".with(ParameterType.INSTRUCTION_BLOCK),
            "LOOP".with(ParameterType.INSTRUCTION_BLOCK).with(transforming(
                    listOf(BOOL) to emptyList<MichelsonStackType>()
            )),
            // :: (or 'a 'b) : 'A   ->  'b : 'A
            "LOOP_LEFT".with(ParameterType.INSTRUCTION_BLOCK).with(TopItemTransformation { item, argTypes ->
                if (item.name != "or" || item.arguments.size != 2) {
                    throw UnsupportedOperationException()
                }
                listOf(item.arguments[1])
            }),
            // :: (map 'key 'val) : 'A   ->  (map 'key 'b) : 'A
            // fixme implement when result type of code block is available
            "MAP".with(ParameterType.INSTRUCTION_BLOCK).and(VARIABLE to 1),
            // two blocks
            // fixme implement when result type of code block is available
            "IF".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            // fixme implement when result type of code block is available
            "IF_CONS".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            // fixme implement when result type of code block is available
            "IF_LEFT".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            // fixme implement when result type of code block is available
            "IF_NONE".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            // fixme implement when result type of code block is available
            "IF_RIGHT".with(ParameterType.INSTRUCTION_BLOCK, ParameterType.INSTRUCTION_BLOCK),
            // one type parameter
            // fixme questionable annotation
            // :: address : 'S   ->   option (contract 'p) : 'S
            "CONTRACT".with(ParameterType.TYPE).and(VARIABLE to 1).with(TopItemTransformation { item, argTypes ->
                if (!item.isType(ADDRESS) || argTypes.size != 1) {
                    throw UnsupportedOperationException()
                }
                listOf(OPTION(CONTRACT(argTypes[0])))
            }),
            "EMPTY_SET".with(ParameterType.TYPE).and(VARIABLE to 1, TYPE to 1).with(NoItemTransformation { argTypes ->
                if (argTypes.size != 1) {
                    throw UnsupportedOperationException()
                }
                listOf(SET(argTypes[0]))
            }),
            "LEFT".with(ParameterType.TYPE).and(VARIABLE to 1, TYPE to 1, FIELD to 2).with(TopItemTransformation { item, argTypes ->
                if (argTypes.size != 1) {
                    throw UnsupportedOperationException()
                }
                listOf(OR(item, argTypes[0]))
            }),
            "NIL".with(ParameterType.TYPE).and(VARIABLE to 1, TYPE to 1).with(NoItemTransformation { argTypes ->
                if (argTypes.size != 1) {
                    throw UnsupportedOperationException()
                }
                listOf(LIST(argTypes[0]))
            }),
            "NONE".with(ParameterType.TYPE).and(VARIABLE to 1, TYPE to 1, FIELD to 1).with(NoItemTransformation { argTypes ->
                if (argTypes.size != 1) {
                    throw UnsupportedOperationException()
                }
                listOf(OPTION(argTypes[0]))
            }),
            "RIGHT".with(ParameterType.TYPE).and(VARIABLE to 1, TYPE to 1, FIELD to 2).with(TopItemTransformation { item, argTypes ->
                if (argTypes.size != 1) {
                    throw UnsupportedOperationException()
                }
                listOf(OR(argTypes[0], item))
            }),
            "UNPACK".with(ParameterType.TYPE).with(TopItemTransformation { item, argTypes ->
                if (!item.isType(BYTES) || argTypes.size != 1) {
                    throw UnsupportedOperationException()
                }
                listOf(OPTION(argTypes[0]))
            }),
            // other
            "EMPTY_MAP".with(ParameterType.COMPARABLE_TYPE, ParameterType.TYPE).and(VARIABLE to 1, TYPE to 1).with(NoItemTransformation { argTypes ->
                if (argTypes.size != 2) {
                    throw UnsupportedOperationException()
                }
                listOf(MAP(argTypes[0], argTypes[1]))
            }),
            "LAMBDA".with(ParameterType.TYPE, ParameterType.TYPE, ParameterType.INSTRUCTION_BLOCK).and(VARIABLE to 1).with(NoItemTransformation { argTypes ->
                if (argTypes.size != 2) {
                    throw UnsupportedOperationException()
                }
                listOf(LAMBDA(argTypes[0], argTypes[1]))
            }),
            "PUSH".with(ParameterType.TYPE, ParameterType.DATA).and(VARIABLE to 1).with(NoItemTransformation { argTypes ->
                if (argTypes.size != 2) {
                    throw UnsupportedOperationException()
                }
                listOf(argTypes[0])
            }),
            // questionable
            "IS_NAT".with(VARIABLE to 1), // fixme qustionable
            "ISNAT".with(VARIABLE to 1) // fixme questionable
    )

    // creates instruction metadata from an instruction name
    private fun String.with(vararg params: ParameterType, annotations: Map<AnnotationType, Short> = emptyMap(), predefinedAnnotations: List<NamedAnnotation> = emptyList()) = SimpleInstruction(this, params.toList(), annotations, predefinedAnnotations)

    private fun String.with(vararg annotations: Pair<AnnotationType, Int>, transformation: StackTransformation = UnsupportedTransformation): SimpleInstruction {
        val intMap = annotations.map { (k, v) -> k to v.toShort() }.toMap()
        return SimpleInstruction(this, emptyList(), intMap, emptyList(), transformation)
    }

    private fun SimpleInstruction.and(vararg values: Pair<AnnotationType, Int>): SimpleInstruction {
        return this.copy(supportedAnnotations = values.map { (k, v) -> k to v.toShort() }.toMap())
    }
}


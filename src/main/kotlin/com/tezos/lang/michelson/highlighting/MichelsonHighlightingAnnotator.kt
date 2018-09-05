package com.tezos.lang.michelson.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import com.tezos.lang.michelson.psi.PsiGenericInstruction
import com.tezos.lang.michelson.psi.PsiGenericType
import com.tezos.lang.michelson.psi.PsiMacroInstruction
import com.tezos.lang.michelson.psi.PsiType

/**
 * Highlighting annotator for Michelson.
 * This is our main syntax and semantic validation logic apart from the parser.
 * All syntax errors which are not caught by the parser have to be handled in this annotator. A unit test must be added for each
 * case.
 *
 * @author jansorg
 */
class MichelsonHighlightingAnnotator : Annotator {
    private companion object {
        // the supported generic types in the Michelson language, doesn't contain the comparable types.
        // the lexer takes care to match comparable types
        val TYPES = setOf("key", "unit", "signature", "option", "list", "set", "operation", "contract", "pair", "or", "lambda", "map", "big_map")

        // instructions which do not support arguments
        val INSTRUCTIONS_NO_ARGS = setOf("ABS", "ADD", "ADDRESS", "AMOUNT", "AND", "BALANCE", "BLAKE2B",
                "CAR", "CAST", "CDR", "CHECK_SIGNATURE", "COMPARE", "CONCAT", "CONS",
                "CREATE_ACCOUNT", "CREATE_CONTRACT", "DIV", "DROP", "DUP",
                "EDIV", "EQ", "EXEC", "FAILWITH", "GE", "GET", "GT",
                "HASH_KEY", "IMPLICIT_ACCOUNT", "INT", "LE", "LSL", "LSR", "LT", "MEM", "MOD", "MUL", "NEG", "NEQ", "NOT", "NOW",
                "OR", "PACK", "PAIR", "RENAME",
                "SELF", "SENDER", "SET_DELEGATE", "SHA256", "SHA512", "SIZE", "SLICE", "SOME", "SOURCE", "STEPS_TO_QUOTA", "SUB", "SWAP",
                "TRANSFER_TOKENS", "UNIT", "UPDATE", "XOR")

        // instructions which are not fully explained in the whitepaper
        val QUESTIONABLE_INSTRUCTIONS = setOf("ISNAT", "REDUCE")

        // instructions which expect one instruction block
        val INSTRUCTIONS_ONE_BLOCK = setOf("DIP", "ITER", "LOOP", "LOOP_LEFT", "MAP")
        // instructions which expect two instruction blocks
        val INSTRUCTIONS_TWO_BLOCKS = setOf("IF", "IF_CONS", "IF_LEFT", "IF_RIGHT", "IF_NONE")
        // instructions which expect one type as argument
        val INSTRUCTIONS_ONE_TYPE = setOf("CONTRACT", "EMPTY_SET", "LEFT", "NIL", "NONE", "RIGHT", "UNPACK")

        // macros which support no arguments
        val MACROS_NO_ARGS = setOf(
                "CMPEQ", "CMPNEQ", "CMPLT", "CMPGT", "CMPLE", "CMPGE",
                "ASSERT", "ASSERT_EQ", "ASSERT_NEQ", "ASSERT_LT", "ASSERT_LE", "ASSERT_GT",
                "ASSERT_GE", "ASSERT_CMPEQ", "ASSERT_CMPNEQ", "ASSERT_CMPLT", "ASSERT_CMPLE",
                "ASSERT_CMPGT", "ASSERT_CMPGE", "ASSERT_NONE", "ASSERT_SOME", "ASSERT_LEFT",
                "ASSERT_RIGHT")

        // macros whch expect two instruction blocks
        val MACROS_TWO_BLOCKS = setOf("IFEQ", "IFNEQ", "IFLT", "IFGT", "IFLE", "IFGE",
                "IFCMPEQ", "IFCMPNEQ", "IFCMPLT", "IFCMPGT", "IFCMPLE", "IFCMPGE",
                "IF_SOME")
    }

    override fun annotate(psi: PsiElement, holder: AnnotationHolder) {
        when (psi) {
            is PsiGenericType -> annotateGenericType(psi, holder)
            is PsiGenericInstruction -> annotateInstruction(psi, holder)
            is PsiMacroInstruction -> annotateMacroInstruction(psi, holder)
        }
    }

    private fun annotateInstruction(psi: PsiGenericInstruction, holder: AnnotationHolder) {
        val name = psi.instructionName ?: return
        val instruction = psi.instructionToken

        val blocks = psi.instructionBlocks
        val types = psi.typeList
        val datas = psi.dataList

        val oneBlockCommand = name in INSTRUCTIONS_ONE_BLOCK
        val twoBlocksCommand = name in INSTRUCTIONS_TWO_BLOCKS
        val noArgsCommand = name in INSTRUCTIONS_NO_ARGS
        val oneTypeCommand = INSTRUCTIONS_ONE_TYPE.contains(name)
        val unknownCommand = !oneBlockCommand && !twoBlocksCommand && !noArgsCommand && !oneTypeCommand && name !in QUESTIONABLE_INSTRUCTIONS

        val blockCount = blocks.size
        val typeCount = types.size
        val dataCount = datas.size

        when {
            // commands which expect a single instruction block
            oneBlockCommand && blockCount != 1 -> holder.createErrorAnnotation(instruction, "One block expected")

            // commands which expect two instruction blocks
            twoBlocksCommand && blockCount != 2 -> holder.createErrorAnnotation(instruction, "Two blocks expected")

            // commands which expect no arguments
            noArgsCommand && (blockCount != 0 || typeCount != 0 || dataCount != 0) -> {
                holder.createErrorAnnotation(instruction, "$name doesn't support arguments")
            }

            // commands which support a single type argument
            oneTypeCommand && typeCount != 1 -> holder.createErrorAnnotation(instruction, "Type argument expected")

            // PUSH <type> <data>
            name == "PUSH" -> annotatePushInstruction(typeCount, dataCount, holder, instruction)

            // EMPTY_MAP <comparable type> <type>
            name == "EMPTY_MAP" -> annotateEmptyMapInstruction(typeCount, holder, instruction, types)

            // LAMBDA <type> <type> { <instruction> ... }
            name == "LAMBDA" -> annotateLambdaInstruction(typeCount, holder, instruction, blockCount)

            // unknown commands
            unknownCommand -> holder.createErrorAnnotation(instruction, "Unknown instruction")
        }
    }

    private fun annotateLambdaInstruction(typeCount: Int, holder: AnnotationHolder, instruction: PsiElement, blockCount: Int) {
        when {
            typeCount != 2 -> holder.createErrorAnnotation(instruction, "Expected two types")
            blockCount != 1 -> holder.createErrorAnnotation(instruction, "Expected an instruction block")
        }
    }

    private fun annotateEmptyMapInstruction(typeCount: Int, holder: AnnotationHolder, instruction: PsiElement, types: List<PsiType>) {
        when {
            typeCount != 2 -> holder.createErrorAnnotation(instruction, "Expected two types")
            !types[0].isComparable -> holder.createErrorAnnotation(types[0], "Expected a comparable type")
        }
    }

    private fun annotatePushInstruction(typeCount: Int, dataCount: Int, holder: AnnotationHolder, instruction: PsiElement) {
        when {
            typeCount == 0 && dataCount == 0 -> holder.createErrorAnnotation(instruction, "Expected type and data")
            typeCount == 0 && dataCount == 1 -> holder.createErrorAnnotation(instruction, "Expected a type")
            typeCount == 1 && dataCount == 0 -> holder.createErrorAnnotation(instruction, "Expected data")
            typeCount != 1 || dataCount != 1 -> holder.createErrorAnnotation(instruction, "Expected one type and one data element")
        }
    }

    private fun annotateGenericType(element: PsiGenericType, holder: AnnotationHolder) {
        val typeName = element.typeNameString
        if (!TYPES.contains(typeName)) {
            holder.createErrorAnnotation(element.typeToken, "Unknown type")
            return
        }
    }

    private fun annotateMacroInstruction(psi: PsiMacroInstruction, holder: AnnotationHolder) {
        val macroName = psi.instructionName ?: throw IllegalStateException("macro name not found")
        val macroToken = psi.instructionToken ?: throw IllegalStateException("macro token not found")

        val blocks = psi.instructionBlocks
        val blockCount = blocks.size

        when {
            macroName in MACROS_NO_ARGS && blockCount != 0 -> {
                holder.createErrorAnnotation(macroToken, "Macro does not support instructions")
                // fixme validate annotations
            }

            macroName in MACROS_TWO_BLOCKS && blockCount != 2 -> {
                holder.createErrorAnnotation(macroToken, "Two instruction blocks expected")
                // fixme validate annotations
            }

            // handle the special macros
            macroName.startsWith("DII") -> annotateDiipMacro(macroToken, holder, blockCount)
            macroName.startsWith("DUU") -> annotateDuupMacro(macroToken, holder, blockCount)
            macroName.startsWith('P') -> annotatePairMacro(macroToken, holder, blockCount)
            macroName.startsWith('U') -> annotateUnpairMacro(macroToken, holder, blockCount)
            macroName.startsWith("CA") || macroName.startsWith("CD") -> annotateCadrMacro(macroToken, holder, blockCount)
        }
    }

    private fun annotateDiipMacro(macroToken: PsiElement, holder: AnnotationHolder, blockCount: Int) {
        when {
            blockCount == 0 -> holder.createErrorAnnotation(macroToken, "Expected an instruction block")
            blockCount > 1 -> holder.createErrorAnnotation(macroToken, "Only one instruction block supported")
        }
    }

    private fun annotateDuupMacro(macroToken: PsiElement, holder: AnnotationHolder, blockCount: Int) {
        when {
            blockCount == 1 -> holder.createErrorAnnotation(macroToken, "Unexpected instruction block")
            blockCount > 1 -> holder.createErrorAnnotation(macroToken, "Unexpected instruction blocks")
        }
    }

    private fun annotatePairMacro(macroToken: PsiElement, holder: AnnotationHolder, blockCount: Int) {
        when {
            blockCount == 1 -> holder.createErrorAnnotation(macroToken, "Unexpected instruction block")
            blockCount > 1 -> holder.createErrorAnnotation(macroToken, "Unexpected instruction blocks")
        }
        //fixme validate token name
    }

    private fun annotateUnpairMacro(macroToken: PsiElement, holder: AnnotationHolder, blockCount: Int) {
        when {
            blockCount == 1 -> holder.createErrorAnnotation(macroToken, "Unexpected instruction block")
            blockCount > 1 -> holder.createErrorAnnotation(macroToken, "Unexpected instruction blocks")
        }
        //fixme validate token name
    }

    private fun annotateCadrMacro(macroToken: PsiElement, holder: AnnotationHolder, blockCount: Int) {
        when {
            blockCount == 1 -> holder.createErrorAnnotation(macroToken, "Unexpected instruction block")
            blockCount > 1 -> holder.createErrorAnnotation(macroToken, "Unexpected instruction blocks")
        }
        //fixme validate token name
    }
}
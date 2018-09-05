package com.tezos.lang.michelson.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import com.tezos.lang.michelson.psi.PsiGenericInstruction
import com.tezos.lang.michelson.psi.PsiGenericType

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

        val INSTRUCTIONS_NO_ARGS = setOf("ABS", "ADD", "AMOUNT", "AND", "BALANCE", "BLAKE2B", "CAR", "CAST", "CDR", "CHECK_SIGNATURE", "COMPARE", "CONCAT",
                "CONS", "CREATE_ACCOUNT", "CREATE_CONTRACT", "DIV", "DROP", "DUP", "EQ", "EXEC", "FAILWITH", "GE", "GET", "GT",
                "HASH_KEY", "IMPLICIT_ACCOUNT", "INT", "LE", "LSL", "LSR", "LT", "MEM", "MOD", "MUL", "NEG", "NEQ", "NOT", "NOW",
                "OR", "PAIR", "RENAME", "SELF", "SENDER", "SET_DELEGATE", "SOME", "SOURCE", "STEPS_TO_QUOTA", "SUB", "SWAP",
                "TRANSFER_TOKENS", "UNIT", "UPDATE", "XOR")

        // instructions which expect one instruction block
        val INSTRUCTIONS_ONE_BLOCK = setOf("ITER", "LOOP", "LOOP_LEFT", "MAP")
        // instructions which expect two instruction blocks
        val INSTRUCTIONS_TWO_BLOCKS = setOf("IF", "IF_CONS", "IF_LEFT", "IF_NONE")
        // instructions which expect one type as argument
        val INSTRUCTIONS_ONE_TYPE = setOf("EMPTY_SET", "LEFT", "NIL", "NONE", "RIGHT")
        /*
        others: PUSH <type> <data>
                EMPTY_MAP <comparable type> <type>
                DIP { <instruction> ... }
                LAMBDA <type> <type> { <instruction> ... }
         */
    }

    override fun annotate(psi: PsiElement, holder: AnnotationHolder) {
        when (psi) {
            is PsiGenericType -> annotateGenericType(psi, holder)
            is PsiGenericInstruction -> annotateInstruction(psi, holder)
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
        val unknownCommand = !oneBlockCommand && !twoBlocksCommand && !noArgsCommand && !oneTypeCommand
        val blockCount = blocks.size

        when {
            // commands which expect a single instruction block
            oneBlockCommand && blockCount != 1 -> holder.createErrorAnnotation(instruction, "One block expected")

            // commands which expect two instruction blocks
            twoBlocksCommand && blockCount != 2 -> holder.createErrorAnnotation(instruction, "Two blocks expected")

            // commands which expect no arguments
            noArgsCommand && (blockCount != 0 || types.size != 0 || datas.size != 0) -> {
                holder.createErrorAnnotation(instruction, "$name doesn't support arguments")
            }

            // commands which support a single type argument
            oneTypeCommand && types.size != 1 -> {
                holder.createErrorAnnotation(instruction, "Type argument expected")
            }

            // unknown commands
            unknownCommand -> holder.createErrorAnnotation(psi, "Unknown instruction")
        }
    }

    private fun annotateGenericType(element: PsiGenericType, holder: AnnotationHolder) {
        val typeName = element.typeNameString
        if (!TYPES.contains(typeName)) {
            holder.createErrorAnnotation(element, "Unknown type")
            return
        }
    }
}
package com.tezos.lang.michelson.editor.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.lang.macro.*
import com.tezos.lang.michelson.psi.*

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
        val LEAF_TOKENS = TokenSet.create(MichelsonTypes.STRING_ESCAPE_INVALID)
    }

    override fun annotate(psi: PsiElement, holder: AnnotationHolder) {
        val nodeType = psi.node.elementType
        if (LEAF_TOKENS.contains(nodeType)) {
            annotateLeafElement(psi, nodeType, holder)
            return
        }

        // generic type and instruction highlighting
        when (psi) {
            is PsiGenericType -> annotateGenericType(psi, holder)
            is PsiGenericInstruction -> annotateInstruction(psi, holder)
            is PsiMacroInstruction -> annotateMacroInstruction(psi, holder)
        }

        // annotation highlighting
        when (psi) {
            is PsiType -> annotateAnnotationsOfType(psi, holder)
            is PsiGenericInstruction -> annotateInstructionAnnotations(psi, holder)
            //fixme handle contract instruction
            is PsiMacroInstruction -> annotateMacroAnnotations(psi, holder)
        }
    }

    /**
     * Instructions support different numbers of variable annotations, depending on the name.
     * The spec also states: to improve readability and robustness, instructions CAR and CDR.
     */
    private fun annotateInstructionAnnotations(psi: PsiGenericInstruction, holder: AnnotationHolder) {
        val instructionName = psi.instructionName
        val annotations = psi.annotations
        val annotationCount = annotations.size

        when {
            // mark annotations on instructions which do not support them
            instructionName in MichelsonLanguage.INSTRUCTIONS_NO_ANNOTATION && annotationCount > 0 -> {
                for (annotation in annotations) {
                    holder.createErrorAnnotation(annotation, "Unexpected annotation")
                }
            }

            annotationCount != 0 -> {
                var varAnnotations = 0
                val maxVarAnnotations = when (instructionName) {
                    in MichelsonLanguage.INSTRUCTIONS_ONE_VAR_ANNOTATION -> 1
                    in MichelsonLanguage.INSTRUCTIONS_ONE_VAR_ANNOTATION_QUESTIONABLE -> 1 //fixme
                    in MichelsonLanguage.INSTRUCTIONS_TWO_VAR_ANNOTATIONS -> 2
                    else -> 0
                }

                var fieldAnnotations = 0
                val maxFieldAnnotations = when {
                    instructionName in MichelsonLanguage.INSTRUCTIONS_ONE_FIELD_ANNOTATION -> 1
                    instructionName in MichelsonLanguage.INSTRUCTIONS_TWO_FIELD_ANNOTATIONS -> 2
                    else -> 0
                }

                var typeAnnotations = 0
                val maxTypeAnnotations = when {
                    instructionName in MichelsonLanguage.INSTRUCTIONS_ONE_TYPE_ANNOTATION -> 1
                    else -> 0
                }

                for (a in annotations) {
                    when {
                        maxVarAnnotations > 0 && a.isVariableAnnotation -> when {
                            varAnnotations >= maxVarAnnotations -> {
                                val msg = when (varAnnotations) {
                                    1 -> "Only one variable annotation supported"
                                    else -> "Only $maxVarAnnotations variable annotations supported"
                                }
                                holder.createErrorAnnotation(a, msg)
                            }
                            else -> varAnnotations++
                        }

                        maxFieldAnnotations > 0 && a.isFieldAnnotation -> when {
                            fieldAnnotations >= maxFieldAnnotations -> {
                                val msg = when (fieldAnnotations) {
                                    1 -> "Only one field annotation supported"
                                    else -> "Only $maxFieldAnnotations field annotations supported"
                                }
                                holder.createErrorAnnotation(a, msg)
                            }
                            else -> fieldAnnotations++
                        }

                        maxTypeAnnotations > 0 && a.isTypeAnnotation -> when {
                            typeAnnotations >= maxTypeAnnotations -> {
                                val msg = when (typeAnnotations) {
                                    1 -> "Only one type annotation supported"
                                    else -> "Only $maxTypeAnnotations type annotations supported"
                                }
                                holder.createErrorAnnotation(a, msg)
                            }
                            else -> typeAnnotations++
                        }

                        else -> holder.createErrorAnnotation(a, "Unsupported annotation")
                    }
                }
            }
        }
    }

    /**
     * Annotations illegal annotations used on a PsiType.
     */
    private fun annotateAnnotationsOfType(psi: PsiType, holder: AnnotationHolder) {
        val annotations = psi.annotations
        val annotationCount = annotations.size
        when {
            annotationCount > 0 -> {
                var typeAnnotations = 0
                var fieldAnnotations = 0
                for (a in annotations) {
                    when {
                        a.isTypeAnnotation -> when {
                            typeAnnotations >= 1 -> holder.createErrorAnnotation(a, "Only one type annotation supported")
                            else -> typeAnnotations++
                        }

                        // spec: components of 'pair' types, 'option' types and 'or' types
                        // can be annotated with a field or constructor annotation
                        a.isFieldAnnotation -> {
                            val componentType = a.findParentType()?.findComposedParentType()
                            val componentTypeName = componentType?.typeNameString

                            val supported = componentTypeName in MichelsonLanguage.TYPE_COMPONENTS_WITH_FIELD_ANNOTATIONS
                            when {
                                supported && fieldAnnotations >= 1 -> holder.createErrorAnnotation(a, "Only one field annotation supported")
                                supported -> fieldAnnotations++
                                else -> holder.createErrorAnnotation(a, "Unsupported annotation")
                            }
                        }

                        else -> holder.createErrorAnnotation(a, "Unsupported annotation")
                    }
                }
            }
        }
    }

    private fun annotateMacroAnnotations(psi: PsiMacroInstruction, holder: AnnotationHolder) {
        // fixme annotate PAIR, UNPAIR, etc.
    }

    private fun annotateLeafElement(psi: PsiElement, nodeType: IElementType, holder: AnnotationHolder) {
        when (nodeType) {
            MichelsonTypes.STRING_ESCAPE_INVALID -> holder.createErrorAnnotation(psi, "Illegal escape character ${psi.text}")
        }
    }

    private fun annotateInstruction(psi: PsiGenericInstruction, holder: AnnotationHolder) {
        val name = psi.instructionName ?: return
        val instruction = psi.instructionToken

        val blocks = psi.instructionBlocks
        val types = psi.typeList
        val datas = psi.dataList

        val oneBlockCommand = name in MichelsonLanguage.INSTRUCTIONS_ONE_BLOCK
        val twoBlocksCommand = name in MichelsonLanguage.INSTRUCTIONS_TWO_BLOCKS
        val noArgsCommand = name in MichelsonLanguage.INSTRUCTIONS_NO_ARGS
        val oneTypeCommand = MichelsonLanguage.INSTRUCTIONS_ONE_TYPE.contains(name)
        val unknownCommand = !oneBlockCommand && !twoBlocksCommand && !noArgsCommand && !oneTypeCommand && name !in MichelsonLanguage.QUESTIONABLE_INSTRUCTIONS

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
        if (!MichelsonLanguage.TYPES.contains(typeName)) {
            holder.createErrorAnnotation(element.typeToken, "Unknown type")
            return
        }
    }

    private fun annotateMacroInstruction(psi: PsiMacroInstruction, holder: AnnotationHolder) {
        val macroName = psi.instructionName ?: throw IllegalStateException("macro name not found")
        when {
            // static macros
            macroName.startsWith("ASSERT") -> annotateMacro(MichelsonLanguage.ASSERT_MACROS, psi, holder)
            macroName.startsWith("ASSERT") -> annotateMacro(MichelsonLanguage.COMPARE_MACROS, psi, holder)
            macroName.startsWith("IF") -> annotateMacro(MichelsonLanguage.IF_MACROS, psi, holder)

            // macros with dynamic name
            macroName.startsWith("DII") -> annotateMacro(MichelsonLanguage.DIIP_MACRO, psi, holder)
            macroName.startsWith("DUU") -> annotateMacro(MichelsonLanguage.DUUP_MACRO, psi, holder)
            macroName.startsWith('P') -> annotateMacro(MichelsonLanguage.PAIR_MACRO, psi, holder)
            macroName.startsWith('U') -> annotateMacro(MichelsonLanguage.UNPAIR_MACRO, psi, holder)
            macroName.startsWith("CA") || macroName.startsWith("CD") -> annotateMacro(MichelsonLanguage.CADR_MACRO, psi, holder)
            macroName.startsWith("SET_C") -> annotateMacro(MichelsonLanguage.SET_CADR_MACRO, psi, holder)
            macroName.startsWith("MAP_C") -> annotateMacro(MichelsonLanguage.MAP_CADR_MACRO, psi, holder)
        }
    }

    private fun annotateMacro(macroMetadata: MacroMetadata, psi: PsiMacroInstruction, holder: AnnotationHolder) {
        val macroToken = psi.macroToken
        val macro = macroToken.text

        val error = macroMetadata.validate(macro)
        if (error != null) {
            val all = psi.textRange
            val r = TextRange.create(all.startOffset + error.second, all.endOffset)
            holder.createErrorAnnotation(r, error.first)
            return
        }

        val blocks = psi.instructionBlocks
        val blockCount = blocks.size
        val requiredBlocks = macroMetadata.requiredBlocks()
        if (blockCount < requiredBlocks) {
            // fixme quickfix to add blocks
            if (requiredBlocks == 1) {
                holder.createErrorAnnotation(macroToken, "Expected one block.")
            } else {
                holder.createErrorAnnotation(macroToken, "Expected $requiredBlocks blocks, found $blockCount")
            }
        } else {
            var index = 0
            for (b in blocks) {
                if (index >= requiredBlocks) {
                    // fixme quickfix to remove superfluous blocks
                    holder.createErrorAnnotation(b, "Unexpected code block")
                }
                index++
            }
        }

        var varAnnotations = macroMetadata.supportedAnnotations(PsiAnnotationType.VARIABLE, macro)
        var typeAnnotations = macroMetadata.supportedAnnotations(PsiAnnotationType.TYPE, macro)
        var fieldAnnotations = macroMetadata.supportedAnnotations(PsiAnnotationType.FIELD, macro)
        for (a in psi.annotations) {
            when {
                a.isTypeAnnotation -> {
                    typeAnnotations--
                    if (typeAnnotations < 0) {
                        holder.createErrorAnnotation(a, "Unexpected type annotation")
                    }
                }
                a.isVariableAnnotation -> {
                    varAnnotations--
                    if (varAnnotations < 0) {
                        holder.createErrorAnnotation(a, "Unexpected variable annotation")
                    }
                }
                a.isFieldAnnotation -> {
                    fieldAnnotations--
                    if (fieldAnnotations < 0) {
                        holder.createErrorAnnotation(a, "Unexpected field annotation")
                    }
                }
            }
        }
    }
}
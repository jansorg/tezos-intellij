package com.tezos.lang.michelson.editor.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.tezos.lang.michelson.MichelsonTypes
import com.tezos.lang.michelson.editor.intention.MoveTrailingAnnotationsIntention
import com.tezos.lang.michelson.editor.intention.RemoveAnnotationIntention
import com.tezos.lang.michelson.editor.intention.RemoveTrailingAnnotationsIntention
import com.tezos.lang.michelson.lang.AnnotationType
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.lang.ParameterType
import com.tezos.lang.michelson.lang.macro.MacroMetadata
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
        val LEAF_TOKENS = TokenSet.create(MichelsonTypes.STRING_ESCAPE_INVALID, MichelsonTypes.TAG_TOKEN)
    }

    override fun annotate(psi: PsiElement, holder: AnnotationHolder) {
        val nodeType = psi.node.elementType
        if (LEAF_TOKENS.contains(nodeType)) {
            annotateLeafElement(psi, nodeType, holder)
            return
        }

        // generic type and instruction highlighting
        when (psi) {
            is PsiSimpleType -> annotateGenericType(psi, holder)
            is PsiGenericInstruction -> annotateInstruction(psi, holder)
            is PsiMacroInstruction -> annotateMacroInstruction(psi, holder)
        }

        // annotation highlighting
        when (psi) {
            is PsiTrailingAnnotationList -> annotateTrailingAnnotations(psi, holder)

            is PsiType -> annotateAnnotationsOfType(psi, holder)
            is PsiGenericInstruction -> annotateInstructionAnnotations(psi, holder)
            //fixme handle contract instruction
            is PsiMacroInstruction -> annotateMacroAnnotations(psi, holder)
        }
    }

    private fun annotateTrailingAnnotations(psi: PsiTrailingAnnotationList, holder: AnnotationHolder) {
        val annotation = holder.createErrorAnnotation(psi, "Unexpected annotations")
        annotation.registerFix(RemoveTrailingAnnotationsIntention())
        annotation.registerFix(MoveTrailingAnnotationsIntention())
    }

    /**
     * Instructions support different numbers of variable annotations, depending on the name.
     * The spec also states: to improve readability and robustness, instructions CAR and CDR.
     */
    private fun annotateInstructionAnnotations(psi: PsiGenericInstruction, holder: AnnotationHolder) {
        val meta = psi.instructionMetadata ?: return
        val annotations = psi.annotations
        val annotationCount = annotations.size

        when {
            // mark annotations on instructions which do not support them
            !meta.supportsAnnotations() && annotationCount > 0 -> {
                for (annotation in annotations) {
                    createAnnotationErrorWithFix(holder, annotation, "Unexpected annotation")
                }
            }

            annotationCount != 0 -> {
                val maxVarAnnotations = meta.supportedAnnotations(AnnotationType.VARIABLE)
                val maxFieldAnnotations = meta.supportedAnnotations(AnnotationType.FIELD)
                val maxTypeAnnotations = meta.supportedAnnotations(AnnotationType.TYPE)

                var varAnnotations = 0
                var fieldAnnotations = 0
                var typeAnnotations = 0

                for (a in annotations) {
                    when {
                        maxVarAnnotations > 0 && a.isVariableAnnotation -> when {
                            varAnnotations >= maxVarAnnotations -> {
                                val msg = when (varAnnotations) {
                                    1 -> "Only one variable annotation supported"
                                    else -> "Only $maxVarAnnotations variable annotations supported"
                                }
                                createAnnotationErrorWithFix(holder, a, msg)
                            }
                            else -> varAnnotations++
                        }

                        maxFieldAnnotations > 0 && a.isFieldAnnotation -> when {
                            fieldAnnotations >= maxFieldAnnotations -> {
                                val msg = when (fieldAnnotations) {
                                    1 -> "Only one field annotation supported"
                                    else -> "Only $maxFieldAnnotations field annotations supported"
                                }
                                createAnnotationErrorWithFix(holder, a, msg)
                            }
                            else -> fieldAnnotations++
                        }

                        maxTypeAnnotations > 0 && a.isTypeAnnotation -> when {
                            typeAnnotations >= maxTypeAnnotations -> {
                                val msg = when (typeAnnotations) {
                                    1 -> "Only one type annotation supported"
                                    else -> "Only $maxTypeAnnotations type annotations supported"
                                }
                                createAnnotationErrorWithFix(holder, a, msg)
                            }
                            else -> typeAnnotations++
                        }

                        else -> createAnnotationErrorWithFix(holder, a, "Unsupported annotation")
                    }
                }
            }
        }
    }

    private fun createAnnotationErrorWithFix(holder: AnnotationHolder, annotation: PsiAnnotation, message: String) {
        val error = holder.createErrorAnnotation(annotation, message)
        error.registerFix(RemoveAnnotationIntention(annotation))
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
                            // the complex type wrapping the simple type which contains the annotation
                            val complexParentType = a.findParentType()?.findParentType()
                            val componentTypeName = complexParentType?.typeNameString

                            val supported = componentTypeName in MichelsonLanguage.TYPE_COMPONENTS_WITH_FIELD_ANNOTATIONS
                            when {
                                supported && fieldAnnotations >= 1 -> holder.createErrorAnnotation(a, "Only one field annotation supported")
                                supported -> fieldAnnotations++
                                else -> createAnnotationErrorWithFix(holder, a, "Unsupported annotation")
                            }
                        }

                        else -> createAnnotationErrorWithFix(holder, a, "Unsupported annotation")
                    }
                }
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun annotateMacroAnnotations(psi: PsiMacroInstruction, holder: AnnotationHolder) {
        // fixme annotate PAIR, UNPAIR, etc.
    }

    private fun annotateLeafElement(psi: PsiElement, nodeType: IElementType, holder: AnnotationHolder) {
        val name = psi.text
        when (nodeType) {
            MichelsonTypes.STRING_ESCAPE_INVALID -> holder.createErrorAnnotation(psi, "Illegal escape character $name")
            MichelsonTypes.TAG_TOKEN -> when (name) {
                in MichelsonLanguage.TAG_BOOL.names() -> {
                    val annotation = holder.createAnnotation(HighlightSeverity.INFORMATION, psi.textRange, null)
                    annotation.textAttributes = MichelsonSyntaxHighlighter.BOOLEAN_TAG
                }
                in MichelsonLanguage.TAG_UNIT.names() -> {
                    val annotation = holder.createAnnotation(HighlightSeverity.INFORMATION, psi.textRange, null)
                    annotation.textAttributes = MichelsonSyntaxHighlighter.UNIT_TAG
                }
                in MichelsonLanguage.TAG_OPTION_NAMES -> {
                    val annotation = holder.createAnnotation(HighlightSeverity.INFORMATION, psi.textRange, null)
                    annotation.textAttributes = MichelsonSyntaxHighlighter.OPTION_TAG
                }
                in MichelsonLanguage.TAG_OR.names() -> {
                    val annotation = holder.createAnnotation(HighlightSeverity.INFORMATION, psi.textRange, null)
                    annotation.textAttributes = MichelsonSyntaxHighlighter.OR_TAG
                }
                !in MichelsonLanguage.TAG_NAMES -> holder.createErrorAnnotation(psi, "Unknown tag $name'")
            }
        }
    }

    private fun annotateInstruction(psi: PsiGenericInstruction, holder: AnnotationHolder) {
        val name = psi.instructionName ?: return

        val instruction = psi.instructionToken
        val meta = psi.instructionMetadata
        if (meta == null) {
            holder.createErrorAnnotation(instruction, "Unknown instruction")
            return
        }

        val blocks = psi.instructionBlocks
        val types = psi.typeList
        val datas = psi.dataList

        val noArgsCommand = meta.parameters.isEmpty()

        val expectedBlocks = meta.count(ParameterType.INSTRUCTION_BLOCK)
        val expectedOptionalBlocks = meta.count(ParameterType.OPTIONAL_INSTRUCTION_BLOCK)

        val expectedTypeCount = meta.count(ParameterType.TYPE) + meta.count(ParameterType.COMPARABLE_TYPE)
        val expectedDataCount = meta.count(ParameterType.DATA)

        val blockCount = blocks.size
        val typeCount = types.size
        val dataCount = datas.size
        val argsCount = blockCount + typeCount + dataCount

        when {
            expectedOptionalBlocks > 0 && blockCount > expectedOptionalBlocks -> when (expectedOptionalBlocks) {
                1 -> holder.createErrorAnnotation(instruction, "At most one optional block expected")
                else -> holder.createErrorAnnotation(instruction, "At most $expectedOptionalBlocks optional blocks expected")
            }

            // commands which expect no arguments
            noArgsCommand && argsCount != 0 -> {
                holder.createErrorAnnotation(instruction, "$name doesn't support arguments")
            }

            // commands which expect a single instruction block
            expectedBlocks > 0 && expectedBlocks != blockCount -> when {
                expectedBlocks == 1 -> holder.createErrorAnnotation(instruction, "One block expected")
                expectedBlocks == 2 -> holder.createErrorAnnotation(instruction, "Two blocks expected")
                else -> holder.createErrorAnnotation(instruction, "$expectedBlocks blocks expected")
            }

            expectedTypeCount != typeCount -> when (expectedTypeCount) {
                0 -> holder.createErrorAnnotation(instruction, "Unexpected <type> arguments")
                1 -> holder.createErrorAnnotation(instruction, "One <type> argument expected")
                else -> holder.createErrorAnnotation(instruction, "$expectedDataCount <type> arguments expected")
            }

            expectedDataCount != dataCount -> when (expectedDataCount) {
                0 -> holder.createErrorAnnotation(instruction, "Unexpected <data> arguments")
                1 -> holder.createErrorAnnotation(instruction, "One <data> argument expected")
                else -> holder.createErrorAnnotation(instruction, "$expectedDataCount <data> arguments expected")
            }

            // PUSH <type> <data>
            name == "PUSH" -> annotatePushInstruction(typeCount, dataCount, holder, instruction)

            // EMPTY_MAP <comparable type> <type>
            name == "EMPTY_MAP" -> annotateEmptyMapInstruction(typeCount, holder, instruction, types)

            // LAMBDA <type> <type> { <instruction> ... }
            name == "LAMBDA" -> annotateLambdaInstruction(typeCount, holder, instruction, blockCount)
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

    private fun annotateGenericType(element: PsiSimpleType, holder: AnnotationHolder) {
        val typeName = element.typeNameString
        if (typeName !in MichelsonLanguage.TYPE_NAMES) {
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

        var varAnnotations = macroMetadata.supportedAnnotations(AnnotationType.VARIABLE, macro)
        var typeAnnotations = macroMetadata.supportedAnnotations(AnnotationType.TYPE, macro)
        var fieldAnnotations = macroMetadata.supportedAnnotations(AnnotationType.FIELD, macro)
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
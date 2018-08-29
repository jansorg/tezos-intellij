// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.tezos.lang.michelson.psi.MichelsonCompositeElementType;
import com.tezos.lang.michelson.lexer.MichelsonElementType;
import com.tezos.lang.michelson.psi.impl.*;
import com.intellij.psi.impl.source.tree.CompositePsiElement;

public interface MichelsonTypes {

  IElementType ANNOTATION = new MichelsonCompositeElementType("ANNOTATION");
  IElementType BLOCK_INSTRUCTION = new MichelsonCompositeElementType("BLOCK_INSTRUCTION");
  IElementType CODE_SECTION = new MichelsonCompositeElementType("CODE_SECTION");
  IElementType COMPARABLE_TYPE = new MichelsonCompositeElementType("COMPARABLE_TYPE");
  IElementType COMPLEX_TYPE = new MichelsonCompositeElementType("COMPLEX_TYPE");
  IElementType CONDITIONAL_INSTRUCTION = new MichelsonCompositeElementType("CONDITIONAL_INSTRUCTION");
  IElementType CONDITIONAL_MACRO = new MichelsonCompositeElementType("CONDITIONAL_MACRO");
  IElementType CONTRACT = new MichelsonCompositeElementType("CONTRACT");
  IElementType CREATE_CONTRACT_INSTRUCTION = new MichelsonCompositeElementType("CREATE_CONTRACT_INSTRUCTION");
  IElementType DATA = new MichelsonCompositeElementType("DATA");
  IElementType GENERIC_DATA = new MichelsonCompositeElementType("GENERIC_DATA");
  IElementType GENERIC_INSTRUCTION = new MichelsonCompositeElementType("GENERIC_INSTRUCTION");
  IElementType GENERIC_MACRO = new MichelsonCompositeElementType("GENERIC_MACRO");
  IElementType GENERIC_TYPE = new MichelsonCompositeElementType("GENERIC_TYPE");
  IElementType INSTRUCTION = new MichelsonCompositeElementType("INSTRUCTION");
  IElementType LAMBDA_INSTRUCTION = new MichelsonCompositeElementType("LAMBDA_INSTRUCTION");
  IElementType LITERAL_DATA = new MichelsonCompositeElementType("LITERAL_DATA");
  IElementType MACRO_INSTRUCTION = new MichelsonCompositeElementType("MACRO_INSTRUCTION");
  IElementType MAP_ENTRY_DATA = new MichelsonCompositeElementType("MAP_ENTRY_DATA");
  IElementType NESTED_DATA = new MichelsonCompositeElementType("NESTED_DATA");
  IElementType NESTED_TYPE = new MichelsonCompositeElementType("NESTED_TYPE");
  IElementType PARAMETER_SECTION = new MichelsonCompositeElementType("PARAMETER_SECTION");
  IElementType RETURN_SECTION = new MichelsonCompositeElementType("RETURN_SECTION");
  IElementType SECTION = new MichelsonCompositeElementType("SECTION");
  IElementType STORAGE_SECTION = new MichelsonCompositeElementType("STORAGE_SECTION");
  IElementType TYPE = new MichelsonCompositeElementType("TYPE");

  IElementType ANNOTATION_TOKEN = new MichelsonElementType("ANNOTATION_TOKEN");
  IElementType BYTE = new MichelsonElementType("BYTE");
  IElementType COMMENT_LINE = new MichelsonElementType("COMMENT_LINE");
  IElementType COMMENT_MULTI_LINE = new MichelsonElementType("COMMENT_MULTI_LINE");
  IElementType FALSE = new MichelsonElementType("False");
  IElementType INSTRUCTION_TOKEN = new MichelsonElementType("INSTRUCTION_TOKEN");
  IElementType INT = new MichelsonElementType("INT");
  IElementType LEFT_CURLY = new MichelsonElementType("{");
  IElementType LEFT_PAREN = new MichelsonElementType("(");
  IElementType MACRO_TOKEN = new MichelsonElementType("MACRO_TOKEN");
  IElementType NONE = new MichelsonElementType("None");
  IElementType RIGHT_CURLY = new MichelsonElementType("}");
  IElementType RIGHT_PAREN = new MichelsonElementType(")");
  IElementType SECTION_NAME = new MichelsonElementType("SECTION_NAME");
  IElementType SEMI = new MichelsonElementType(";");
  IElementType STRING = new MichelsonElementType("STRING");
  IElementType TAG = new MichelsonElementType("TAG");
  IElementType TRUE = new MichelsonElementType("True");
  IElementType TYPE_NAME = new MichelsonElementType("TYPE_NAME");
  IElementType TYPE_NAME_COMPARABLE = new MichelsonElementType("TYPE_NAME_COMPARABLE");
  IElementType UNIT = new MichelsonElementType("Unit");

  class Factory {

    public static CompositePsiElement createElement(IElementType type) {
       if (type == ANNOTATION) {
        return new PsiAnnotationImpl(type);
      }
      else if (type == BLOCK_INSTRUCTION) {
        return new PsiBlockInstructionImpl(type);
      }
      else if (type == CODE_SECTION) {
        return new PsiCodeSectionImpl(type);
      }
      else if (type == COMPARABLE_TYPE) {
        return new PsiComparableTypeImpl(type);
      }
      else if (type == COMPLEX_TYPE) {
        return new PsiComplexTypeImpl(type);
      }
      else if (type == CONDITIONAL_INSTRUCTION) {
        return new PsiConditionalInstructionImpl(type);
      }
      else if (type == CONDITIONAL_MACRO) {
        return new PsiConditionalMacroImpl(type);
      }
      else if (type == CONTRACT) {
        return new PsiContractImpl(type);
      }
      else if (type == CREATE_CONTRACT_INSTRUCTION) {
        return new PsiCreateContractInstructionImpl(type);
      }
      else if (type == DATA) {
        return new PsiDataImpl(type);
      }
      else if (type == GENERIC_DATA) {
        return new PsiGenericDataImpl(type);
      }
      else if (type == GENERIC_INSTRUCTION) {
        return new PsiGenericInstructionImpl(type);
      }
      else if (type == GENERIC_MACRO) {
        return new PsiGenericMacroImpl(type);
      }
      else if (type == GENERIC_TYPE) {
        return new PsiGenericTypeImpl(type);
      }
      else if (type == LAMBDA_INSTRUCTION) {
        return new PsiLambdaInstructionImpl(type);
      }
      else if (type == LITERAL_DATA) {
        return new PsiLiteralDataImpl(type);
      }
      else if (type == MAP_ENTRY_DATA) {
        return new PsiMapEntryDataImpl(type);
      }
      else if (type == NESTED_DATA) {
        return new PsiNestedDataImpl(type);
      }
      else if (type == NESTED_TYPE) {
        return new PsiNestedTypeImpl(type);
      }
      else if (type == PARAMETER_SECTION) {
        return new PsiParameterSectionImpl(type);
      }
      else if (type == RETURN_SECTION) {
        return new PsiReturnSectionImpl(type);
      }
      else if (type == STORAGE_SECTION) {
        return new PsiStorageSectionImpl(type);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

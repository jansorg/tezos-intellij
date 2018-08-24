// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.tezos.lang.michelson.psi.MichelsonCompositeElementType;
import com.tezos.lang.michelson.lexer.MichelsonElementType;
import com.tezos.lang.michelson.parser.impl.*;
import com.intellij.psi.impl.source.tree.CompositePsiElement;

public interface MichelsonTypes {

  IElementType ANNOTATION = new MichelsonCompositeElementType("ANNOTATION");
  IElementType BLOCK_INSTRUCTION = new MichelsonCompositeElementType("BLOCK_INSTRUCTION");
  IElementType CODE_SECTION = new MichelsonCompositeElementType("CODE_SECTION");
  IElementType COMPARABLE_TYPE = new MichelsonCompositeElementType("COMPARABLE_TYPE");
  IElementType CONDITIONAL_INSTRUCTION = new MichelsonCompositeElementType("CONDITIONAL_INSTRUCTION");
  IElementType CONDITIONAL_MACRO = new MichelsonCompositeElementType("CONDITIONAL_MACRO");
  IElementType CONTRACT = new MichelsonCompositeElementType("CONTRACT");
  IElementType CREATE_CONTRACT_INSTRUCTION = new MichelsonCompositeElementType("CREATE_CONTRACT_INSTRUCTION");
  IElementType DATA = new MichelsonCompositeElementType("DATA");
  IElementType GENERIC_INSTRUCTION = new MichelsonCompositeElementType("GENERIC_INSTRUCTION");
  IElementType INSTRUCTION = new MichelsonCompositeElementType("INSTRUCTION");
  IElementType LAMBDA_INSTRUCTION = new MichelsonCompositeElementType("LAMBDA_INSTRUCTION");
  IElementType MACRO_INSTRUCTION = new MichelsonCompositeElementType("MACRO_INSTRUCTION");
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
  IElementType RIGHT_CURLY = new MichelsonElementType("}");
  IElementType RIGHT_PAREN = new MichelsonElementType(")");
  IElementType SECTION_NAME = new MichelsonElementType("SECTION_NAME");
  IElementType SEMI = new MichelsonElementType(";");
  IElementType STRING = new MichelsonElementType("STRING");
  IElementType TAG = new MichelsonElementType("TAG");
  IElementType TRUE = new MichelsonElementType("True");
  IElementType TYPE_NAME = new MichelsonElementType("TYPE_NAME");
  IElementType TYPE_NAME_COMPARABLE = new MichelsonElementType("TYPE_NAME_COMPARABLE");

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
      else if (type == GENERIC_INSTRUCTION) {
        return new PsiGenericInstructionImpl(type);
      }
      else if (type == LAMBDA_INSTRUCTION) {
        return new PsiLambdaInstructionImpl(type);
      }
      else if (type == MACRO_INSTRUCTION) {
        return new PsiMacroInstructionImpl(type);
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
      else if (type == TYPE) {
        return new PsiTypeImpl(type);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

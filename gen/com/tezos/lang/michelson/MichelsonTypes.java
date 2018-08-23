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

  IElementType ARG_INSTRUCTION = new MichelsonCompositeElementType("ARG_INSTRUCTION");
  IElementType ASSERT_MACRO = new MichelsonCompositeElementType("ASSERT_MACRO");
  IElementType BLOCK_INSTRUCTION = new MichelsonCompositeElementType("BLOCK_INSTRUCTION");
  IElementType CMP_MACRO = new MichelsonCompositeElementType("CMP_MACRO");
  IElementType CODE_SECTION = new MichelsonCompositeElementType("CODE_SECTION");
  IElementType COMPARABLE_TYPE = new MichelsonCompositeElementType("COMPARABLE_TYPE");
  IElementType CONDITIONAL_MACRO = new MichelsonCompositeElementType("CONDITIONAL_MACRO");
  IElementType DATA = new MichelsonCompositeElementType("DATA");
  IElementType FAIL_MACRO = new MichelsonCompositeElementType("FAIL_MACRO");
  IElementType IF_INSTRUCTION = new MichelsonCompositeElementType("IF_INSTRUCTION");
  IElementType INSTRUCTION = new MichelsonCompositeElementType("INSTRUCTION");
  IElementType MACRO_INSTRUCTION = new MichelsonCompositeElementType("MACRO_INSTRUCTION");
  IElementType MAP_ACCESS_MACRO = new MichelsonCompositeElementType("MAP_ACCESS_MACRO");
  IElementType MAP_MACRO = new MichelsonCompositeElementType("MAP_MACRO");
  IElementType PAIR_ACCESS_MACRO = new MichelsonCompositeElementType("PAIR_ACCESS_MACRO");
  IElementType PARAMETER_SECTION = new MichelsonCompositeElementType("PARAMETER_SECTION");
  IElementType RETURN_SECTION = new MichelsonCompositeElementType("RETURN_SECTION");
  IElementType SECTION = new MichelsonCompositeElementType("SECTION");
  IElementType SET_ACCESS_MACRO = new MichelsonCompositeElementType("SET_ACCESS_MACRO");
  IElementType SET_MACRO = new MichelsonCompositeElementType("SET_MACRO");
  IElementType SIMPLE_INSTRUCTION = new MichelsonCompositeElementType("SIMPLE_INSTRUCTION");
  IElementType STORAGE_SECTION = new MichelsonCompositeElementType("STORAGE_SECTION");
  IElementType TYPE = new MichelsonCompositeElementType("TYPE");
  IElementType UNKNOWN_INSTRUCTION = new MichelsonCompositeElementType("UNKNOWN_INSTRUCTION");

  IElementType BYTE = new MichelsonElementType("BYTE");
  IElementType COMMENT_LINE = new MichelsonElementType("COMMENT_LINE");
  IElementType COMMENT_MULTI_LINE = new MichelsonElementType("COMMENT_MULTI_LINE");
  IElementType FALSE = new MichelsonElementType("False");
  IElementType INSTRUCTION_TOKEN = new MichelsonElementType("INSTRUCTION_TOKEN");
  IElementType INT = new MichelsonElementType("INT");
  IElementType LEFT_CURLY = new MichelsonElementType("{");
  IElementType LEFT_PAREN = new MichelsonElementType("(");
  IElementType MACRO_DIIP_TOKEN = new MichelsonElementType("MACRO_DIIP_TOKEN");
  IElementType MACRO_DUUP_TOKEN = new MichelsonElementType("MACRO_DUUP_TOKEN");
  IElementType MACRO_MAP_CADR_TOKEN = new MichelsonElementType("MACRO_MAP_CADR_TOKEN");
  IElementType MACRO_NESTED_TOKEN = new MichelsonElementType("MACRO_NESTED_TOKEN");
  IElementType MACRO_PAIRS_TOKEN = new MichelsonElementType("MACRO_PAIRS_TOKEN");
  IElementType MACRO_PAIR_ACCESS_TOKEN = new MichelsonElementType("MACRO_PAIR_ACCESS_TOKEN");
  IElementType MACRO_SET_CADR_TOKEN = new MichelsonElementType("MACRO_SET_CADR_TOKEN");
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
       if (type == ARG_INSTRUCTION) {
        return new PsiArgInstructionImpl(type);
      }
      else if (type == ASSERT_MACRO) {
        return new PsiAssertMacroImpl(type);
      }
      else if (type == BLOCK_INSTRUCTION) {
        return new PsiBlockInstructionImpl(type);
      }
      else if (type == CMP_MACRO) {
        return new PsiCmpMacroImpl(type);
      }
      else if (type == CODE_SECTION) {
        return new PsiCodeSectionImpl(type);
      }
      else if (type == COMPARABLE_TYPE) {
        return new PsiComparableTypeImpl(type);
      }
      else if (type == CONDITIONAL_MACRO) {
        return new PsiConditionalMacroImpl(type);
      }
      else if (type == DATA) {
        return new PsiDataImpl(type);
      }
      else if (type == FAIL_MACRO) {
        return new PsiFailMacroImpl(type);
      }
      else if (type == IF_INSTRUCTION) {
        return new PsiIfInstructionImpl(type);
      }
      else if (type == MAP_ACCESS_MACRO) {
        return new PsiMapAccessMacroImpl(type);
      }
      else if (type == MAP_MACRO) {
        return new PsiMapMacroImpl(type);
      }
      else if (type == PAIR_ACCESS_MACRO) {
        return new PsiPairAccessMacroImpl(type);
      }
      else if (type == PARAMETER_SECTION) {
        return new PsiParameterSectionImpl(type);
      }
      else if (type == RETURN_SECTION) {
        return new PsiReturnSectionImpl(type);
      }
      else if (type == SET_ACCESS_MACRO) {
        return new PsiSetAccessMacroImpl(type);
      }
      else if (type == SET_MACRO) {
        return new PsiSetMacroImpl(type);
      }
      else if (type == SIMPLE_INSTRUCTION) {
        return new PsiSimpleInstructionImpl(type);
      }
      else if (type == STORAGE_SECTION) {
        return new PsiStorageSectionImpl(type);
      }
      else if (type == TYPE) {
        return new PsiTypeImpl(type);
      }
      else if (type == UNKNOWN_INSTRUCTION) {
        return new PsiUnknownInstructionImpl(type);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

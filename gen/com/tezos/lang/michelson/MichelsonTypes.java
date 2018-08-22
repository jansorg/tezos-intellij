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

  IElementType CODE_SECTION = new MichelsonCompositeElementType("CODE_SECTION");
  IElementType COMPARABLE_TYPE = new MichelsonCompositeElementType("COMPARABLE_TYPE");
  IElementType DATA = new MichelsonCompositeElementType("DATA");
  IElementType INSTRUCTION = new MichelsonCompositeElementType("INSTRUCTION");
  IElementType INSTRUCTIONS = new MichelsonCompositeElementType("INSTRUCTIONS");
  IElementType PARAMETER_SECTION = new MichelsonCompositeElementType("PARAMETER_SECTION");
  IElementType RETURN_SECTION = new MichelsonCompositeElementType("RETURN_SECTION");
  IElementType SECTION = new MichelsonCompositeElementType("SECTION");
  IElementType STORAGE_SECTION = new MichelsonCompositeElementType("STORAGE_SECTION");
  IElementType TYPE = new MichelsonCompositeElementType("TYPE");

  IElementType BYTE = new MichelsonElementType("BYTE");
  IElementType COMMENT_LINE = new MichelsonElementType("COMMENT_LINE");
  IElementType COMMENT_MULTI_LINE = new MichelsonElementType("COMMENT_MULTI_LINE");
  IElementType INSTRUCTION_TOKEN = new MichelsonElementType("INSTRUCTION_TOKEN");
  IElementType INT = new MichelsonElementType("INT");
  IElementType LEFT_CURLY = new MichelsonElementType("{");
  IElementType LEFT_PAREN = new MichelsonElementType("(");
  IElementType NAME = new MichelsonElementType("NAME");
  IElementType RIGHT_CURLY = new MichelsonElementType("}");
  IElementType RIGHT_PAREN = new MichelsonElementType(")");
  IElementType SEMI = new MichelsonElementType(";");
  IElementType STRING = new MichelsonElementType("STRING");
  IElementType TAG = new MichelsonElementType("TAG");

  class Factory {

    public static CompositePsiElement createElement(IElementType type) {
       if (type == CODE_SECTION) {
        return new PsiCodeSectionImpl(type);
      }
      else if (type == COMPARABLE_TYPE) {
        return new PsiComparableTypeImpl(type);
      }
      else if (type == DATA) {
        return new PsiDataImpl(type);
      }
      else if (type == INSTRUCTION) {
        return new PsiInstructionImpl(type);
      }
      else if (type == INSTRUCTIONS) {
        return new PsiInstructionsImpl(type);
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

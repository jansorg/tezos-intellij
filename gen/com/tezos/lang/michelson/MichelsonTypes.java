// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.tezos.lang.michelson.parser.impl.*;

public interface MichelsonTypes {

  IElementType CODE_SECTION = new IElementType("CODE_SECTION", null);
  IElementType COMPARABLE_TYPE = new IElementType("COMPARABLE_TYPE", null);
  IElementType DATA = new IElementType("DATA", null);
  IElementType DATA_TOPLEVEL = new IElementType("DATA_TOPLEVEL", null);
  IElementType INSTRUCTION = new IElementType("INSTRUCTION", null);
  IElementType INSTRUCTIONS = new IElementType("INSTRUCTIONS", null);
  IElementType PARAMETER_SECTION = new IElementType("PARAMETER_SECTION", null);
  IElementType RETURN_SECTION = new IElementType("RETURN_SECTION", null);
  IElementType SECTION = new IElementType("SECTION", null);
  IElementType STORAGE_SECTION = new IElementType("STORAGE_SECTION", null);
  IElementType TYPE = new IElementType("TYPE", null);
  IElementType TYPE_TOPLEVEL = new IElementType("TYPE_TOPLEVEL", null);

  IElementType BYTE = new IElementType("BYTE", null);
  IElementType COMMENT_LINE = new IElementType("COMMENT_LINE", null);
  IElementType COMMENT_MULTI_LINE = new IElementType("COMMENT_MULTI_LINE", null);
  IElementType INSTRUCTION_TOKEN = new IElementType("INSTRUCTION_TOKEN", null);
  IElementType INT = new IElementType("INT", null);
  IElementType LEFT_CURLY = new IElementType("{", null);
  IElementType LEFT_PAREN = new IElementType("(", null);
  IElementType NAME = new IElementType("NAME", null);
  IElementType RIGHT_CURLY = new IElementType("}", null);
  IElementType RIGHT_PAREN = new IElementType(")", null);
  IElementType SEMI = new IElementType(";", null);
  IElementType STRING = new IElementType("STRING", null);
  IElementType TAG = new IElementType("TAG", null);

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == CODE_SECTION) {
        return new CodeSectionImpl(node);
      }
      else if (type == COMPARABLE_TYPE) {
        return new ComparableTypeImpl(node);
      }
      else if (type == DATA) {
        return new DataImpl(node);
      }
      else if (type == DATA_TOPLEVEL) {
        return new DataToplevelImpl(node);
      }
      else if (type == INSTRUCTION) {
        return new InstructionImpl(node);
      }
      else if (type == INSTRUCTIONS) {
        return new InstructionsImpl(node);
      }
      else if (type == PARAMETER_SECTION) {
        return new ParameterSectionImpl(node);
      }
      else if (type == RETURN_SECTION) {
        return new ReturnSectionImpl(node);
      }
      else if (type == SECTION) {
        return new SectionImpl(node);
      }
      else if (type == STORAGE_SECTION) {
        return new StorageSectionImpl(node);
      }
      else if (type == TYPE) {
        return new TypeImpl(node);
      }
      else if (type == TYPE_TOPLEVEL) {
        return new TypeToplevelImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

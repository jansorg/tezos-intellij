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
  IElementType ANNOTATION_LIST = new MichelsonCompositeElementType("ANNOTATION_LIST");
  IElementType BLOCK_INSTRUCTION = new MichelsonCompositeElementType("BLOCK_INSTRUCTION");
  IElementType COMPLEX_TYPE = new MichelsonCompositeElementType("COMPLEX_TYPE");
  IElementType CONTRACT = new MichelsonCompositeElementType("CONTRACT");
  IElementType CONTRACT_WRAPPER = new MichelsonCompositeElementType("CONTRACT_WRAPPER");
  IElementType CREATE_CONTRACT_INSTRUCTION = new MichelsonCompositeElementType("CREATE_CONTRACT_INSTRUCTION");
  IElementType DATA_LIST = new MichelsonCompositeElementType("DATA_LIST");
  IElementType DATA_MAP = new MichelsonCompositeElementType("DATA_MAP");
  IElementType EMPTY_BLOCK = new MichelsonCompositeElementType("EMPTY_BLOCK");
  IElementType FIELD_ANNOTATION = new MichelsonCompositeElementType("FIELD_ANNOTATION");
  IElementType GENERIC_INSTRUCTION = new MichelsonCompositeElementType("GENERIC_INSTRUCTION");
  IElementType INSTRUCTION = new MichelsonCompositeElementType("INSTRUCTION");
  IElementType LITERAL_DATA = new MichelsonCompositeElementType("LITERAL_DATA");
  IElementType MACRO_INSTRUCTION = new MichelsonCompositeElementType("MACRO_INSTRUCTION");
  IElementType MAP_ENTRY = new MichelsonCompositeElementType("MAP_ENTRY");
  IElementType SECTION = new MichelsonCompositeElementType("SECTION");
  IElementType SIMPLE_TYPE = new MichelsonCompositeElementType("SIMPLE_TYPE");
  IElementType STRING_LITERAL = new MichelsonCompositeElementType("STRING_LITERAL");
  IElementType TAG = new MichelsonCompositeElementType("TAG");
  IElementType TYPE = new MichelsonCompositeElementType("TYPE");
  IElementType TYPE_ANNOTATION = new MichelsonCompositeElementType("TYPE_ANNOTATION");
  IElementType VARIABLE_ANNOTATION = new MichelsonCompositeElementType("VARIABLE_ANNOTATION");

  IElementType BYTE = new MichelsonElementType("BYTE");
  IElementType COMMENT_LINE = new MichelsonElementType("COMMENT_LINE");
  IElementType COMMENT_MULTI_LINE = new MichelsonElementType("COMMENT_MULTI_LINE");
  IElementType EOL = new MichelsonElementType("end-of-line-marker");
  IElementType FIELD_ANNOTATION_TOKEN = new MichelsonElementType("FIELD_ANNOTATION_TOKEN");
  IElementType INSTRUCTION_TOKEN = new MichelsonElementType("INSTRUCTION_TOKEN");
  IElementType INT = new MichelsonElementType("INT");
  IElementType LEFT_CURLY = new MichelsonElementType("{");
  IElementType LEFT_PAREN = new MichelsonElementType("(");
  IElementType MACRO_TOKEN = new MichelsonElementType("MACRO_TOKEN");
  IElementType QUOTE = new MichelsonElementType("\"");
  IElementType RIGHT_CURLY = new MichelsonElementType("}");
  IElementType RIGHT_PAREN = new MichelsonElementType(")");
  IElementType SECTION_NAME = new MichelsonElementType("SECTION_NAME");
  IElementType SEMI = new MichelsonElementType(";");
  IElementType STRING_CONTENT = new MichelsonElementType("STRING_CONTENT");
  IElementType STRING_ESCAPE = new MichelsonElementType("STRING_ESCAPE");
  IElementType STRING_ESCAPE_INVALID = new MichelsonElementType("STRING_ESCAPE_INVALID");
  IElementType TAG_TOKEN = new MichelsonElementType("TAG_TOKEN");
  IElementType TYPE_ANNOTATION_TOKEN = new MichelsonElementType("TYPE_ANNOTATION_TOKEN");
  IElementType TYPE_NAME = new MichelsonElementType("TYPE_NAME");
  IElementType TYPE_NAME_COMPARABLE = new MichelsonElementType("TYPE_NAME_COMPARABLE");
  IElementType VAR_ANNOTATION_TOKEN = new MichelsonElementType("VAR_ANNOTATION_TOKEN");

  class Factory {

    public static CompositePsiElement createElement(IElementType type) {
       if (type == ANNOTATION_LIST) {
        return new PsiAnnotationListImpl(type);
      }
      else if (type == BLOCK_INSTRUCTION) {
        return new PsiBlockInstructionImpl(type);
      }
      else if (type == COMPLEX_TYPE) {
        return new PsiComplexTypeImpl(type);
      }
      else if (type == CONTRACT) {
        return new PsiContractImpl(type);
      }
      else if (type == CONTRACT_WRAPPER) {
        return new PsiContractWrapperImpl(type);
      }
      else if (type == CREATE_CONTRACT_INSTRUCTION) {
        return new PsiCreateContractInstructionImpl(type);
      }
      else if (type == DATA_LIST) {
        return new PsiDataListImpl(type);
      }
      else if (type == DATA_MAP) {
        return new PsiDataMapImpl(type);
      }
      else if (type == EMPTY_BLOCK) {
        return new PsiEmptyBlockImpl(type);
      }
      else if (type == FIELD_ANNOTATION) {
        return new PsiFieldAnnotationImpl(type);
      }
      else if (type == GENERIC_INSTRUCTION) {
        return new PsiGenericInstructionImpl(type);
      }
      else if (type == LITERAL_DATA) {
        return new PsiLiteralDataImpl(type);
      }
      else if (type == MACRO_INSTRUCTION) {
        return new PsiMacroInstructionImpl(type);
      }
      else if (type == MAP_ENTRY) {
        return new PsiMapEntryImpl(type);
      }
      else if (type == SECTION) {
        return new PsiSectionImpl(type);
      }
      else if (type == SIMPLE_TYPE) {
        return new PsiSimpleTypeImpl(type);
      }
      else if (type == STRING_LITERAL) {
        return new PsiStringLiteralImpl(type);
      }
      else if (type == TAG) {
        return new PsiTagImpl(type);
      }
      else if (type == TYPE_ANNOTATION) {
        return new PsiTypeAnnotationImpl(type);
      }
      else if (type == VARIABLE_ANNOTATION) {
        return new PsiVariableAnnotationImpl(type);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

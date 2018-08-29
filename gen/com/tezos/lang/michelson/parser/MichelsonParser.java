// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.tezos.lang.michelson.MichelsonTypes.*;
import static com.tezos.lang.michelson.parser.MichelsonParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class MichelsonParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType type, PsiBuilder builder) {
    parseLight(type, builder);
    return builder.getTreeBuilt();
  }

  public void parseLight(IElementType type, PsiBuilder builder) {
    boolean result;
    builder = adapt_builder_(type, builder, this, EXTENDS_SETS_);
    Marker marker = enter_section_(builder, 0, _COLLAPSE_, null);
    if (type == ANNOTATION) {
      result = annotation(builder, 0);
    }
    else if (type == BLOCK_INSTRUCTION) {
      result = block_instruction(builder, 0);
    }
    else if (type == CODE_SECTION) {
      result = code_section(builder, 0);
    }
    else if (type == COMPARABLE_TYPE) {
      result = comparable_type(builder, 0);
    }
    else if (type == COMPLEX_TYPE) {
      result = complex_type(builder, 0);
    }
    else if (type == CONDITIONAL_INSTRUCTION) {
      result = conditional_instruction(builder, 0);
    }
    else if (type == CONDITIONAL_MACRO) {
      result = conditional_macro(builder, 0);
    }
    else if (type == CONTRACT) {
      result = contract(builder, 0);
    }
    else if (type == CREATE_CONTRACT_INSTRUCTION) {
      result = create_contract_instruction(builder, 0);
    }
    else if (type == DATA) {
      result = data(builder, 0);
    }
    else if (type == DATA_NESTED) {
      result = data_nested(builder, 0);
    }
    else if (type == GENERIC_INSTRUCTION) {
      result = generic_instruction(builder, 0);
    }
    else if (type == GENERIC_MACRO) {
      result = generic_macro(builder, 0);
    }
    else if (type == GENERIC_TYPE) {
      result = generic_type(builder, 0);
    }
    else if (type == INSTRUCTION) {
      result = instruction(builder, 0);
    }
    else if (type == LAMBDA_INSTRUCTION) {
      result = lambda_instruction(builder, 0);
    }
    else if (type == MACRO_INSTRUCTION) {
      result = macro_instruction(builder, 0);
    }
    else if (type == NESTED_TYPE) {
      result = nested_type(builder, 0);
    }
    else if (type == PARAMETER_SECTION) {
      result = parameter_section(builder, 0);
    }
    else if (type == RETURN_SECTION) {
      result = return_section(builder, 0);
    }
    else if (type == SECTION) {
      result = section(builder, 0);
    }
    else if (type == STORAGE_SECTION) {
      result = storage_section(builder, 0);
    }
    else if (type == TYPE) {
      result = type(builder, 0);
    }
    else {
      result = parse_root_(type, builder, 0);
    }
    exit_section_(builder, 0, marker, type, result, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType type, PsiBuilder builder, int level) {
    return script_file(builder, level + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(CODE_SECTION, PARAMETER_SECTION, RETURN_SECTION, SECTION,
      STORAGE_SECTION),
    create_token_set_(COMPARABLE_TYPE, COMPLEX_TYPE, GENERIC_TYPE, NESTED_TYPE,
      TYPE),
    create_token_set_(BLOCK_INSTRUCTION, CONDITIONAL_INSTRUCTION, CONDITIONAL_MACRO, CREATE_CONTRACT_INSTRUCTION,
      GENERIC_INSTRUCTION, GENERIC_MACRO, INSTRUCTION, LAMBDA_INSTRUCTION,
      MACRO_INSTRUCTION),
  };

  /* ********************************************************** */
  // ANNOTATION_TOKEN
  public static boolean annotation(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "annotation")) return false;
    if (!nextTokenIs(builder, "<annotation>", ANNOTATION_TOKEN)) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, ANNOTATION, "<annotation>");
    result = consumeToken(builder, ANNOTATION_TOKEN);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // '{' (instruction (';' instruction)*)? ';'? '}'
  public static boolean block_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "block_instruction")) return false;
    if (!nextTokenIs(builder, LEFT_CURLY)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_CURLY);
    result = result && block_instruction_1(builder, level + 1);
    result = result && block_instruction_2(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_CURLY);
    exit_section_(builder, marker, BLOCK_INSTRUCTION, result);
    return result;
  }

  // (instruction (';' instruction)*)?
  private static boolean block_instruction_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "block_instruction_1")) return false;
    block_instruction_1_0(builder, level + 1);
    return true;
  }

  // instruction (';' instruction)*
  private static boolean block_instruction_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "block_instruction_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = instruction(builder, level + 1);
    result = result && block_instruction_1_0_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (';' instruction)*
  private static boolean block_instruction_1_0_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "block_instruction_1_0_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!block_instruction_1_0_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "block_instruction_1_0_1", pos)) break;
    }
    return true;
  }

  // ';' instruction
  private static boolean block_instruction_1_0_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "block_instruction_1_0_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, SEMI);
    result = result && instruction(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // ';'?
  private static boolean block_instruction_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "block_instruction_2")) return false;
    consumeToken(builder, SEMI);
    return true;
  }

  /* ********************************************************** */
  // 'code' instruction ';'?
  public static boolean code_section(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "code_section")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, CODE_SECTION, "<code section>");
    result = consumeToken(builder, "code");
    result = result && instruction(builder, level + 1);
    result = result && code_section_2(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // ';'?
  private static boolean code_section_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "code_section_2")) return false;
    consumeToken(builder, SEMI);
    return true;
  }

  /* ********************************************************** */
  // TYPE_NAME_COMPARABLE annotation*
  public static boolean comparable_type(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "comparable_type")) return false;
    if (!nextTokenIs(builder, "<comparable type>", TYPE_NAME_COMPARABLE)) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, COMPARABLE_TYPE, "<comparable type>");
    result = consumeToken(builder, TYPE_NAME_COMPARABLE);
    result = result && comparable_type_1(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // annotation*
  private static boolean comparable_type_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "comparable_type_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!annotation(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "comparable_type_1", pos)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ('option' | 'list' | 'set' | 'contract' | 'pair' | 'or' | 'lambda' | 'map' | 'big_map') annotation* toplevel_type+
  public static boolean complex_type(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "complex_type")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, COMPLEX_TYPE, "<complex type>");
    result = complex_type_0(builder, level + 1);
    result = result && complex_type_1(builder, level + 1);
    result = result && complex_type_2(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // 'option' | 'list' | 'set' | 'contract' | 'pair' | 'or' | 'lambda' | 'map' | 'big_map'
  private static boolean complex_type_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "complex_type_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "option");
    if (!result) result = consumeToken(builder, "list");
    if (!result) result = consumeToken(builder, "set");
    if (!result) result = consumeToken(builder, "contract");
    if (!result) result = consumeToken(builder, "pair");
    if (!result) result = consumeToken(builder, "or");
    if (!result) result = consumeToken(builder, "lambda");
    if (!result) result = consumeToken(builder, "map");
    if (!result) result = consumeToken(builder, "big_map");
    exit_section_(builder, marker, null, result);
    return result;
  }

  // annotation*
  private static boolean complex_type_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "complex_type_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!annotation(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "complex_type_1", pos)) break;
    }
    return true;
  }

  // toplevel_type+
  private static boolean complex_type_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "complex_type_2")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = toplevel_type(builder, level + 1);
    while (result) {
      int pos = current_position_(builder);
      if (!toplevel_type(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "complex_type_2", pos)) break;
    }
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // ('IF' | 'IF_CONS' | 'IF_LEFT' | 'IF_NONE') block_instruction block_instruction
  public static boolean conditional_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "conditional_instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, CONDITIONAL_INSTRUCTION, "<conditional instruction>");
    result = conditional_instruction_0(builder, level + 1);
    result = result && block_instruction(builder, level + 1);
    result = result && block_instruction(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // 'IF' | 'IF_CONS' | 'IF_LEFT' | 'IF_NONE'
  private static boolean conditional_instruction_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "conditional_instruction_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "IF");
    if (!result) result = consumeToken(builder, "IF_CONS");
    if (!result) result = consumeToken(builder, "IF_LEFT");
    if (!result) result = consumeToken(builder, "IF_NONE");
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // ("IF_SOME"
  //               | "IFEQ" | "IFNEQ" | "IFLT" | "IFGT" | "IFLE" | "IFGE"
  //               | "IFCMPEQ" | "IFCMPNEQ" | "IFCMPLT" | "IFCMPGT" | "IFCMPLE" | "IFCMPGE") block_instruction block_instruction
  public static boolean conditional_macro(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "conditional_macro")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, CONDITIONAL_MACRO, "<conditional macro>");
    result = conditional_macro_0(builder, level + 1);
    result = result && block_instruction(builder, level + 1);
    result = result && block_instruction(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // "IF_SOME"
  //               | "IFEQ" | "IFNEQ" | "IFLT" | "IFGT" | "IFLE" | "IFGE"
  //               | "IFCMPEQ" | "IFCMPNEQ" | "IFCMPLT" | "IFCMPGT" | "IFCMPLE" | "IFCMPGE"
  private static boolean conditional_macro_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "conditional_macro_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "IF_SOME");
    if (!result) result = consumeToken(builder, "IFEQ");
    if (!result) result = consumeToken(builder, "IFNEQ");
    if (!result) result = consumeToken(builder, "IFLT");
    if (!result) result = consumeToken(builder, "IFGT");
    if (!result) result = consumeToken(builder, "IFLE");
    if (!result) result = consumeToken(builder, "IFGE");
    if (!result) result = consumeToken(builder, "IFCMPEQ");
    if (!result) result = consumeToken(builder, "IFCMPNEQ");
    if (!result) result = consumeToken(builder, "IFCMPLT");
    if (!result) result = consumeToken(builder, "IFCMPGT");
    if (!result) result = consumeToken(builder, "IFCMPLE");
    if (!result) result = consumeToken(builder, "IFCMPGE");
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // section+
  public static boolean contract(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "contract")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, CONTRACT, "<contract>");
    result = section(builder, level + 1);
    while (result) {
      int pos = current_position_(builder);
      if (!section(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "contract", pos)) break;
    }
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // 'CREATE_CONTRACT' '{' contract '}' {}
  public static boolean create_contract_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "create_contract_instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, CREATE_CONTRACT_INSTRUCTION, "<create contract instruction>");
    result = consumeToken(builder, "CREATE_CONTRACT");
    result = result && consumeToken(builder, LEFT_CURLY);
    result = result && contract(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_CURLY);
    result = result && create_contract_instruction_4(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // {}
  private static boolean create_contract_instruction_4(PsiBuilder builder, int level) {
    return true;
  }

  /* ********************************************************** */
  // 'None'
  //   | data_toplevel
  //   | data_pair
  //   | data_left
  //   | data_right
  //   | data_some
  //   | data_list_literal
  //   | data_map_literal
  //   | instruction
  public static boolean data(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, DATA, "<data>");
    result = consumeToken(builder, "None");
    if (!result) result = data_toplevel(builder, level + 1);
    if (!result) result = data_pair(builder, level + 1);
    if (!result) result = data_left(builder, level + 1);
    if (!result) result = data_right(builder, level + 1);
    if (!result) result = data_some(builder, level + 1);
    if (!result) result = data_list_literal(builder, level + 1);
    if (!result) result = data_map_literal(builder, level + 1);
    if (!result) result = instruction(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // 'Left' data_toplevel {}
  static boolean data_left(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_left")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Left");
    result = result && data_toplevel(builder, level + 1);
    result = result && data_left_2(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // {}
  private static boolean data_left_2(PsiBuilder builder, int level) {
    return true;
  }

  /* ********************************************************** */
  // '{' (data_toplevel (';' data_toplevel)*)? '}' {}
  static boolean data_list_literal(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_literal")) return false;
    if (!nextTokenIs(builder, LEFT_CURLY)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_CURLY);
    result = result && data_list_literal_1(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_CURLY);
    result = result && data_list_literal_3(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (data_toplevel (';' data_toplevel)*)?
  private static boolean data_list_literal_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_literal_1")) return false;
    data_list_literal_1_0(builder, level + 1);
    return true;
  }

  // data_toplevel (';' data_toplevel)*
  private static boolean data_list_literal_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_literal_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = data_toplevel(builder, level + 1);
    result = result && data_list_literal_1_0_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (';' data_toplevel)*
  private static boolean data_list_literal_1_0_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_literal_1_0_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!data_list_literal_1_0_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "data_list_literal_1_0_1", pos)) break;
    }
    return true;
  }

  // ';' data_toplevel
  private static boolean data_list_literal_1_0_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_literal_1_0_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, SEMI);
    result = result && data_toplevel(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // {}
  private static boolean data_list_literal_3(PsiBuilder builder, int level) {
    return true;
  }

  /* ********************************************************** */
  // 'Elt' data_toplevel data_toplevel {}
  static boolean data_map_entry(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_map_entry")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Elt");
    result = result && data_toplevel(builder, level + 1);
    result = result && data_toplevel(builder, level + 1);
    result = result && data_map_entry_3(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // {}
  private static boolean data_map_entry_3(PsiBuilder builder, int level) {
    return true;
  }

  /* ********************************************************** */
  // '{' data_map_entry (';' data_map_entry)* '}'
  static boolean data_map_literal(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_map_literal")) return false;
    if (!nextTokenIs(builder, LEFT_CURLY)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_CURLY);
    result = result && data_map_entry(builder, level + 1);
    result = result && data_map_literal_2(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_CURLY);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (';' data_map_entry)*
  private static boolean data_map_literal_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_map_literal_2")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!data_map_literal_2_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "data_map_literal_2", pos)) break;
    }
    return true;
  }

  // ';' data_map_entry
  private static boolean data_map_literal_2_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_map_literal_2_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, SEMI);
    result = result && data_map_entry(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // '(' data ')' {}
  public static boolean data_nested(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_nested")) return false;
    if (!nextTokenIs(builder, LEFT_PAREN)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_PAREN);
    result = result && data(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_PAREN);
    result = result && data_nested_3(builder, level + 1);
    exit_section_(builder, marker, DATA_NESTED, result);
    return result;
  }

  // {}
  private static boolean data_nested_3(PsiBuilder builder, int level) {
    return true;
  }

  /* ********************************************************** */
  // 'Pair' data_toplevel data_toplevel {}
  static boolean data_pair(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_pair")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Pair");
    result = result && data_toplevel(builder, level + 1);
    result = result && data_toplevel(builder, level + 1);
    result = result && data_pair_3(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // {}
  private static boolean data_pair_3(PsiBuilder builder, int level) {
    return true;
  }

  /* ********************************************************** */
  // 'Right' data_toplevel {}
  static boolean data_right(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_right")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Right");
    result = result && data_toplevel(builder, level + 1);
    result = result && data_right_2(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // {}
  private static boolean data_right_2(PsiBuilder builder, int level) {
    return true;
  }

  /* ********************************************************** */
  // 'Some' data_toplevel {}
  static boolean data_some(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_some")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Some");
    result = result && data_toplevel(builder, level + 1);
    result = result && data_some_2(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // {}
  private static boolean data_some_2(PsiBuilder builder, int level) {
    return true;
  }

  /* ********************************************************** */
  // data_nested | INT | STRING | 'True' | 'False' | 'Unit'
  static boolean data_toplevel(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_toplevel")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = data_nested(builder, level + 1);
    if (!result) result = consumeToken(builder, INT);
    if (!result) result = consumeToken(builder, STRING);
    if (!result) result = consumeToken(builder, TRUE);
    if (!result) result = consumeToken(builder, FALSE);
    if (!result) result = consumeToken(builder, UNIT);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // generic_instruction_block | generic_instruction_attrs
  public static boolean generic_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_instruction")) return false;
    if (!nextTokenIs(builder, INSTRUCTION_TOKEN)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = generic_instruction_block(builder, level + 1);
    if (!result) result = generic_instruction_attrs(builder, level + 1);
    exit_section_(builder, marker, GENERIC_INSTRUCTION, result);
    return result;
  }

  /* ********************************************************** */
  // INSTRUCTION_TOKEN (toplevel_type | data_toplevel | annotation)*
  static boolean generic_instruction_attrs(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_instruction_attrs")) return false;
    if (!nextTokenIs(builder, INSTRUCTION_TOKEN)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, INSTRUCTION_TOKEN);
    result = result && generic_instruction_attrs_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (toplevel_type | data_toplevel | annotation)*
  private static boolean generic_instruction_attrs_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_instruction_attrs_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!generic_instruction_attrs_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "generic_instruction_attrs_1", pos)) break;
    }
    return true;
  }

  // toplevel_type | data_toplevel | annotation
  private static boolean generic_instruction_attrs_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_instruction_attrs_1_0")) return false;
    boolean result;
    result = toplevel_type(builder, level + 1);
    if (!result) result = data_toplevel(builder, level + 1);
    if (!result) result = annotation(builder, level + 1);
    return result;
  }

  /* ********************************************************** */
  // INSTRUCTION_TOKEN block_instruction+
  static boolean generic_instruction_block(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_instruction_block")) return false;
    if (!nextTokenIs(builder, INSTRUCTION_TOKEN)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, INSTRUCTION_TOKEN);
    result = result && generic_instruction_block_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // block_instruction+
  private static boolean generic_instruction_block_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_instruction_block_1")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = block_instruction(builder, level + 1);
    while (result) {
      int pos = current_position_(builder);
      if (!block_instruction(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "generic_instruction_block_1", pos)) break;
    }
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // MACRO_TOKEN (annotation | block_instruction)*
  public static boolean generic_macro(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_macro")) return false;
    if (!nextTokenIs(builder, MACRO_TOKEN)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, MACRO_TOKEN);
    result = result && generic_macro_1(builder, level + 1);
    exit_section_(builder, marker, GENERIC_MACRO, result);
    return result;
  }

  // (annotation | block_instruction)*
  private static boolean generic_macro_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_macro_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!generic_macro_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "generic_macro_1", pos)) break;
    }
    return true;
  }

  // annotation | block_instruction
  private static boolean generic_macro_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_macro_1_0")) return false;
    boolean result;
    result = annotation(builder, level + 1);
    if (!result) result = block_instruction(builder, level + 1);
    return result;
  }

  /* ********************************************************** */
  // TYPE_NAME annotation*
  public static boolean generic_type(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_type")) return false;
    if (!nextTokenIs(builder, "<comparable type>", TYPE_NAME)) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, GENERIC_TYPE, "<comparable type>");
    result = consumeToken(builder, TYPE_NAME);
    result = result && generic_type_1(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // annotation*
  private static boolean generic_type_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_type_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!annotation(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "generic_type_1", pos)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // block_instruction
  //   | conditional_instruction
  //   | lambda_instruction
  //   | create_contract_instruction
  //   | generic_instruction
  //   | macro_instruction
  public static boolean instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, INSTRUCTION, "<instruction>");
    result = block_instruction(builder, level + 1);
    if (!result) result = conditional_instruction(builder, level + 1);
    if (!result) result = lambda_instruction(builder, level + 1);
    if (!result) result = create_contract_instruction(builder, level + 1);
    if (!result) result = generic_instruction(builder, level + 1);
    if (!result) result = macro_instruction(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // 'LAMBDA' (type | annotation)+ block_instruction
  public static boolean lambda_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "lambda_instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, LAMBDA_INSTRUCTION, "<lambda instruction>");
    result = consumeToken(builder, "LAMBDA");
    result = result && lambda_instruction_1(builder, level + 1);
    result = result && block_instruction(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // (type | annotation)+
  private static boolean lambda_instruction_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "lambda_instruction_1")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = lambda_instruction_1_0(builder, level + 1);
    while (result) {
      int pos = current_position_(builder);
      if (!lambda_instruction_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "lambda_instruction_1", pos)) break;
    }
    exit_section_(builder, marker, null, result);
    return result;
  }

  // type | annotation
  private static boolean lambda_instruction_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "lambda_instruction_1_0")) return false;
    boolean result;
    result = type(builder, level + 1);
    if (!result) result = annotation(builder, level + 1);
    return result;
  }

  /* ********************************************************** */
  // conditional_macro | generic_macro
  public static boolean macro_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "macro_instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, MACRO_INSTRUCTION, "<macro instruction>");
    result = conditional_macro(builder, level + 1);
    if (!result) result = generic_macro(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // '(' type ')'
  public static boolean nested_type(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "nested_type")) return false;
    if (!nextTokenIs(builder, LEFT_PAREN)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_PAREN);
    result = result && type(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_PAREN);
    exit_section_(builder, marker, NESTED_TYPE, result);
    return result;
  }

  /* ********************************************************** */
  // 'parameter' toplevel_type ';'
  public static boolean parameter_section(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "parameter_section")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, PARAMETER_SECTION, "<parameter section>");
    result = consumeToken(builder, "parameter");
    result = result && toplevel_type(builder, level + 1);
    result = result && consumeToken(builder, SEMI);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // 'return' toplevel_type ';'
  public static boolean return_section(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "return_section")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, RETURN_SECTION, "<return section>");
    result = consumeToken(builder, "return");
    result = result && toplevel_type(builder, level + 1);
    result = result && consumeToken(builder, SEMI);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // contract?
  static boolean script_file(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "script_file")) return false;
    contract(builder, level + 1);
    return true;
  }

  /* ********************************************************** */
  // parameter_section | return_section | storage_section | code_section
  public static boolean section(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "section")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, SECTION, "<section>");
    result = parameter_section(builder, level + 1);
    if (!result) result = return_section(builder, level + 1);
    if (!result) result = storage_section(builder, level + 1);
    if (!result) result = code_section(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // 'storage' toplevel_type ';'
  public static boolean storage_section(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "storage_section")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, STORAGE_SECTION, "<storage section>");
    result = consumeToken(builder, "storage");
    result = result && toplevel_type(builder, level + 1);
    result = result && consumeToken(builder, SEMI);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // nested_type | generic_type | comparable_type
  static boolean toplevel_type(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "toplevel_type")) return false;
    boolean result;
    result = nested_type(builder, level + 1);
    if (!result) result = generic_type(builder, level + 1);
    if (!result) result = comparable_type(builder, level + 1);
    return result;
  }

  /* ********************************************************** */
  // complex_type | toplevel_type
  public static boolean type(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, TYPE, "<type>");
    result = complex_type(builder, level + 1);
    if (!result) result = toplevel_type(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

}

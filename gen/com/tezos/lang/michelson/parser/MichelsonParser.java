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
    if (type == ARG_INSTRUCTION) {
      result = arg_instruction(builder, 0);
    }
    else if (type == ASSERT_MACRO) {
      result = assert_macro(builder, 0);
    }
    else if (type == BLOCK_INSTRUCTION) {
      result = block_instruction(builder, 0);
    }
    else if (type == CMP_MACRO) {
      result = cmp_macro(builder, 0);
    }
    else if (type == CODE_SECTION) {
      result = code_section(builder, 0);
    }
    else if (type == COMPARABLE_TYPE) {
      result = comparable_type(builder, 0);
    }
    else if (type == CONDITIONAL_MACRO) {
      result = conditional_macro(builder, 0);
    }
    else if (type == DATA) {
      result = data(builder, 0);
    }
    else if (type == FAIL_MACRO) {
      result = fail_macro(builder, 0);
    }
    else if (type == IF_INSTRUCTION) {
      result = if_instruction(builder, 0);
    }
    else if (type == INSTRUCTION) {
      result = instruction(builder, 0);
    }
    else if (type == MACRO_INSTRUCTION) {
      result = macro_instruction(builder, 0);
    }
    else if (type == MAP_ACCESS_MACRO) {
      result = map_access_macro(builder, 0);
    }
    else if (type == MAP_MACRO) {
      result = map_macro(builder, 0);
    }
    else if (type == PAIR_ACCESS_MACRO) {
      result = pair_access_macro(builder, 0);
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
    else if (type == SET_ACCESS_MACRO) {
      result = set_access_macro(builder, 0);
    }
    else if (type == SET_MACRO) {
      result = set_macro(builder, 0);
    }
    else if (type == SIMPLE_INSTRUCTION) {
      result = simple_instruction(builder, 0);
    }
    else if (type == STORAGE_SECTION) {
      result = storage_section(builder, 0);
    }
    else if (type == TYPE) {
      result = type(builder, 0);
    }
    else if (type == UNKNOWN_INSTRUCTION) {
      result = unknown_instruction(builder, 0);
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
    create_token_set_(COMPARABLE_TYPE, TYPE),
    create_token_set_(CODE_SECTION, PARAMETER_SECTION, RETURN_SECTION, SECTION,
      STORAGE_SECTION),
    create_token_set_(ARG_INSTRUCTION, ASSERT_MACRO, BLOCK_INSTRUCTION, CMP_MACRO,
      CONDITIONAL_MACRO, FAIL_MACRO, IF_INSTRUCTION, INSTRUCTION,
      MACRO_INSTRUCTION, MAP_ACCESS_MACRO, MAP_MACRO, PAIR_ACCESS_MACRO,
      SET_ACCESS_MACRO, SET_MACRO, SIMPLE_INSTRUCTION, UNKNOWN_INSTRUCTION),
  };

  /* ********************************************************** */
  // 'PUSH' toplevel_type toplevel_data
  //   | 'NONE' toplevel_type
  //   | 'LEFT' toplevel_type
  //   | 'RIGHT' toplevel_type
  //   | 'NIL' toplevel_type
  //   | 'EMPTY_SET' type
  //   | 'EMPTY_MAP' comparable_type type
  //   | 'MAP' block_instruction
  //   | 'ITER' block_instruction
  //   | 'LOOP' block_instruction
  //   | 'LOOP_LEFT' block_instruction
  //   | 'LAMBDA' type type block_instruction
  //   | 'DIP' block_instruction
  public static boolean arg_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, ARG_INSTRUCTION, "<arg instruction>");
    result = arg_instruction_0(builder, level + 1);
    if (!result) result = arg_instruction_1(builder, level + 1);
    if (!result) result = arg_instruction_2(builder, level + 1);
    if (!result) result = arg_instruction_3(builder, level + 1);
    if (!result) result = arg_instruction_4(builder, level + 1);
    if (!result) result = arg_instruction_5(builder, level + 1);
    if (!result) result = arg_instruction_6(builder, level + 1);
    if (!result) result = arg_instruction_7(builder, level + 1);
    if (!result) result = arg_instruction_8(builder, level + 1);
    if (!result) result = arg_instruction_9(builder, level + 1);
    if (!result) result = arg_instruction_10(builder, level + 1);
    if (!result) result = arg_instruction_11(builder, level + 1);
    if (!result) result = arg_instruction_12(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // 'PUSH' toplevel_type toplevel_data
  private static boolean arg_instruction_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "PUSH");
    result = result && toplevel_type(builder, level + 1);
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'NONE' toplevel_type
  private static boolean arg_instruction_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction_1")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "NONE");
    result = result && toplevel_type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'LEFT' toplevel_type
  private static boolean arg_instruction_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction_2")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "LEFT");
    result = result && toplevel_type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'RIGHT' toplevel_type
  private static boolean arg_instruction_3(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction_3")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "RIGHT");
    result = result && toplevel_type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'NIL' toplevel_type
  private static boolean arg_instruction_4(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction_4")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "NIL");
    result = result && toplevel_type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'EMPTY_SET' type
  private static boolean arg_instruction_5(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction_5")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "EMPTY_SET");
    result = result && type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'EMPTY_MAP' comparable_type type
  private static boolean arg_instruction_6(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction_6")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "EMPTY_MAP");
    result = result && comparable_type(builder, level + 1);
    result = result && type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'MAP' block_instruction
  private static boolean arg_instruction_7(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction_7")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "MAP");
    result = result && block_instruction(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'ITER' block_instruction
  private static boolean arg_instruction_8(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction_8")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "ITER");
    result = result && block_instruction(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'LOOP' block_instruction
  private static boolean arg_instruction_9(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction_9")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "LOOP");
    result = result && block_instruction(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'LOOP_LEFT' block_instruction
  private static boolean arg_instruction_10(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction_10")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "LOOP_LEFT");
    result = result && block_instruction(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'LAMBDA' type type block_instruction
  private static boolean arg_instruction_11(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction_11")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "LAMBDA");
    result = result && type(builder, level + 1);
    result = result && type(builder, level + 1);
    result = result && block_instruction(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'DIP' block_instruction
  private static boolean arg_instruction_12(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "arg_instruction_12")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "DIP");
    result = result && block_instruction(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // "ASSERT"
  //   | "ASSERT_EQ" | "ASSERT_NEQ" | "ASSERT_LT" | "ASSERT_LE" | "ASSERT_GT" | "ASSERT_GE"
  //   | "ASSERT_CMPEQ" | "ASSERT_CMPNEQ" |"ASSERT_CMPLT" | "ASSERT_CMPLE" | "ASSERT_CMPGT" | "ASSERT_CMPGE"
  //   | "ASSERT_NONE"
  //   | "ASSERT_SOME"
  //   | "ASSERT_LEFT"
  //   | "ASSERT_RIGHT"
  public static boolean assert_macro(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "assert_macro")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, ASSERT_MACRO, "<assert macro>");
    result = consumeToken(builder, "ASSERT");
    if (!result) result = consumeToken(builder, "ASSERT_EQ");
    if (!result) result = consumeToken(builder, "ASSERT_NEQ");
    if (!result) result = consumeToken(builder, "ASSERT_LT");
    if (!result) result = consumeToken(builder, "ASSERT_LE");
    if (!result) result = consumeToken(builder, "ASSERT_GT");
    if (!result) result = consumeToken(builder, "ASSERT_GE");
    if (!result) result = consumeToken(builder, "ASSERT_CMPEQ");
    if (!result) result = consumeToken(builder, "ASSERT_CMPNEQ");
    if (!result) result = consumeToken(builder, "ASSERT_CMPLT");
    if (!result) result = consumeToken(builder, "ASSERT_CMPLE");
    if (!result) result = consumeToken(builder, "ASSERT_CMPGT");
    if (!result) result = consumeToken(builder, "ASSERT_CMPGE");
    if (!result) result = consumeToken(builder, "ASSERT_NONE");
    if (!result) result = consumeToken(builder, "ASSERT_SOME");
    if (!result) result = consumeToken(builder, "ASSERT_LEFT");
    if (!result) result = consumeToken(builder, "ASSERT_RIGHT");
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // '{' (instruction (';' instruction)*)? '}'
  public static boolean block_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "block_instruction")) return false;
    if (!nextTokenIs(builder, LEFT_CURLY)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_CURLY);
    result = result && block_instruction_1(builder, level + 1);
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

  /* ********************************************************** */
  // "CMPEQ" | "CMPNEQ" | "CMPLT" | "CMPGT" | "CMPLE" | "CMPGE"
  public static boolean cmp_macro(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "cmp_macro")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, CMP_MACRO, "<cmp macro>");
    result = consumeToken(builder, "CMPEQ");
    if (!result) result = consumeToken(builder, "CMPNEQ");
    if (!result) result = consumeToken(builder, "CMPLT");
    if (!result) result = consumeToken(builder, "CMPGT");
    if (!result) result = consumeToken(builder, "CMPLE");
    if (!result) result = consumeToken(builder, "CMPGE");
    exit_section_(builder, level, marker, result, false, null);
    return result;
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
  // 'int'
  //   | 'nat'
  //   | 'string'
  //   | 'tez'
  //   | 'bool'
  //   | 'key_hash'
  //   | 'timestamp'
  public static boolean comparable_type(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "comparable_type")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, COMPARABLE_TYPE, "<comparable type>");
    result = consumeToken(builder, "int");
    if (!result) result = consumeToken(builder, "nat");
    if (!result) result = consumeToken(builder, "string");
    if (!result) result = consumeToken(builder, "tez");
    if (!result) result = consumeToken(builder, "bool");
    if (!result) result = consumeToken(builder, "key_hash");
    if (!result) result = consumeToken(builder, "timestamp");
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // ( "IF_SOME"
  //               | "IFEQ" | "IFNEQ" | "IFLT" | "IFGT" | "IFLE" | "IFGE"
  //               | "IFCMPEQ" | "IFCMPNEQ" | "IFCMPLT" | "IFCMPGT" | "IFCMPLE" | "IFCMPGE"
  //              ) block_instruction block_instruction
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
  // toplevel_data
  //   | 'Pair' toplevel_data toplevel_data
  //   | 'Left' toplevel_data
  //   | 'Right' toplevel_data
  //   | 'Some' toplevel_data
  //   | 'None'
  //   | '{' (toplevel_data (';' toplevel_data)*)? '}' //fixme
  //   | '{' ('Elt' toplevel_data toplevel_data (';' 'Elt' toplevel_data toplevel_data)*)? '}' //fixme
  //   | instruction
  public static boolean data(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, DATA, "<data>");
    result = toplevel_data(builder, level + 1);
    if (!result) result = data_1(builder, level + 1);
    if (!result) result = data_2(builder, level + 1);
    if (!result) result = data_3(builder, level + 1);
    if (!result) result = data_4(builder, level + 1);
    if (!result) result = consumeToken(builder, "None");
    if (!result) result = data_6(builder, level + 1);
    if (!result) result = data_7(builder, level + 1);
    if (!result) result = instruction(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // 'Pair' toplevel_data toplevel_data
  private static boolean data_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_1")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Pair");
    result = result && toplevel_data(builder, level + 1);
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'Left' toplevel_data
  private static boolean data_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_2")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Left");
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'Right' toplevel_data
  private static boolean data_3(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_3")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Right");
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'Some' toplevel_data
  private static boolean data_4(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_4")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Some");
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // '{' (toplevel_data (';' toplevel_data)*)? '}'
  private static boolean data_6(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_6")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_CURLY);
    result = result && data_6_1(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_CURLY);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (toplevel_data (';' toplevel_data)*)?
  private static boolean data_6_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_6_1")) return false;
    data_6_1_0(builder, level + 1);
    return true;
  }

  // toplevel_data (';' toplevel_data)*
  private static boolean data_6_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_6_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = toplevel_data(builder, level + 1);
    result = result && data_6_1_0_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (';' toplevel_data)*
  private static boolean data_6_1_0_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_6_1_0_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!data_6_1_0_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "data_6_1_0_1", pos)) break;
    }
    return true;
  }

  // ';' toplevel_data
  private static boolean data_6_1_0_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_6_1_0_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, SEMI);
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // '{' ('Elt' toplevel_data toplevel_data (';' 'Elt' toplevel_data toplevel_data)*)? '}'
  private static boolean data_7(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_7")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_CURLY);
    result = result && data_7_1(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_CURLY);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // ('Elt' toplevel_data toplevel_data (';' 'Elt' toplevel_data toplevel_data)*)?
  private static boolean data_7_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_7_1")) return false;
    data_7_1_0(builder, level + 1);
    return true;
  }

  // 'Elt' toplevel_data toplevel_data (';' 'Elt' toplevel_data toplevel_data)*
  private static boolean data_7_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_7_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Elt");
    result = result && toplevel_data(builder, level + 1);
    result = result && toplevel_data(builder, level + 1);
    result = result && data_7_1_0_3(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (';' 'Elt' toplevel_data toplevel_data)*
  private static boolean data_7_1_0_3(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_7_1_0_3")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!data_7_1_0_3_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "data_7_1_0_3", pos)) break;
    }
    return true;
  }

  // ';' 'Elt' toplevel_data toplevel_data
  private static boolean data_7_1_0_3_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_7_1_0_3_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, SEMI);
    result = result && consumeToken(builder, "Elt");
    result = result && toplevel_data(builder, level + 1);
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // "FAIL"
  public static boolean fail_macro(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "fail_macro")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, FAIL_MACRO, "<fail macro>");
    result = consumeToken(builder, "FAIL");
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // ('IF' | 'IF_CONS' | 'IF_LEFT' | 'IF_NONE') block_instruction block_instruction
  public static boolean if_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "if_instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, IF_INSTRUCTION, "<if instruction>");
    result = if_instruction_0(builder, level + 1);
    result = result && block_instruction(builder, level + 1);
    result = result && block_instruction(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // 'IF' | 'IF_CONS' | 'IF_LEFT' | 'IF_NONE'
  private static boolean if_instruction_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "if_instruction_0")) return false;
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
  // block_instruction
  //   | if_instruction
  //   | simple_instruction
  //   | arg_instruction
  //   | macro_instruction
  //   | unknown_instruction
  public static boolean instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, INSTRUCTION, "<instruction>");
    result = block_instruction(builder, level + 1);
    if (!result) result = if_instruction(builder, level + 1);
    if (!result) result = simple_instruction(builder, level + 1);
    if (!result) result = arg_instruction(builder, level + 1);
    if (!result) result = macro_instruction(builder, level + 1);
    if (!result) result = unknown_instruction(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // cmp_macro
  //   | conditional_macro
  //   | fail_macro
  //   | assert_macro
  //   | pair_access_macro
  //   | set_macro
  //   | map_macro
  public static boolean macro_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "macro_instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, MACRO_INSTRUCTION, "<macro instruction>");
    result = cmp_macro(builder, level + 1);
    if (!result) result = conditional_macro(builder, level + 1);
    if (!result) result = fail_macro(builder, level + 1);
    if (!result) result = assert_macro(builder, level + 1);
    if (!result) result = pair_access_macro(builder, level + 1);
    if (!result) result = set_macro(builder, level + 1);
    if (!result) result = map_macro(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // MACRO_MAP_CADR_TOKEN
  public static boolean map_access_macro(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "map_access_macro")) return false;
    if (!nextTokenIs(builder, MACRO_MAP_CADR_TOKEN)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, MACRO_MAP_CADR_TOKEN);
    exit_section_(builder, marker, MAP_ACCESS_MACRO, result);
    return result;
  }

  /* ********************************************************** */
  // "MAP_CAR" | "MAP_CDR" | map_access_macro
  public static boolean map_macro(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "map_macro")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, MAP_MACRO, "<map macro>");
    result = consumeToken(builder, "MAP_CAR");
    if (!result) result = consumeToken(builder, "MAP_CDR");
    if (!result) result = map_access_macro(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // (MACRO_DIIP_TOKEN | MACRO_DUUP_TOKEN | MACRO_NESTED_TOKEN | MACRO_PAIR_ACCESS_TOKEN | MACRO_PAIRS_TOKEN) block_instruction
  public static boolean pair_access_macro(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "pair_access_macro")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, PAIR_ACCESS_MACRO, "<pair access macro>");
    result = pair_access_macro_0(builder, level + 1);
    result = result && block_instruction(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // MACRO_DIIP_TOKEN | MACRO_DUUP_TOKEN | MACRO_NESTED_TOKEN | MACRO_PAIR_ACCESS_TOKEN | MACRO_PAIRS_TOKEN
  private static boolean pair_access_macro_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "pair_access_macro_0")) return false;
    boolean result;
    result = consumeToken(builder, MACRO_DIIP_TOKEN);
    if (!result) result = consumeToken(builder, MACRO_DUUP_TOKEN);
    if (!result) result = consumeToken(builder, MACRO_NESTED_TOKEN);
    if (!result) result = consumeToken(builder, MACRO_PAIR_ACCESS_TOKEN);
    if (!result) result = consumeToken(builder, MACRO_PAIRS_TOKEN);
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
  // section*
  static boolean script_file(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "script_file")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!section(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "script_file", pos)) break;
    }
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
  // MACRO_SET_CADR_TOKEN
  public static boolean set_access_macro(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "set_access_macro")) return false;
    if (!nextTokenIs(builder, MACRO_SET_CADR_TOKEN)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, MACRO_SET_CADR_TOKEN);
    exit_section_(builder, marker, SET_ACCESS_MACRO, result);
    return result;
  }

  /* ********************************************************** */
  // "SET_CAR" | "SET_CDR" | set_access_macro
  public static boolean set_macro(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "set_macro")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, SET_MACRO, "<set macro>");
    result = consumeToken(builder, "SET_CAR");
    if (!result) result = consumeToken(builder, "SET_CDR");
    if (!result) result = set_access_macro(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // 'DROP'
  //   | 'DUP'
  //   | 'SWAP'
  //   | 'SOME'
  //   | 'UNIT'
  //   | 'PAIR'
  //   | 'CAR'
  //   | 'CDR'
  //   | 'CONS'
  //   | 'MEM'
  //   | 'GET'
  //   | 'UPDATE'
  //   | 'EXEC'
  //   | 'FAILWITH'
  //   | 'CAST'
  //   | 'RENAME'
  //   | 'CONCAT'
  //   | 'ADD'
  //   | 'SUB'
  //   | 'MUL'
  //   | 'DIV'
  //   | 'ABS'
  //   | 'NEG'
  //   | 'MOD'
  //   | 'LSL'
  //   | 'LSR'
  //   | 'OR'
  //   | 'AND'
  //   | 'XOR'
  //   | 'NOT'
  //   | 'COMPARE'
  //   | 'EQ'
  //   | 'NEQ'
  //   | 'LT'
  //   | 'GT'
  //   | 'LE'
  //   | 'GE'
  //   | 'INT'
  //   | 'SELF'
  //   | 'TRANSFER_TOKENS'
  //   | 'SET_DELEGATE'
  //   | 'CREATE_ACCOUNT'
  //   | 'CREATE_CONTRACT'
  //   | 'IMPLICIT_ACCOUNT'
  //   | 'NOW'
  //   | 'AMOUNT'
  //   | 'BALANCE'
  //   | 'CHECK_SIGNATURE'
  //   | 'BLAKE2B'
  //   | 'HASH_KEY'
  //   | 'STEPS_TO_QUOTA'
  //   | 'SOURCE'
  //   | 'SENDER'
  public static boolean simple_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "simple_instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, SIMPLE_INSTRUCTION, "<simple instruction>");
    result = consumeToken(builder, "DROP");
    if (!result) result = consumeToken(builder, "DUP");
    if (!result) result = consumeToken(builder, "SWAP");
    if (!result) result = consumeToken(builder, "SOME");
    if (!result) result = consumeToken(builder, "UNIT");
    if (!result) result = consumeToken(builder, "PAIR");
    if (!result) result = consumeToken(builder, "CAR");
    if (!result) result = consumeToken(builder, "CDR");
    if (!result) result = consumeToken(builder, "CONS");
    if (!result) result = consumeToken(builder, "MEM");
    if (!result) result = consumeToken(builder, "GET");
    if (!result) result = consumeToken(builder, "UPDATE");
    if (!result) result = consumeToken(builder, "EXEC");
    if (!result) result = consumeToken(builder, "FAILWITH");
    if (!result) result = consumeToken(builder, "CAST");
    if (!result) result = consumeToken(builder, "RENAME");
    if (!result) result = consumeToken(builder, "CONCAT");
    if (!result) result = consumeToken(builder, "ADD");
    if (!result) result = consumeToken(builder, "SUB");
    if (!result) result = consumeToken(builder, "MUL");
    if (!result) result = consumeToken(builder, "DIV");
    if (!result) result = consumeToken(builder, "ABS");
    if (!result) result = consumeToken(builder, "NEG");
    if (!result) result = consumeToken(builder, "MOD");
    if (!result) result = consumeToken(builder, "LSL");
    if (!result) result = consumeToken(builder, "LSR");
    if (!result) result = consumeToken(builder, "OR");
    if (!result) result = consumeToken(builder, "AND");
    if (!result) result = consumeToken(builder, "XOR");
    if (!result) result = consumeToken(builder, "NOT");
    if (!result) result = consumeToken(builder, "COMPARE");
    if (!result) result = consumeToken(builder, "EQ");
    if (!result) result = consumeToken(builder, "NEQ");
    if (!result) result = consumeToken(builder, "LT");
    if (!result) result = consumeToken(builder, "GT");
    if (!result) result = consumeToken(builder, "LE");
    if (!result) result = consumeToken(builder, "GE");
    if (!result) result = consumeToken(builder, "INT");
    if (!result) result = consumeToken(builder, "SELF");
    if (!result) result = consumeToken(builder, "TRANSFER_TOKENS");
    if (!result) result = consumeToken(builder, "SET_DELEGATE");
    if (!result) result = consumeToken(builder, "CREATE_ACCOUNT");
    if (!result) result = consumeToken(builder, "CREATE_CONTRACT");
    if (!result) result = consumeToken(builder, "IMPLICIT_ACCOUNT");
    if (!result) result = consumeToken(builder, "NOW");
    if (!result) result = consumeToken(builder, "AMOUNT");
    if (!result) result = consumeToken(builder, "BALANCE");
    if (!result) result = consumeToken(builder, "CHECK_SIGNATURE");
    if (!result) result = consumeToken(builder, "BLAKE2B");
    if (!result) result = consumeToken(builder, "HASH_KEY");
    if (!result) result = consumeToken(builder, "STEPS_TO_QUOTA");
    if (!result) result = consumeToken(builder, "SOURCE");
    if (!result) result = consumeToken(builder, "SENDER");
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
  // '(' data ')'
  //   | INT
  //   | STRING
  // //  | TIMESTAMP_STRING
  // //  | SIGNATURE_STRING
  // //  | KEY_STRING
  // //  | KEY_HASH_STRING
  // //  | TEZ_STRING
  // //  | CONTRACT_STRING
  //   | 'Unit'
  //   | 'True'
  //   | 'False'
  static boolean toplevel_data(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "toplevel_data")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = toplevel_data_0(builder, level + 1);
    if (!result) result = consumeToken(builder, INT);
    if (!result) result = consumeToken(builder, STRING);
    if (!result) result = consumeToken(builder, "Unit");
    if (!result) result = consumeToken(builder, TRUE);
    if (!result) result = consumeToken(builder, FALSE);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // '(' data ')'
  private static boolean toplevel_data_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "toplevel_data_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_PAREN);
    result = result && data(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_PAREN);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // '(' type ')'
  //   | 'key'
  //   | 'unit'
  //   | 'signature'
  //   | 'operation'
  //   | comparable_type
  static boolean toplevel_type(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "toplevel_type")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = toplevel_type_0(builder, level + 1);
    if (!result) result = consumeToken(builder, "key");
    if (!result) result = consumeToken(builder, "unit");
    if (!result) result = consumeToken(builder, "signature");
    if (!result) result = consumeToken(builder, "operation");
    if (!result) result = comparable_type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // '(' type ')'
  private static boolean toplevel_type_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "toplevel_type_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_PAREN);
    result = result && type(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_PAREN);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // toplevel_type
  //   | 'option' type
  //   | 'list' type
  //   | 'set' comparable_type
  //   | 'contract' type
  //   | 'pair' type type
  //   | 'or' type type
  //   | 'lambda' type type
  //   | 'map' comparable_type type
  //   | 'big_map' comparable_type type
  public static boolean type(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, TYPE, "<type>");
    result = toplevel_type(builder, level + 1);
    if (!result) result = type_1(builder, level + 1);
    if (!result) result = type_2(builder, level + 1);
    if (!result) result = type_3(builder, level + 1);
    if (!result) result = type_4(builder, level + 1);
    if (!result) result = type_5(builder, level + 1);
    if (!result) result = type_6(builder, level + 1);
    if (!result) result = type_7(builder, level + 1);
    if (!result) result = type_8(builder, level + 1);
    if (!result) result = type_9(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // 'option' type
  private static boolean type_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_1")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "option");
    result = result && type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'list' type
  private static boolean type_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_2")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "list");
    result = result && type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'set' comparable_type
  private static boolean type_3(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_3")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "set");
    result = result && comparable_type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'contract' type
  private static boolean type_4(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_4")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "contract");
    result = result && type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'pair' type type
  private static boolean type_5(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_5")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "pair");
    result = result && type(builder, level + 1);
    result = result && type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'or' type type
  private static boolean type_6(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_6")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "or");
    result = result && type(builder, level + 1);
    result = result && type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'lambda' type type
  private static boolean type_7(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_7")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "lambda");
    result = result && type(builder, level + 1);
    result = result && type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'map' comparable_type type
  private static boolean type_8(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_8")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "map");
    result = result && comparable_type(builder, level + 1);
    result = result && type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'big_map' comparable_type type
  private static boolean type_9(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_9")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "big_map");
    result = result && comparable_type(builder, level + 1);
    result = result && type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // INSTRUCTION_TOKEN (block_instruction | toplevel_type | toplevel_data)*
  public static boolean unknown_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "unknown_instruction")) return false;
    if (!nextTokenIs(builder, INSTRUCTION_TOKEN)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, INSTRUCTION_TOKEN);
    result = result && unknown_instruction_1(builder, level + 1);
    exit_section_(builder, marker, UNKNOWN_INSTRUCTION, result);
    return result;
  }

  // (block_instruction | toplevel_type | toplevel_data)*
  private static boolean unknown_instruction_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "unknown_instruction_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!unknown_instruction_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "unknown_instruction_1", pos)) break;
    }
    return true;
  }

  // block_instruction | toplevel_type | toplevel_data
  private static boolean unknown_instruction_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "unknown_instruction_1_0")) return false;
    boolean result;
    result = block_instruction(builder, level + 1);
    if (!result) result = toplevel_type(builder, level + 1);
    if (!result) result = toplevel_data(builder, level + 1);
    return result;
  }

}

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
    builder = adapt_builder_(type, builder, this, null);
    Marker marker = enter_section_(builder, 0, _COLLAPSE_, null);
    if (type == CODE_SECTION) {
      result = code_section(builder, 0);
    }
    else if (type == COMPARABLE_TYPE) {
      result = comparable_type(builder, 0);
    }
    else if (type == DATA) {
      result = data(builder, 0);
    }
    else if (type == DATA_TOPLEVEL) {
      result = data_toplevel(builder, 0);
    }
    else if (type == INSTRUCTION) {
      result = instruction(builder, 0);
    }
    else if (type == INSTRUCTIONS) {
      result = instructions(builder, 0);
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
    else if (type == TYPE_TOPLEVEL) {
      result = type_toplevel(builder, 0);
    }
    else {
      result = parse_root_(type, builder, 0);
    }
    exit_section_(builder, 0, marker, type, result, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType type, PsiBuilder builder, int level) {
    return script_file(builder, level + 1);
  }

  /* ********************************************************** */
  // 'code' instruction ';'
  public static boolean code_section(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "code_section")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, CODE_SECTION, "<code section>");
    result = consumeToken(builder, "code");
    result = result && instruction(builder, level + 1);
    result = result && consumeToken(builder, SEMI);
    exit_section_(builder, level, marker, result, false, null);
    return result;
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
  // data_toplevel
  //   |  'Pair' data_toplevel data_toplevel
  //   | 'Left' data_toplevel
  //   | 'Right' data_toplevel
  //   | 'Some' data_toplevel
  //   | 'None'
  //   | '{' (data_toplevel (';' data_toplevel)*)? '}' //fixme
  //   | '{' ('Elt' data_toplevel data_toplevel (';' 'Elt' data_toplevel data_toplevel)*)? '}' //fixme
  //   | instruction
  public static boolean data(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, DATA, "<data>");
    result = data_toplevel(builder, level + 1);
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

  // 'Pair' data_toplevel data_toplevel
  private static boolean data_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_1")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Pair");
    result = result && data_toplevel(builder, level + 1);
    result = result && data_toplevel(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'Left' data_toplevel
  private static boolean data_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_2")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Left");
    result = result && data_toplevel(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'Right' data_toplevel
  private static boolean data_3(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_3")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Right");
    result = result && data_toplevel(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'Some' data_toplevel
  private static boolean data_4(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_4")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Some");
    result = result && data_toplevel(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // '{' (data_toplevel (';' data_toplevel)*)? '}'
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

  // (data_toplevel (';' data_toplevel)*)?
  private static boolean data_6_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_6_1")) return false;
    data_6_1_0(builder, level + 1);
    return true;
  }

  // data_toplevel (';' data_toplevel)*
  private static boolean data_6_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_6_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = data_toplevel(builder, level + 1);
    result = result && data_6_1_0_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (';' data_toplevel)*
  private static boolean data_6_1_0_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_6_1_0_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!data_6_1_0_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "data_6_1_0_1", pos)) break;
    }
    return true;
  }

  // ';' data_toplevel
  private static boolean data_6_1_0_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_6_1_0_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, SEMI);
    result = result && data_toplevel(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // '{' ('Elt' data_toplevel data_toplevel (';' 'Elt' data_toplevel data_toplevel)*)? '}'
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

  // ('Elt' data_toplevel data_toplevel (';' 'Elt' data_toplevel data_toplevel)*)?
  private static boolean data_7_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_7_1")) return false;
    data_7_1_0(builder, level + 1);
    return true;
  }

  // 'Elt' data_toplevel data_toplevel (';' 'Elt' data_toplevel data_toplevel)*
  private static boolean data_7_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_7_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Elt");
    result = result && data_toplevel(builder, level + 1);
    result = result && data_toplevel(builder, level + 1);
    result = result && data_7_1_0_3(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (';' 'Elt' data_toplevel data_toplevel)*
  private static boolean data_7_1_0_3(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_7_1_0_3")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!data_7_1_0_3_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "data_7_1_0_3", pos)) break;
    }
    return true;
  }

  // ';' 'Elt' data_toplevel data_toplevel
  private static boolean data_7_1_0_3_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_7_1_0_3_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, SEMI);
    result = result && consumeToken(builder, "Elt");
    result = result && data_toplevel(builder, level + 1);
    result = result && data_toplevel(builder, level + 1);
    exit_section_(builder, marker, null, result);
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
  public static boolean data_toplevel(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_toplevel")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, DATA_TOPLEVEL, "<data toplevel>");
    result = data_toplevel_0(builder, level + 1);
    if (!result) result = consumeToken(builder, INT);
    if (!result) result = consumeToken(builder, STRING);
    if (!result) result = consumeToken(builder, "Unit");
    if (!result) result = consumeToken(builder, "True");
    if (!result) result = consumeToken(builder, "False");
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // '(' data ')'
  private static boolean data_toplevel_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_toplevel_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_PAREN);
    result = result && data(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_PAREN);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // instructions
  //   | 'DROP'
  //   | 'DUP'
  //   | 'SWAP'
  //   | 'PUSH' type_toplevel data_toplevel
  //   | 'SOME'
  //   | 'NONE' type_toplevel
  //   | 'UNIT'
  //   | 'IF_NONE' instructions instructions
  //   | 'PAIR'
  //   | 'CAR'
  //   | 'CDR'
  //   | 'LEFT' type_toplevel
  //   | 'RIGHT' type_toplevel
  //   | 'IF_LEFT' instructions instructions
  //   | 'NIL' type_toplevel
  //   | 'CONS'
  //   | 'IF_CONS' instructions instructions
  //   | 'EMPTY_SET' type
  //   | 'EMPTY_MAP' comparable_type type
  //   | 'MAP' instructions
  //   | 'ITER' instructions
  //   | 'MEM'
  //   | 'GET'
  //   | 'UPDATE'
  //   | 'IF' instructions instructions
  //   | 'LOOP' instructions
  //   | 'LOOP_LEFT' instructions
  //   | 'LAMBDA' type type instructions
  //   | 'EXEC'
  //   | 'DIP' instructions
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
  public static boolean instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, INSTRUCTION, "<instruction>");
    result = instructions(builder, level + 1);
    if (!result) result = consumeToken(builder, "DROP");
    if (!result) result = consumeToken(builder, "DUP");
    if (!result) result = consumeToken(builder, "SWAP");
    if (!result) result = instruction_4(builder, level + 1);
    if (!result) result = consumeToken(builder, "SOME");
    if (!result) result = instruction_6(builder, level + 1);
    if (!result) result = consumeToken(builder, "UNIT");
    if (!result) result = instruction_8(builder, level + 1);
    if (!result) result = consumeToken(builder, "PAIR");
    if (!result) result = consumeToken(builder, "CAR");
    if (!result) result = consumeToken(builder, "CDR");
    if (!result) result = instruction_12(builder, level + 1);
    if (!result) result = instruction_13(builder, level + 1);
    if (!result) result = instruction_14(builder, level + 1);
    if (!result) result = instruction_15(builder, level + 1);
    if (!result) result = consumeToken(builder, "CONS");
    if (!result) result = instruction_17(builder, level + 1);
    if (!result) result = instruction_18(builder, level + 1);
    if (!result) result = instruction_19(builder, level + 1);
    if (!result) result = instruction_20(builder, level + 1);
    if (!result) result = instruction_21(builder, level + 1);
    if (!result) result = consumeToken(builder, "MEM");
    if (!result) result = consumeToken(builder, "GET");
    if (!result) result = consumeToken(builder, "UPDATE");
    if (!result) result = instruction_25(builder, level + 1);
    if (!result) result = instruction_26(builder, level + 1);
    if (!result) result = instruction_27(builder, level + 1);
    if (!result) result = instruction_28(builder, level + 1);
    if (!result) result = consumeToken(builder, "EXEC");
    if (!result) result = instruction_30(builder, level + 1);
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

  // 'PUSH' type_toplevel data_toplevel
  private static boolean instruction_4(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_4")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "PUSH");
    result = result && type_toplevel(builder, level + 1);
    result = result && data_toplevel(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'NONE' type_toplevel
  private static boolean instruction_6(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_6")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "NONE");
    result = result && type_toplevel(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'IF_NONE' instructions instructions
  private static boolean instruction_8(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_8")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "IF_NONE");
    result = result && instructions(builder, level + 1);
    result = result && instructions(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'LEFT' type_toplevel
  private static boolean instruction_12(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_12")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "LEFT");
    result = result && type_toplevel(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'RIGHT' type_toplevel
  private static boolean instruction_13(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_13")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "RIGHT");
    result = result && type_toplevel(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'IF_LEFT' instructions instructions
  private static boolean instruction_14(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_14")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "IF_LEFT");
    result = result && instructions(builder, level + 1);
    result = result && instructions(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'NIL' type_toplevel
  private static boolean instruction_15(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_15")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "NIL");
    result = result && type_toplevel(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'IF_CONS' instructions instructions
  private static boolean instruction_17(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_17")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "IF_CONS");
    result = result && instructions(builder, level + 1);
    result = result && instructions(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'EMPTY_SET' type
  private static boolean instruction_18(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_18")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "EMPTY_SET");
    result = result && type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'EMPTY_MAP' comparable_type type
  private static boolean instruction_19(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_19")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "EMPTY_MAP");
    result = result && comparable_type(builder, level + 1);
    result = result && type(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'MAP' instructions
  private static boolean instruction_20(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_20")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "MAP");
    result = result && instructions(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'ITER' instructions
  private static boolean instruction_21(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_21")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "ITER");
    result = result && instructions(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'IF' instructions instructions
  private static boolean instruction_25(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_25")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "IF");
    result = result && instructions(builder, level + 1);
    result = result && instructions(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'LOOP' instructions
  private static boolean instruction_26(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_26")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "LOOP");
    result = result && instructions(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'LOOP_LEFT' instructions
  private static boolean instruction_27(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_27")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "LOOP_LEFT");
    result = result && instructions(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'LAMBDA' type type instructions
  private static boolean instruction_28(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_28")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "LAMBDA");
    result = result && type(builder, level + 1);
    result = result && type(builder, level + 1);
    result = result && instructions(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // 'DIP' instructions
  private static boolean instruction_30(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction_30")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "DIP");
    result = result && instructions(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // '{' (instruction (';' instruction)*)? '}'
  public static boolean instructions(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instructions")) return false;
    if (!nextTokenIs(builder, LEFT_CURLY)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_CURLY);
    result = result && instructions_1(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_CURLY);
    exit_section_(builder, marker, INSTRUCTIONS, result);
    return result;
  }

  // (instruction (';' instruction)*)?
  private static boolean instructions_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instructions_1")) return false;
    instructions_1_0(builder, level + 1);
    return true;
  }

  // instruction (';' instruction)*
  private static boolean instructions_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instructions_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = instruction(builder, level + 1);
    result = result && instructions_1_0_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (';' instruction)*
  private static boolean instructions_1_0_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instructions_1_0_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!instructions_1_0_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "instructions_1_0_1", pos)) break;
    }
    return true;
  }

  // ';' instruction
  private static boolean instructions_1_0_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instructions_1_0_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, SEMI);
    result = result && instruction(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // 'parameter' type_toplevel ';'
  public static boolean parameter_section(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "parameter_section")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, PARAMETER_SECTION, "<parameter section>");
    result = consumeToken(builder, "parameter");
    result = result && type_toplevel(builder, level + 1);
    result = result && consumeToken(builder, SEMI);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // 'return' type_toplevel ';'
  public static boolean return_section(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "return_section")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, RETURN_SECTION, "<return section>");
    result = consumeToken(builder, "return");
    result = result && type_toplevel(builder, level + 1);
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
    Marker marker = enter_section_(builder, level, _NONE_, SECTION, "<section>");
    result = parameter_section(builder, level + 1);
    if (!result) result = return_section(builder, level + 1);
    if (!result) result = storage_section(builder, level + 1);
    if (!result) result = code_section(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // 'storage' type_toplevel ';'
  public static boolean storage_section(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "storage_section")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, STORAGE_SECTION, "<storage section>");
    result = consumeToken(builder, "storage");
    result = result && type_toplevel(builder, level + 1);
    result = result && consumeToken(builder, SEMI);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // type_toplevel
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
    Marker marker = enter_section_(builder, level, _NONE_, TYPE, "<type>");
    result = type_toplevel(builder, level + 1);
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
  // '(' type ')'
  //   | 'key'
  //   | 'unit'
  //   | 'signature'
  //   | 'operation'
  //   | comparable_type
  public static boolean type_toplevel(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_toplevel")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, TYPE_TOPLEVEL, "<type toplevel>");
    result = type_toplevel_0(builder, level + 1);
    if (!result) result = consumeToken(builder, "key");
    if (!result) result = consumeToken(builder, "unit");
    if (!result) result = consumeToken(builder, "signature");
    if (!result) result = consumeToken(builder, "operation");
    if (!result) result = comparable_type(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // '(' type ')'
  private static boolean type_toplevel_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_toplevel_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_PAREN);
    result = result && type(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_PAREN);
    exit_section_(builder, marker, null, result);
    return result;
  }

}

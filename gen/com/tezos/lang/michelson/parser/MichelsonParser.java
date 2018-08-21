// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.tezos.lang.michelson.MichelsonTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class MichelsonParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == CODE_SECTION) {
      r = code_section(b, 0);
    }
    else if (t == COMPARABLE_TYPE) {
      r = comparable_type(b, 0);
    }
    else if (t == DATA) {
      r = data(b, 0);
    }
    else if (t == DATA_TOPLEVEL) {
      r = data_toplevel(b, 0);
    }
    else if (t == INSTRUCTION) {
      r = instruction(b, 0);
    }
    else if (t == INSTRUCTIONS) {
      r = instructions(b, 0);
    }
    else if (t == PARAMETER_SECTION) {
      r = parameter_section(b, 0);
    }
    else if (t == RETURN_SECTION) {
      r = return_section(b, 0);
    }
    else if (t == SECTION) {
      r = section(b, 0);
    }
    else if (t == STORAGE_SECTION) {
      r = storage_section(b, 0);
    }
    else if (t == TYPE) {
      r = type(b, 0);
    }
    else if (t == TYPE_TOPLEVEL) {
      r = type_toplevel(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return grammar(b, l + 1);
  }

  /* ********************************************************** */
  // 'code' instruction
  public static boolean code_section(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_section")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CODE_SECTION, "<code section>");
    r = consumeToken(b, "code");
    r = r && instruction(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'int'
  //   | 'nat'
  //   | 'string'
  //   | 'tez'
  //   | 'bool'
  //   | 'key_hash'
  //   | 'timestamp'
  public static boolean comparable_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparable_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMPARABLE_TYPE, "<comparable type>");
    r = consumeToken(b, "int");
    if (!r) r = consumeToken(b, "nat");
    if (!r) r = consumeToken(b, "string");
    if (!r) r = consumeToken(b, "tez");
    if (!r) r = consumeToken(b, "bool");
    if (!r) r = consumeToken(b, "key_hash");
    if (!r) r = consumeToken(b, "timestamp");
    exit_section_(b, l, m, r, false, null);
    return r;
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
  public static boolean data(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DATA, "<data>");
    r = data_toplevel(b, l + 1);
    if (!r) r = data_1(b, l + 1);
    if (!r) r = data_2(b, l + 1);
    if (!r) r = data_3(b, l + 1);
    if (!r) r = data_4(b, l + 1);
    if (!r) r = consumeToken(b, "None");
    if (!r) r = data_6(b, l + 1);
    if (!r) r = data_7(b, l + 1);
    if (!r) r = instruction(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'Pair' data_toplevel data_toplevel
  private static boolean data_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "Pair");
    r = r && data_toplevel(b, l + 1);
    r = r && data_toplevel(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'Left' data_toplevel
  private static boolean data_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "Left");
    r = r && data_toplevel(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'Right' data_toplevel
  private static boolean data_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "Right");
    r = r && data_toplevel(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'Some' data_toplevel
  private static boolean data_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "Some");
    r = r && data_toplevel(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '{' (data_toplevel (';' data_toplevel)*)? '}'
  private static boolean data_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "{");
    r = r && data_6_1(b, l + 1);
    r = r && consumeToken(b, "}");
    exit_section_(b, m, null, r);
    return r;
  }

  // (data_toplevel (';' data_toplevel)*)?
  private static boolean data_6_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_6_1")) return false;
    data_6_1_0(b, l + 1);
    return true;
  }

  // data_toplevel (';' data_toplevel)*
  private static boolean data_6_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_6_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = data_toplevel(b, l + 1);
    r = r && data_6_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (';' data_toplevel)*
  private static boolean data_6_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_6_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!data_6_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "data_6_1_0_1", c)) break;
    }
    return true;
  }

  // ';' data_toplevel
  private static boolean data_6_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_6_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ";");
    r = r && data_toplevel(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '{' ('Elt' data_toplevel data_toplevel (';' 'Elt' data_toplevel data_toplevel)*)? '}'
  private static boolean data_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_7")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "{");
    r = r && data_7_1(b, l + 1);
    r = r && consumeToken(b, "}");
    exit_section_(b, m, null, r);
    return r;
  }

  // ('Elt' data_toplevel data_toplevel (';' 'Elt' data_toplevel data_toplevel)*)?
  private static boolean data_7_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_7_1")) return false;
    data_7_1_0(b, l + 1);
    return true;
  }

  // 'Elt' data_toplevel data_toplevel (';' 'Elt' data_toplevel data_toplevel)*
  private static boolean data_7_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_7_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "Elt");
    r = r && data_toplevel(b, l + 1);
    r = r && data_toplevel(b, l + 1);
    r = r && data_7_1_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (';' 'Elt' data_toplevel data_toplevel)*
  private static boolean data_7_1_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_7_1_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!data_7_1_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "data_7_1_0_3", c)) break;
    }
    return true;
  }

  // ';' 'Elt' data_toplevel data_toplevel
  private static boolean data_7_1_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_7_1_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ";");
    r = r && consumeToken(b, "Elt");
    r = r && data_toplevel(b, l + 1);
    r = r && data_toplevel(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
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
  public static boolean data_toplevel(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_toplevel")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DATA_TOPLEVEL, "<data toplevel>");
    r = data_toplevel_0(b, l + 1);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, "Unit");
    if (!r) r = consumeToken(b, "True");
    if (!r) r = consumeToken(b, "False");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '(' data ')'
  private static boolean data_toplevel_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_toplevel_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "(");
    r = r && data(b, l + 1);
    r = r && consumeToken(b, ")");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // section*
  static boolean grammar(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grammar")) return false;
    while (true) {
      int c = current_position_(b);
      if (!section(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "grammar", c)) break;
    }
    return true;
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
  public static boolean instruction(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INSTRUCTION, "<instruction>");
    r = instructions(b, l + 1);
    if (!r) r = consumeToken(b, "DROP");
    if (!r) r = consumeToken(b, "DUP");
    if (!r) r = consumeToken(b, "SWAP");
    if (!r) r = instruction_4(b, l + 1);
    if (!r) r = consumeToken(b, "SOME");
    if (!r) r = instruction_6(b, l + 1);
    if (!r) r = consumeToken(b, "UNIT");
    if (!r) r = instruction_8(b, l + 1);
    if (!r) r = consumeToken(b, "PAIR");
    if (!r) r = consumeToken(b, "CAR");
    if (!r) r = consumeToken(b, "CDR");
    if (!r) r = instruction_12(b, l + 1);
    if (!r) r = instruction_13(b, l + 1);
    if (!r) r = instruction_14(b, l + 1);
    if (!r) r = instruction_15(b, l + 1);
    if (!r) r = consumeToken(b, "CONS");
    if (!r) r = instruction_17(b, l + 1);
    if (!r) r = instruction_18(b, l + 1);
    if (!r) r = instruction_19(b, l + 1);
    if (!r) r = instruction_20(b, l + 1);
    if (!r) r = instruction_21(b, l + 1);
    if (!r) r = consumeToken(b, "MEM");
    if (!r) r = consumeToken(b, "GET");
    if (!r) r = consumeToken(b, "UPDATE");
    if (!r) r = instruction_25(b, l + 1);
    if (!r) r = instruction_26(b, l + 1);
    if (!r) r = instruction_27(b, l + 1);
    if (!r) r = instruction_28(b, l + 1);
    if (!r) r = consumeToken(b, "EXEC");
    if (!r) r = instruction_30(b, l + 1);
    if (!r) r = consumeToken(b, "FAILWITH");
    if (!r) r = consumeToken(b, "CAST");
    if (!r) r = consumeToken(b, "RENAME");
    if (!r) r = consumeToken(b, "CONCAT");
    if (!r) r = consumeToken(b, "ADD");
    if (!r) r = consumeToken(b, "SUB");
    if (!r) r = consumeToken(b, "MUL");
    if (!r) r = consumeToken(b, "DIV");
    if (!r) r = consumeToken(b, "ABS");
    if (!r) r = consumeToken(b, "NEG");
    if (!r) r = consumeToken(b, "MOD");
    if (!r) r = consumeToken(b, "LSL");
    if (!r) r = consumeToken(b, "LSR");
    if (!r) r = consumeToken(b, "OR");
    if (!r) r = consumeToken(b, "AND");
    if (!r) r = consumeToken(b, "XOR");
    if (!r) r = consumeToken(b, "NOT");
    if (!r) r = consumeToken(b, "COMPARE");
    if (!r) r = consumeToken(b, "EQ");
    if (!r) r = consumeToken(b, "NEQ");
    if (!r) r = consumeToken(b, "LT");
    if (!r) r = consumeToken(b, "GT");
    if (!r) r = consumeToken(b, "LE");
    if (!r) r = consumeToken(b, "GE");
    if (!r) r = consumeToken(b, "INT");
    if (!r) r = consumeToken(b, "SELF");
    if (!r) r = consumeToken(b, "TRANSFER_TOKENS");
    if (!r) r = consumeToken(b, "SET_DELEGATE");
    if (!r) r = consumeToken(b, "CREATE_ACCOUNT");
    if (!r) r = consumeToken(b, "CREATE_CONTRACT");
    if (!r) r = consumeToken(b, "IMPLICIT_ACCOUNT");
    if (!r) r = consumeToken(b, "NOW");
    if (!r) r = consumeToken(b, "AMOUNT");
    if (!r) r = consumeToken(b, "BALANCE");
    if (!r) r = consumeToken(b, "CHECK_SIGNATURE");
    if (!r) r = consumeToken(b, "BLAKE2B");
    if (!r) r = consumeToken(b, "HASH_KEY");
    if (!r) r = consumeToken(b, "STEPS_TO_QUOTA");
    if (!r) r = consumeToken(b, "SOURCE");
    if (!r) r = consumeToken(b, "SENDER");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'PUSH' type_toplevel data_toplevel
  private static boolean instruction_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "PUSH");
    r = r && type_toplevel(b, l + 1);
    r = r && data_toplevel(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'NONE' type_toplevel
  private static boolean instruction_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "NONE");
    r = r && type_toplevel(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'IF_NONE' instructions instructions
  private static boolean instruction_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_8")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "IF_NONE");
    r = r && instructions(b, l + 1);
    r = r && instructions(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'LEFT' type_toplevel
  private static boolean instruction_12(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_12")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "LEFT");
    r = r && type_toplevel(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'RIGHT' type_toplevel
  private static boolean instruction_13(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_13")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "RIGHT");
    r = r && type_toplevel(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'IF_LEFT' instructions instructions
  private static boolean instruction_14(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_14")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "IF_LEFT");
    r = r && instructions(b, l + 1);
    r = r && instructions(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'NIL' type_toplevel
  private static boolean instruction_15(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_15")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "NIL");
    r = r && type_toplevel(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'IF_CONS' instructions instructions
  private static boolean instruction_17(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_17")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "IF_CONS");
    r = r && instructions(b, l + 1);
    r = r && instructions(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'EMPTY_SET' type
  private static boolean instruction_18(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_18")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "EMPTY_SET");
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'EMPTY_MAP' comparable_type type
  private static boolean instruction_19(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_19")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "EMPTY_MAP");
    r = r && comparable_type(b, l + 1);
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'MAP' instructions
  private static boolean instruction_20(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_20")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "MAP");
    r = r && instructions(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'ITER' instructions
  private static boolean instruction_21(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_21")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "ITER");
    r = r && instructions(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'IF' instructions instructions
  private static boolean instruction_25(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_25")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "IF");
    r = r && instructions(b, l + 1);
    r = r && instructions(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'LOOP' instructions
  private static boolean instruction_26(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_26")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "LOOP");
    r = r && instructions(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'LOOP_LEFT' instructions
  private static boolean instruction_27(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_27")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "LOOP_LEFT");
    r = r && instructions(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'LAMBDA' type type instructions
  private static boolean instruction_28(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_28")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "LAMBDA");
    r = r && type(b, l + 1);
    r = r && type(b, l + 1);
    r = r && instructions(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'DIP' instructions
  private static boolean instruction_30(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instruction_30")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "DIP");
    r = r && instructions(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '{' (instruction (';' instruction)*)? '}'
  public static boolean instructions(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instructions")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INSTRUCTIONS, "<instructions>");
    r = consumeToken(b, "{");
    r = r && instructions_1(b, l + 1);
    r = r && consumeToken(b, "}");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (instruction (';' instruction)*)?
  private static boolean instructions_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instructions_1")) return false;
    instructions_1_0(b, l + 1);
    return true;
  }

  // instruction (';' instruction)*
  private static boolean instructions_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instructions_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = instruction(b, l + 1);
    r = r && instructions_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (';' instruction)*
  private static boolean instructions_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instructions_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!instructions_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "instructions_1_0_1", c)) break;
    }
    return true;
  }

  // ';' instruction
  private static boolean instructions_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instructions_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ";");
    r = r && instruction(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'parameter' type_toplevel
  public static boolean parameter_section(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_section")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER_SECTION, "<parameter section>");
    r = consumeToken(b, "parameter");
    r = r && type_toplevel(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'return' type_toplevel
  public static boolean return_section(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "return_section")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RETURN_SECTION, "<return section>");
    r = consumeToken(b, "return");
    r = r && type_toplevel(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // parameter_section | return_section | storage_section | code_section
  public static boolean section(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "section")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SECTION, "<section>");
    r = parameter_section(b, l + 1);
    if (!r) r = return_section(b, l + 1);
    if (!r) r = storage_section(b, l + 1);
    if (!r) r = code_section(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'storage' type_toplevel
  public static boolean storage_section(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "storage_section")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STORAGE_SECTION, "<storage section>");
    r = consumeToken(b, "storage");
    r = r && type_toplevel(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
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
  public static boolean type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE, "<type>");
    r = type_toplevel(b, l + 1);
    if (!r) r = type_1(b, l + 1);
    if (!r) r = type_2(b, l + 1);
    if (!r) r = type_3(b, l + 1);
    if (!r) r = type_4(b, l + 1);
    if (!r) r = type_5(b, l + 1);
    if (!r) r = type_6(b, l + 1);
    if (!r) r = type_7(b, l + 1);
    if (!r) r = type_8(b, l + 1);
    if (!r) r = type_9(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'option' type
  private static boolean type_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "option");
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'list' type
  private static boolean type_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "list");
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'set' comparable_type
  private static boolean type_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "set");
    r = r && comparable_type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'contract' type
  private static boolean type_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "contract");
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'pair' type type
  private static boolean type_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "pair");
    r = r && type(b, l + 1);
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'or' type type
  private static boolean type_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "or");
    r = r && type(b, l + 1);
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'lambda' type type
  private static boolean type_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_7")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "lambda");
    r = r && type(b, l + 1);
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'map' comparable_type type
  private static boolean type_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_8")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "map");
    r = r && comparable_type(b, l + 1);
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'big_map' comparable_type type
  private static boolean type_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_9")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "big_map");
    r = r && comparable_type(b, l + 1);
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '(' type ')'
  //   | 'key'
  //   | 'unit'
  //   | 'signature'
  //   | 'operation'
  public static boolean type_toplevel(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_toplevel")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_TOPLEVEL, "<type toplevel>");
    r = type_toplevel_0(b, l + 1);
    if (!r) r = consumeToken(b, "key");
    if (!r) r = consumeToken(b, "unit");
    if (!r) r = consumeToken(b, "signature");
    if (!r) r = consumeToken(b, "operation");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '(' type ')'
  private static boolean type_toplevel_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_toplevel_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "(");
    r = r && type(b, l + 1);
    r = r && consumeToken(b, ")");
    exit_section_(b, m, null, r);
    return r;
  }

}

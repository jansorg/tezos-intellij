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
    else if (type == CONTRACT) {
      result = contract(builder, 0);
    }
    else if (type == CREATE_CONTRACT_INSTRUCTION) {
      result = create_contract_instruction(builder, 0);
    }
    else if (type == DATA) {
      result = data(builder, 0);
    }
    else if (type == GENERIC_DATA) {
      result = generic_data(builder, 0);
    }
    else if (type == GENERIC_INSTRUCTION) {
      result = generic_instruction(builder, 0);
    }
    else if (type == GENERIC_TYPE) {
      result = generic_type(builder, 0);
    }
    else if (type == INSTRUCTION) {
      result = instruction(builder, 0);
    }
    else if (type == LITERAL_DATA) {
      result = literal_data(builder, 0);
    }
    else if (type == MACRO_INSTRUCTION) {
      result = macro_instruction(builder, 0);
    }
    else if (type == MAP_ENTRY_DATA) {
      result = map_entry_data(builder, 0);
    }
    else if (type == NESTED_DATA) {
      result = nested_data(builder, 0);
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
    create_token_set_(DATA, GENERIC_DATA, LITERAL_DATA, MAP_ENTRY_DATA,
      NESTED_DATA),
    create_token_set_(BLOCK_INSTRUCTION, CREATE_CONTRACT_INSTRUCTION, GENERIC_INSTRUCTION, INSTRUCTION,
      MACRO_INSTRUCTION),
    create_token_set_(COMPARABLE_TYPE, COMPLEX_TYPE, GENERIC_TYPE, NESTED_TYPE,
      TYPE),
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
  // block_instruction_inner
  public static boolean block_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "block_instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, BLOCK_INSTRUCTION, "<block instruction>");
    result = parse_instruction_block(builder, level + 1, instruction_parser_);
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
  // 'CREATE_CONTRACT' '{' contract '}'
  public static boolean create_contract_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "create_contract_instruction")) return false;
    boolean result, pinned;
    Marker marker = enter_section_(builder, level, _NONE_, CREATE_CONTRACT_INSTRUCTION, "<create contract instruction>");
    result = consumeToken(builder, "CREATE_CONTRACT");
    result = result && consumeToken(builder, LEFT_CURLY);
    pinned = result; // pin = 2
    result = result && report_error_(builder, contract(builder, level + 1));
    result = pinned && consumeToken(builder, RIGHT_CURLY) && result;
    exit_section_(builder, level, marker, result, pinned, null);
    return result || pinned;
  }

  /* ********************************************************** */
  // toplevel_data
  //   | generic_data
  //   | instruction
  public static boolean data(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, DATA, "<data>");
    result = toplevel_data(builder, level + 1);
    if (!result) result = generic_data(builder, level + 1);
    if (!result) result = instruction(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // 'Left' toplevel_data
  static boolean data_left(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_left")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Left");
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // '{' (toplevel_data (';' toplevel_data)*)? '}'
  static boolean data_list(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list")) return false;
    if (!nextTokenIs(builder, LEFT_CURLY)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_CURLY);
    result = result && data_list_1(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_CURLY);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (toplevel_data (';' toplevel_data)*)?
  private static boolean data_list_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_1")) return false;
    data_list_1_0(builder, level + 1);
    return true;
  }

  // toplevel_data (';' toplevel_data)*
  private static boolean data_list_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = toplevel_data(builder, level + 1);
    result = result && data_list_1_0_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (';' toplevel_data)*
  private static boolean data_list_1_0_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_1_0_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!data_list_1_0_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "data_list_1_0_1", pos)) break;
    }
    return true;
  }

  // ';' toplevel_data
  private static boolean data_list_1_0_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_1_0_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, SEMI);
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // '{' map_entry_data (';' map_entry_data)* '}'
  static boolean data_map(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_map")) return false;
    if (!nextTokenIs(builder, LEFT_CURLY)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_CURLY);
    result = result && map_entry_data(builder, level + 1);
    result = result && data_map_2(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_CURLY);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (';' map_entry_data)*
  private static boolean data_map_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_map_2")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!data_map_2_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "data_map_2", pos)) break;
    }
    return true;
  }

  // ';' map_entry_data
  private static boolean data_map_2_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_map_2_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, SEMI);
    result = result && map_entry_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // 'Pair' toplevel_data toplevel_data
  static boolean data_pair(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_pair")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Pair");
    result = result && toplevel_data(builder, level + 1);
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // 'Right' toplevel_data
  static boolean data_right(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_right")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Right");
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // 'Some' toplevel_data
  static boolean data_some(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_some")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "Some");
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // data_pair | data_left | data_right | data_some | data_list | data_map
  public static boolean generic_data(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_data")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, GENERIC_DATA, "<generic data>");
    result = data_pair(builder, level + 1);
    if (!result) result = data_left(builder, level + 1);
    if (!result) result = data_right(builder, level + 1);
    if (!result) result = data_some(builder, level + 1);
    if (!result) result = data_list(builder, level + 1);
    if (!result) result = data_map(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // INSTRUCTION_TOKEN (block_instruction | toplevel_type | toplevel_data | annotation)*
  public static boolean generic_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_instruction")) return false;
    boolean result, pinned;
    Marker marker = enter_section_(builder, level, _NONE_, GENERIC_INSTRUCTION, "<generic instruction>");
    result = consumeToken(builder, INSTRUCTION_TOKEN);
    pinned = result; // pin = 1
    result = result && generic_instruction_1(builder, level + 1);
    exit_section_(builder, level, marker, result, pinned, instruction_recover_parser_);
    return result || pinned;
  }

  // (block_instruction | toplevel_type | toplevel_data | annotation)*
  private static boolean generic_instruction_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_instruction_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!generic_instruction_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "generic_instruction_1", pos)) break;
    }
    return true;
  }

  // block_instruction | toplevel_type | toplevel_data | annotation
  private static boolean generic_instruction_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_instruction_1_0")) return false;
    boolean result;
    result = block_instruction(builder, level + 1);
    if (!result) result = toplevel_type(builder, level + 1);
    if (!result) result = toplevel_data(builder, level + 1);
    if (!result) result = annotation(builder, level + 1);
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
  // block_instruction | simple_instruction
  public static boolean instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, INSTRUCTION, "<instruction>");
    result = block_instruction(builder, level + 1);
    if (!result) result = simple_instruction(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // <<instruction_recover_while>>
  static boolean instruction_recover(PsiBuilder builder, int level) {
    return instruction_recover_while(builder, level + 1);
  }

  /* ********************************************************** */
  // 'None' | 'True' | 'False' | 'Unit' | INT | STRING
  public static boolean literal_data(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "literal_data")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, LITERAL_DATA, "<literal data>");
    result = consumeToken(builder, NONE);
    if (!result) result = consumeToken(builder, TRUE);
    if (!result) result = consumeToken(builder, FALSE);
    if (!result) result = consumeToken(builder, UNIT);
    if (!result) result = consumeToken(builder, INT);
    if (!result) result = consumeToken(builder, STRING);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // MACRO_TOKEN (annotation | block_instruction)*
  public static boolean macro_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "macro_instruction")) return false;
    boolean result, pinned;
    Marker marker = enter_section_(builder, level, _NONE_, MACRO_INSTRUCTION, "<macro instruction>");
    result = consumeToken(builder, MACRO_TOKEN);
    pinned = result; // pin = 1
    result = result && macro_instruction_1(builder, level + 1);
    exit_section_(builder, level, marker, result, pinned, instruction_recover_parser_);
    return result || pinned;
  }

  // (annotation | block_instruction)*
  private static boolean macro_instruction_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "macro_instruction_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!macro_instruction_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "macro_instruction_1", pos)) break;
    }
    return true;
  }

  // annotation | block_instruction
  private static boolean macro_instruction_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "macro_instruction_1_0")) return false;
    boolean result;
    result = annotation(builder, level + 1);
    if (!result) result = block_instruction(builder, level + 1);
    return result;
  }

  /* ********************************************************** */
  // 'Elt' toplevel_data toplevel_data
  public static boolean map_entry_data(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "map_entry_data")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, MAP_ENTRY_DATA, "<map entry data>");
    result = consumeToken(builder, "Elt");
    result = result && toplevel_data(builder, level + 1);
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // '(' data ')'
  public static boolean nested_data(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "nested_data")) return false;
    if (!nextTokenIs(builder, LEFT_PAREN)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_PAREN);
    result = result && data(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_PAREN);
    exit_section_(builder, marker, NESTED_DATA, result);
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
  // create_contract_instruction
  //   | generic_instruction
  //   | macro_instruction
  static boolean simple_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "simple_instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = create_contract_instruction(builder, level + 1);
    if (!result) result = generic_instruction(builder, level + 1);
    if (!result) result = macro_instruction(builder, level + 1);
    exit_section_(builder, marker, null, result);
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
  // nested_data | literal_data
  static boolean toplevel_data(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "toplevel_data")) return false;
    boolean result;
    result = nested_data(builder, level + 1);
    if (!result) result = literal_data(builder, level + 1);
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

  final static Parser instruction_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder, int level) {
      return instruction(builder, level + 1);
    }
  };
  final static Parser instruction_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder, int level) {
      return instruction_recover(builder, level + 1);
    }
  };
}

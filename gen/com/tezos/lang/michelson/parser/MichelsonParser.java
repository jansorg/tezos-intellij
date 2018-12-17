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
    else if (type == ANNOTATION_LIST) {
      result = annotation_list(builder, 0);
    }
    else if (type == BLOCK_INSTRUCTION) {
      result = block_instruction(builder, 0);
    }
    else if (type == CODE_SECTION) {
      result = code_section(builder, 0);
    }
    else if (type == COMPLEX_TYPE) {
      result = complex_type(builder, 0);
    }
    else if (type == CONTRACT) {
      result = contract(builder, 0);
    }
    else if (type == CONTRACT_WRAPPER) {
      result = contract_wrapper(builder, 0);
    }
    else if (type == CREATE_CONTRACT_INSTRUCTION) {
      result = create_contract_instruction(builder, 0);
    }
    else if (type == DATA_LIST) {
      result = data_list(builder, 0);
    }
    else if (type == DATA_MAP) {
      result = data_map(builder, 0);
    }
    else if (type == EMPTY_BLOCK) {
      result = empty_block(builder, 0);
    }
    else if (type == FIELD_ANNOTATION) {
      result = field_annotation(builder, 0);
    }
    else if (type == GENERIC_INSTRUCTION) {
      result = generic_instruction(builder, 0);
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
    else if (type == MAP_ENTRY) {
      result = map_entry(builder, 0);
    }
    else if (type == SECTION) {
      result = section(builder, 0);
    }
    else if (type == SIMPLE_TYPE) {
      result = simple_type(builder, 0);
    }
    else if (type == STRING_LITERAL) {
      result = string_literal(builder, 0);
    }
    else if (type == TAG) {
      result = tag(builder, 0);
    }
    else if (type == TYPE) {
      result = type(builder, 0);
    }
    else if (type == TYPE_ANNOTATION) {
      result = type_annotation(builder, 0);
    }
    else if (type == TYPE_SECTION) {
      result = type_section(builder, 0);
    }
    else if (type == UNKNOWN_SECTION) {
      result = unknown_section(builder, 0);
    }
    else if (type == VARIABLE_ANNOTATION) {
      result = variable_annotation(builder, 0);
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
    create_token_set_(COMPLEX_TYPE, SIMPLE_TYPE, TYPE),
    create_token_set_(CODE_SECTION, SECTION, TYPE_SECTION, UNKNOWN_SECTION),
    create_token_set_(ANNOTATION, FIELD_ANNOTATION, TYPE_ANNOTATION, VARIABLE_ANNOTATION),
    create_token_set_(DATA_LIST, DATA_MAP, LITERAL_DATA, MAP_ENTRY,
      STRING_LITERAL, TAG),
    create_token_set_(BLOCK_INSTRUCTION, CREATE_CONTRACT_INSTRUCTION, EMPTY_BLOCK, GENERIC_INSTRUCTION,
      INSTRUCTION, MACRO_INSTRUCTION),
  };

  /* ********************************************************** */
  // type_annotation | variable_annotation | field_annotation
  public static boolean annotation(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "annotation")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, ANNOTATION, "<annotation>");
    result = type_annotation(builder, level + 1);
    if (!result) result = variable_annotation(builder, level + 1);
    if (!result) result = field_annotation(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // annotation+
  public static boolean annotation_list(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "annotation_list")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, ANNOTATION_LIST, "<annotation list>");
    result = annotation(builder, level + 1);
    while (result) {
      int pos = current_position_(builder);
      if (!annotation(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "annotation_list", pos)) break;
    }
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // <<parse_instruction_block instruction>>
  public static boolean block_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "block_instruction")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, BLOCK_INSTRUCTION, "<block instruction>");
    result = parse_instruction_block(builder, level + 1, instruction_parser_);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // 'code' instruction
  public static boolean code_section(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "code_section")) return false;
    boolean result, pinned;
    Marker marker = enter_section_(builder, level, _NONE_, CODE_SECTION, "<code section>");
    result = consumeToken(builder, "code");
    pinned = result; // pin = 1
    result = result && instruction(builder, level + 1);
    exit_section_(builder, level, marker, result, pinned, null);
    return result || pinned;
  }

  /* ********************************************************** */
  // '(' (TYPE_NAME | TYPE_NAME_COMPARABLE) annotation_list? type* ')'
  public static boolean complex_type(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "complex_type")) return false;
    if (!nextTokenIs(builder, "<type>", LEFT_PAREN)) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, COMPLEX_TYPE, "<type>");
    result = consumeToken(builder, LEFT_PAREN);
    result = result && complex_type_1(builder, level + 1);
    result = result && complex_type_2(builder, level + 1);
    result = result && complex_type_3(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_PAREN);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // TYPE_NAME | TYPE_NAME_COMPARABLE
  private static boolean complex_type_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "complex_type_1")) return false;
    boolean result;
    result = consumeToken(builder, TYPE_NAME);
    if (!result) result = consumeToken(builder, TYPE_NAME_COMPARABLE);
    return result;
  }

  // annotation_list?
  private static boolean complex_type_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "complex_type_2")) return false;
    annotation_list(builder, level + 1);
    return true;
  }

  // type*
  private static boolean complex_type_3(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "complex_type_3")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!type(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "complex_type_3", pos)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // sections
  public static boolean contract(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "contract")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, CONTRACT, "<contract>");
    result = sections(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // '{' contract '}'
  public static boolean contract_wrapper(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "contract_wrapper")) return false;
    if (!nextTokenIs(builder, LEFT_CURLY)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_CURLY);
    result = result && contract(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_CURLY);
    exit_section_(builder, marker, CONTRACT_WRAPPER, result);
    return result;
  }

  /* ********************************************************** */
  // 'CREATE_CONTRACT' annotation_list? contract_wrapper?
  public static boolean create_contract_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "create_contract_instruction")) return false;
    boolean result, pinned;
    Marker marker = enter_section_(builder, level, _NONE_, CREATE_CONTRACT_INSTRUCTION, "<create contract instruction>");
    result = consumeToken(builder, "CREATE_CONTRACT");
    pinned = result; // pin = 1
    result = result && report_error_(builder, create_contract_instruction_1(builder, level + 1));
    result = pinned && create_contract_instruction_2(builder, level + 1) && result;
    exit_section_(builder, level, marker, result, pinned, null);
    return result || pinned;
  }

  // annotation_list?
  private static boolean create_contract_instruction_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "create_contract_instruction_1")) return false;
    annotation_list(builder, level + 1);
    return true;
  }

  // contract_wrapper?
  private static boolean create_contract_instruction_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "create_contract_instruction_2")) return false;
    contract_wrapper(builder, level + 1);
    return true;
  }

  /* ********************************************************** */
  // '{' ';'? toplevel_data (';' (toplevel_data | &'}'))* '}'
  public static boolean data_list(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list")) return false;
    if (!nextTokenIs(builder, LEFT_CURLY)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_CURLY);
    result = result && data_list_1(builder, level + 1);
    result = result && toplevel_data(builder, level + 1);
    result = result && data_list_3(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_CURLY);
    exit_section_(builder, marker, DATA_LIST, result);
    return result;
  }

  // ';'?
  private static boolean data_list_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_1")) return false;
    consumeToken(builder, SEMI);
    return true;
  }

  // (';' (toplevel_data | &'}'))*
  private static boolean data_list_3(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_3")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!data_list_3_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "data_list_3", pos)) break;
    }
    return true;
  }

  // ';' (toplevel_data | &'}')
  private static boolean data_list_3_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_3_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, SEMI);
    result = result && data_list_3_0_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // toplevel_data | &'}'
  private static boolean data_list_3_0_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_3_0_1")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = toplevel_data(builder, level + 1);
    if (!result) result = data_list_3_0_1_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // &'}'
  private static boolean data_list_3_0_1_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_list_3_0_1_1")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _AND_);
    result = consumeToken(builder, RIGHT_CURLY);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // '{' ';'? map_entry (';' (map_entry | &'}'))* '}'
  public static boolean data_map(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_map")) return false;
    if (!nextTokenIs(builder, LEFT_CURLY)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_CURLY);
    result = result && data_map_1(builder, level + 1);
    result = result && map_entry(builder, level + 1);
    result = result && data_map_3(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_CURLY);
    exit_section_(builder, marker, DATA_MAP, result);
    return result;
  }

  // ';'?
  private static boolean data_map_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_map_1")) return false;
    consumeToken(builder, SEMI);
    return true;
  }

  // (';' (map_entry | &'}'))*
  private static boolean data_map_3(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_map_3")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!data_map_3_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "data_map_3", pos)) break;
    }
    return true;
  }

  // ';' (map_entry | &'}')
  private static boolean data_map_3_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_map_3_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, SEMI);
    result = result && data_map_3_0_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // map_entry | &'}'
  private static boolean data_map_3_0_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_map_3_0_1")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = map_entry(builder, level + 1);
    if (!result) result = data_map_3_0_1_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // &'}'
  private static boolean data_map_3_0_1_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "data_map_3_0_1_1")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _AND_);
    result = consumeToken(builder, RIGHT_CURLY);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // '{' ';'? '}'
  public static boolean empty_block(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "empty_block")) return false;
    if (!nextTokenIs(builder, LEFT_CURLY)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, LEFT_CURLY);
    result = result && empty_block_1(builder, level + 1);
    result = result && consumeToken(builder, RIGHT_CURLY);
    exit_section_(builder, marker, EMPTY_BLOCK, result);
    return result;
  }

  // ';'?
  private static boolean empty_block_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "empty_block_1")) return false;
    consumeToken(builder, SEMI);
    return true;
  }

  /* ********************************************************** */
  // FIELD_ANNOTATION_TOKEN
  public static boolean field_annotation(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "field_annotation")) return false;
    if (!nextTokenIs(builder, FIELD_ANNOTATION_TOKEN)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, FIELD_ANNOTATION_TOKEN);
    exit_section_(builder, marker, FIELD_ANNOTATION, result);
    return result;
  }

  /* ********************************************************** */
  // INSTRUCTION_TOKEN annotation_list? (toplevel_data | type | block_instruction)*
  public static boolean generic_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_instruction")) return false;
    boolean result, pinned;
    Marker marker = enter_section_(builder, level, _NONE_, GENERIC_INSTRUCTION, "<generic instruction>");
    result = consumeToken(builder, INSTRUCTION_TOKEN);
    pinned = result; // pin = 1
    result = result && report_error_(builder, generic_instruction_1(builder, level + 1));
    result = pinned && generic_instruction_2(builder, level + 1) && result;
    exit_section_(builder, level, marker, result, pinned, instruction_recover_parser_);
    return result || pinned;
  }

  // annotation_list?
  private static boolean generic_instruction_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_instruction_1")) return false;
    annotation_list(builder, level + 1);
    return true;
  }

  // (toplevel_data | type | block_instruction)*
  private static boolean generic_instruction_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_instruction_2")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!generic_instruction_2_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "generic_instruction_2", pos)) break;
    }
    return true;
  }

  // toplevel_data | type | block_instruction
  private static boolean generic_instruction_2_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "generic_instruction_2_0")) return false;
    boolean result;
    result = toplevel_data(builder, level + 1);
    if (!result) result = type(builder, level + 1);
    if (!result) result = block_instruction(builder, level + 1);
    return result;
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
  // INT | BYTE | string_literal
  public static boolean literal_data(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "literal_data")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, LITERAL_DATA, "<literal data>");
    result = consumeToken(builder, INT);
    if (!result) result = consumeToken(builder, BYTE);
    if (!result) result = string_literal(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // MACRO_TOKEN annotation_list? block_instruction*
  public static boolean macro_instruction(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "macro_instruction")) return false;
    boolean result, pinned;
    Marker marker = enter_section_(builder, level, _NONE_, MACRO_INSTRUCTION, "<macro instruction>");
    result = consumeToken(builder, MACRO_TOKEN);
    pinned = result; // pin = 1
    result = result && report_error_(builder, macro_instruction_1(builder, level + 1));
    result = pinned && macro_instruction_2(builder, level + 1) && result;
    exit_section_(builder, level, marker, result, pinned, instruction_recover_parser_);
    return result || pinned;
  }

  // annotation_list?
  private static boolean macro_instruction_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "macro_instruction_1")) return false;
    annotation_list(builder, level + 1);
    return true;
  }

  // block_instruction*
  private static boolean macro_instruction_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "macro_instruction_2")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!block_instruction(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "macro_instruction_2", pos)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'Elt' toplevel_data toplevel_data
  public static boolean map_entry(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "map_entry")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, MAP_ENTRY, "<map entry>");
    result = consumeToken(builder, "Elt");
    result = result && toplevel_data(builder, level + 1);
    result = result && toplevel_data(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // <<parseNestedData toplevel_data>>
  static boolean nested_data(PsiBuilder builder, int level) {
    return parseNestedData(builder, level + 1, toplevel_data_parser_);
  }

  /* ********************************************************** */
  // contract?
  static boolean script_file(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "script_file")) return false;
    contract(builder, level + 1);
    return true;
  }

  /* ********************************************************** */
  // type_section | code_section
  public static boolean section(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "section")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, SECTION, "<section>");
    result = type_section(builder, level + 1);
    if (!result) result = code_section(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // <<parse_section_error_aware section unknown_section>>
  static boolean section_parser(PsiBuilder builder, int level) {
    return parse_section_error_aware(builder, level + 1, section_parser_, unknown_section_parser_);
  }

  /* ********************************************************** */
  // section_parser (';' section_parser)* ';'?
  static boolean sections(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "sections")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = section_parser(builder, level + 1);
    result = result && sections_1(builder, level + 1);
    result = result && sections_2(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // (';' section_parser)*
  private static boolean sections_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "sections_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!sections_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "sections_1", pos)) break;
    }
    return true;
  }

  // ';' section_parser
  private static boolean sections_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "sections_1_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, SEMI);
    result = result && section_parser(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // ';'?
  private static boolean sections_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "sections_2")) return false;
    consumeToken(builder, SEMI);
    return true;
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
  // TYPE_NAME | TYPE_NAME_COMPARABLE
  public static boolean simple_type(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "simple_type")) return false;
    if (!nextTokenIs(builder, "<type>", TYPE_NAME, TYPE_NAME_COMPARABLE)) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, SIMPLE_TYPE, "<type>");
    result = consumeToken(builder, TYPE_NAME);
    if (!result) result = consumeToken(builder, TYPE_NAME_COMPARABLE);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // '"' (STRING_CONTENT | STRING_ESCAPE | STRING_ESCAPE_INVALID)* '"'
  public static boolean string_literal(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "string_literal")) return false;
    if (!nextTokenIs(builder, QUOTE)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, QUOTE);
    result = result && string_literal_1(builder, level + 1);
    result = result && consumeToken(builder, QUOTE);
    exit_section_(builder, marker, STRING_LITERAL, result);
    return result;
  }

  // (STRING_CONTENT | STRING_ESCAPE | STRING_ESCAPE_INVALID)*
  private static boolean string_literal_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "string_literal_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!string_literal_1_0(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "string_literal_1", pos)) break;
    }
    return true;
  }

  // STRING_CONTENT | STRING_ESCAPE | STRING_ESCAPE_INVALID
  private static boolean string_literal_1_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "string_literal_1_0")) return false;
    boolean result;
    result = consumeToken(builder, STRING_CONTENT);
    if (!result) result = consumeToken(builder, STRING_ESCAPE);
    if (!result) result = consumeToken(builder, STRING_ESCAPE_INVALID);
    return result;
  }

  /* ********************************************************** */
  // 'Unit' | 'True' | 'False' | 'None' | TAG_TOKEN toplevel_data*
  public static boolean tag(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "tag")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, TAG, "<tag>");
    result = consumeToken(builder, "Unit");
    if (!result) result = consumeToken(builder, "True");
    if (!result) result = consumeToken(builder, "False");
    if (!result) result = consumeToken(builder, "None");
    if (!result) result = tag_4(builder, level + 1);
    exit_section_(builder, level, marker, result, false, tag_recovery_parser_);
    return result;
  }

  // TAG_TOKEN toplevel_data*
  private static boolean tag_4(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "tag_4")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, TAG_TOKEN);
    result = result && tag_4_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // toplevel_data*
  private static boolean tag_4_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "tag_4_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!toplevel_data(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "tag_4_1", pos)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // <<tag_data_recover_while>>
  static boolean tag_recovery(PsiBuilder builder, int level) {
    return tag_data_recover_while(builder, level + 1);
  }

  /* ********************************************************** */
  // literal_data | tag | data_map | data_list | empty_block | nested_data
  static boolean toplevel_data(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "toplevel_data")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = literal_data(builder, level + 1);
    if (!result) result = tag(builder, level + 1);
    if (!result) result = data_map(builder, level + 1);
    if (!result) result = data_list(builder, level + 1);
    if (!result) result = empty_block(builder, level + 1);
    if (!result) result = nested_data(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // simple_type | complex_type
  public static boolean type(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, TYPE, "<type>");
    result = simple_type(builder, level + 1);
    if (!result) result = complex_type(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // TYPE_ANNOTATION_TOKEN
  public static boolean type_annotation(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_annotation")) return false;
    if (!nextTokenIs(builder, TYPE_ANNOTATION_TOKEN)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, TYPE_ANNOTATION_TOKEN);
    exit_section_(builder, marker, TYPE_ANNOTATION, result);
    return result;
  }

  /* ********************************************************** */
  // ('parameter' | 'storage') type
  public static boolean type_section(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_section")) return false;
    boolean result, pinned;
    Marker marker = enter_section_(builder, level, _NONE_, TYPE_SECTION, "<type section>");
    result = type_section_0(builder, level + 1);
    pinned = result; // pin = 1
    result = result && type(builder, level + 1);
    exit_section_(builder, level, marker, result, pinned, null);
    return result || pinned;
  }

  // 'parameter' | 'storage'
  private static boolean type_section_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "type_section_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, "parameter");
    if (!result) result = consumeToken(builder, "storage");
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  public static boolean unknown_section(PsiBuilder builder, int level) {
    Marker marker = enter_section_(builder);
    exit_section_(builder, marker, UNKNOWN_SECTION, true);
    return true;
  }

  /* ********************************************************** */
  // VAR_ANNOTATION_TOKEN
  public static boolean variable_annotation(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "variable_annotation")) return false;
    if (!nextTokenIs(builder, VAR_ANNOTATION_TOKEN)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, VAR_ANNOTATION_TOKEN);
    exit_section_(builder, marker, VARIABLE_ANNOTATION, result);
    return result;
  }

  static final Parser instruction_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder, int level) {
      return instruction(builder, level + 1);
    }
  };
  static final Parser instruction_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder, int level) {
      return instruction_recover(builder, level + 1);
    }
  };
  static final Parser section_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder, int level) {
      return section(builder, level + 1);
    }
  };
  static final Parser tag_recovery_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder, int level) {
      return tag_recovery(builder, level + 1);
    }
  };
  static final Parser toplevel_data_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder, int level) {
      return toplevel_data(builder, level + 1);
    }
  };
  static final Parser unknown_section_parser_ = new Parser() {
    public boolean parse(PsiBuilder builder, int level) {
      return unknown_section(builder, level + 1);
    }
  };
}

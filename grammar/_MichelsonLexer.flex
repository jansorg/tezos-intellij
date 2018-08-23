package com.tezos.lang.michelson.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.tezos.lang.michelson.MichelsonTypes.*;

%%

%{
  public _MichelsonLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _MichelsonLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

SECTION_NAME=parameter|return|storage|code
INT=-?[0-9]+
STRING=\"[^\"]*\"
BYTE=0x[A-F0-9]+
TAG=[A-Z][a-z]+
NAME=[a-z]+
MACRO_TOKEN=CMPEQ | CMPNEQ | CMPLT | CMPGT | CMPLE | CMPGE | FAIL | ASSERT | ASSERT_EQ | ASSERT_NEQ | ASSERT_LT | ASSERT_LE | ASSERT_GT | ASSERT_GE | ASSERT_CMPEQ | ASSERT_CMPNEQ |ASSERT_CMPLT | ASSERT_CMPLE | ASSERT_CMPGT | ASSERT_CMPGE | ASSERT_NONE | ASSERT_SOME | ASSERT_LEFT | ASSERT_RIGHT | SET_CAR | SET_CDR | MAP_CAR | MAP_CDR
MACRO_DIIP_TOKEN=DII+P
MACRO_DUUP_TOKEN=DUU+P
MACRO_PAIRS_TOKEN=P(A | I | P)+R
MACRO_NESTED_TOKEN=UNP(A | I | R)*R
MACRO_PAIR_ACCESS_TOKEN=C[AD]+R
MACRO_SET_CADR_TOKEN=SET_C[AD]+R
MACRO_MAP_CADR_TOKEN=MAP_C[AD]+R
INSTRUCTION_TOKEN=[A-Z][A-Z_0-9]*
COMMENT_LINE=#.*
COMMENT_MULTI_LINE="/"\* ~\*"/"

%%
<YYINITIAL> {
  {WHITE_SPACE}                  { return WHITE_SPACE; }

  "("                            { return LEFT_PAREN; }
  ")"                            { return RIGHT_PAREN; }
  "{"                            { return LEFT_CURLY; }
  "}"                            { return RIGHT_CURLY; }
  ";"                            { return SEMI; }
  "True"                         { return TRUE; }
  "False"                        { return FALSE; }

  {SECTION_NAME}                 { return SECTION_NAME; }
  {INT}                          { return INT; }
  {STRING}                       { return STRING; }
  {BYTE}                         { return BYTE; }
  {TAG}                          { return TAG; }
  {NAME}                         { return NAME; }
  {MACRO_TOKEN}                  { return MACRO_TOKEN; }
  {MACRO_DIIP_TOKEN}             { return MACRO_DIIP_TOKEN; }
  {MACRO_DUUP_TOKEN}             { return MACRO_DUUP_TOKEN; }
  {MACRO_PAIRS_TOKEN}            { return MACRO_PAIRS_TOKEN; }
  {MACRO_NESTED_TOKEN}           { return MACRO_NESTED_TOKEN; }
  {MACRO_PAIR_ACCESS_TOKEN}      { return MACRO_PAIR_ACCESS_TOKEN; }
  {MACRO_SET_CADR_TOKEN}         { return MACRO_SET_CADR_TOKEN; }
  {MACRO_MAP_CADR_TOKEN}         { return MACRO_MAP_CADR_TOKEN; }
  {INSTRUCTION_TOKEN}            { return INSTRUCTION_TOKEN; }
  {COMMENT_LINE}                 { return COMMENT_LINE; }
  {COMMENT_MULTI_LINE}           { return COMMENT_MULTI_LINE; }

}

[^] { return BAD_CHARACTER; }

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
TYPE_NAME_COMPARABLE=int | nat | string | tez | bool | key_hash | timestamp
TYPE_NAME=[a-z]+
INT=-?[0-9]+
STRING=\"[^\"]*\"
BYTE=0x[A-F0-9]+
TAG=[A-Z][a-z]+
MACRO_TOKEN= (CMPEQ | CMPNEQ | CMPLT | CMPGT | CMPLE | CMPGE | FAIL | ASSERT | ASSERT_EQ | ASSERT_NEQ | ASSERT_LT | ASSERT_LE | ASSERT_GT | ASSERT_GE | ASSERT_CMPEQ | ASSERT_CMPNEQ |ASSERT_CMPLT | ASSERT_CMPLE | ASSERT_CMPGT | ASSERT_CMPGE | ASSERT_NONE | ASSERT_SOME | ASSERT_LEFT | ASSERT_RIGHT | SET_CAR | SET_CDR | MAP_CAR | MAP_CDR) | DII+P | DUU+P | P(A | I | P)+R | UNP(A | I | R)*R | C[AD]+R | SET_C[AD]+R | MAP_C[AD]+R
INSTRUCTION_TOKEN=[A-Z][A-Z_0-9]*
ANNOTATION_TOKEN=[@:%](@|%|%%|[_a-zA-Z][_0-9a-zA-Z.]*)?
COMMENT_LINE=#.*
COMMENT_MULTI_LINE="/"\* ~\*"/"

%%
<YYINITIAL> {
  {WHITE_SPACE}               { return WHITE_SPACE; }

  "("                         { return LEFT_PAREN; }
  ")"                         { return RIGHT_PAREN; }
  "{"                         { return LEFT_CURLY; }
  "}"                         { return RIGHT_CURLY; }
  ";"                         { return SEMI; }
  "True"                      { return TRUE; }
  "False"                     { return FALSE; }

  {SECTION_NAME}              { return SECTION_NAME; }
  {TYPE_NAME_COMPARABLE}      { return TYPE_NAME_COMPARABLE; }
  {TYPE_NAME}                 { return TYPE_NAME; }
  {INT}                       { return INT; }
  {STRING}                    { return STRING; }
  {BYTE}                      { return BYTE; }
  {TAG}                       { return TAG; }
  {MACRO_TOKEN}               { return MACRO_TOKEN; }
  {INSTRUCTION_TOKEN}         { return INSTRUCTION_TOKEN; }
  {ANNOTATION_TOKEN}          { return ANNOTATION_TOKEN; }
  {COMMENT_LINE}              { return COMMENT_LINE; }
  {COMMENT_MULTI_LINE}        { return COMMENT_MULTI_LINE; }

}

[^] { return BAD_CHARACTER; }

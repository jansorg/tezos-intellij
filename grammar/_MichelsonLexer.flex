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

// exclusive state to contain rules which matches content between quotes, i.e. "..."
%xstate S_STRING

EOL=\R
WHITE_SPACE=\s+

SECTION_NAME=parameter|return|storage|code
TYPE_NAME_COMPARABLE=int | nat | string | tez | bool | key_hash | timestamp | bytes | mutez | address
TYPE_NAME=[a-z_]+
INT=-?[0-9]+
BYTE=0x[A-F0-9]+
TAG=[A-Z][a-z]+
STRING_CONTENT=[^\"\\]+
STRING_ESCAPE=\\[ntbr\\\"]
STRING_ESCAPE_INVALID=\\[^ntbr\\\"]
MACRO_TOKEN= (CMPEQ | CMPNEQ | CMPLT | CMPGT | CMPLE | CMPGE | IFEQ | IFNEQ | IFLT | IFGT | IFLE | IFGE | IFCMPEQ | IFCMPNEQ | IFCMPLT | IFCMPGT | IFCMPLE | IFCMPGE | FAIL | ASSERT | ASSERT_EQ | ASSERT_NEQ | ASSERT_LT | ASSERT_LE | ASSERT_GT | ASSERT_GE | ASSERT_CMPEQ | ASSERT_CMPNEQ |ASSERT_CMPLT | ASSERT_CMPLE | ASSERT_CMPGT | ASSERT_CMPGE | ASSERT_NONE | ASSERT_SOME | ASSERT_LEFT | ASSERT_RIGHT | SET_CAR | SET_CDR | MAP_CAR | MAP_CDR | IF_SOME) | DII+P | DUU+P | P[AIP]+R | UNP[PAI]+R | C[AD]+R | SET_C[AD]+R | MAP_C[AD]+R
INSTRUCTION_TOKEN=[A-Z][A-Z_0-9]*
TYPE_ANNOTATION_TOKEN=:(@|%|%%|[_a-zA-Z][_0-9a-zA-Z.]*)?
VAR_ANNOTATION_TOKEN=@(@|%|%%|[_a-zA-Z][_0-9a-zA-Z.]*)?
FIELD_ANNOTATION_TOKEN=%(@|%|%%|[_a-zA-Z][_0-9a-zA-Z.]*)?
COMMENT_LINE=#.*
COMMENT_MULTI_LINE="/"\* ~\*"/"

%%

<S_STRING> {
  {STRING_ESCAPE}             { return STRING_ESCAPE; }
  {STRING_ESCAPE_INVALID}     { return STRING_ESCAPE_INVALID; }
  {STRING_CONTENT}            { return STRING_CONTENT; }
  "\""                        { yybegin(YYINITIAL); return QUOTE; }
}

<YYINITIAL> {
  {WHITE_SPACE}                 { return WHITE_SPACE; }

  "("                           { return LEFT_PAREN; }
  ")"                           { return RIGHT_PAREN; }
  "{"                           { return LEFT_CURLY; }
  "}"                           { return RIGHT_CURLY; }
  ";"                           { return SEMI; }
  "\""                          { yybegin(S_STRING); return QUOTE; }
  "True"                        { return TRUE; }
  "False"                       { return FALSE; }
  "Unit"                        { return UNIT; }
  "None"                        { return NONE; }

  {SECTION_NAME}                { return SECTION_NAME; }
  {TYPE_NAME_COMPARABLE}        { return TYPE_NAME_COMPARABLE; }
  {TYPE_NAME}                   { return TYPE_NAME; }
  {INT}                         { return INT; }
  {BYTE}                        { return BYTE; }
  {TAG}                         { return TAG; }
  {MACRO_TOKEN}                 { return MACRO_TOKEN; }
  {INSTRUCTION_TOKEN}           { return INSTRUCTION_TOKEN; }
  {TYPE_ANNOTATION_TOKEN}       { return TYPE_ANNOTATION_TOKEN; }
  {VAR_ANNOTATION_TOKEN}        { return VAR_ANNOTATION_TOKEN; }
  {FIELD_ANNOTATION_TOKEN}      { return FIELD_ANNOTATION_TOKEN; }
  {COMMENT_LINE}                { return COMMENT_LINE; }
  {COMMENT_MULTI_LINE}          { return COMMENT_MULTI_LINE; }

}

[^] { return BAD_CHARACTER; }

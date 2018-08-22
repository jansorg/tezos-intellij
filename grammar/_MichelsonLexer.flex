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

INT=-?[0-9]+
STRING=\"[^\"]*\"
BYTE=0x[A-F0-9]+
TAG=[A-Z][a-z]+
NAME=[a-z]+
INSTRUCTION_TOKEN=[A-Z][A-Z_0-9]*
COMMENT_LINE=#.*
COMMENT_MULTI_LINE="/"\* ~\*"/"

%%
<YYINITIAL> {
  {WHITE_SPACE}             { return WHITE_SPACE; }

  "("                       { return LEFT_PAREN; }
  ")"                       { return RIGHT_PAREN; }
  "{"                       { return LEFT_CURLY; }
  "}"                       { return RIGHT_CURLY; }
  ";"                       { return SEMI; }

  {INT}                     { return INT; }
  {STRING}                  { return STRING; }
  {BYTE}                    { return BYTE; }
  {TAG}                     { return TAG; }
  {NAME}                    { return NAME; }
  {INSTRUCTION_TOKEN}       { return INSTRUCTION_TOKEN; }
  {COMMENT_LINE}            { return COMMENT_LINE; }
  {COMMENT_MULTI_LINE}      { return COMMENT_MULTI_LINE; }

}

[^] { return BAD_CHARACTER; }


package generated;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static generated.GeneratedTypes.*;

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

INT_CONSTANT=[0-9]+
NATURAL_NUMBER_CONSTANT=[0-9]+.[0-9]+]

%%
<YYINITIAL> {
  {WHITE_SPACE}                  { return WHITE_SPACE; }

  ""                             { return STRING_CONSTANT; }
  ""                             { return TIMESTAMP_STRING_CONSTANT; }
  ""                             { return SIGNATURE_STRING_CONSTANT; }
  ""                             { return KEY_STRING_CONSTANT; }
  ""                             { return KEY_HASH_STRING_CONSTANT; }
  ""                             { return TEZ_STRING_CONSTANT; }
  ""                             { return CONTRACT_STRING_CONSTANT; }

  {INT_CONSTANT}                 { return INT_CONSTANT; }
  {NATURAL_NUMBER_CONSTANT}      { return NATURAL_NUMBER_CONSTANT; }

}

[^] { return BAD_CHARACTER; }

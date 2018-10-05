/* The following code was generated by JFlex 1.7.0-1 tweaked for IntelliJ platform */

package com.tezos.lang.michelson.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.tezos.lang.michelson.MichelsonTypes.*;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0-1
 * from the specification file <tt>/IslandWork/self-employment/customers/Tezos Foundation/tezos-intellij-plugin/grammar/_MichelsonLexer.flex</tt>
 */
public class _MichelsonLexer implements FlexLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int S_STRING = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   * Chosen bits are [7, 7, 7]
   * Total runtime size is 1928 bytes
   */
  public static int ZZ_CMAP(int ch) {
    return ZZ_CMAP_A[(ZZ_CMAP_Y[ZZ_CMAP_Z[ch>>14]|((ch>>7)&0x7f)]<<7)|(ch&0x7f)];
  }

  /* The ZZ_CMAP_Z table has 68 entries */
  static final char ZZ_CMAP_Z[] = zzUnpackCMap(
    "\1\0\103\200");

  /* The ZZ_CMAP_Y table has 256 entries */
  static final char ZZ_CMAP_Y[] = zzUnpackCMap(
    "\1\0\1\1\53\2\1\3\22\2\1\4\37\2\1\3\237\2");

  /* The ZZ_CMAP_A table has 640 entries */
  static final char ZZ_CMAP_A[] = zzUnpackCMap(
    "\11\0\1\2\4\1\22\0\1\2\1\0\1\30\1\61\1\0\1\57\2\0\1\64\1\65\1\63\2\0\1\22"+
    "\1\60\1\62\1\24\11\23\1\55\1\70\4\0\1\56\1\46\1\26\1\33\1\53\1\36\1\45\1\43"+
    "\1\52\1\44\2\21\1\41\1\34\1\40\1\51\1\35\1\37\1\50\1\47\1\42\1\54\5\21\1\0"+
    "\1\31\2\0\1\20\1\0\1\4\1\32\1\16\1\17\1\7\1\27\1\15\5\27\1\6\1\12\1\14\1\3"+
    "\1\27\1\5\1\13\1\10\1\11\2\27\1\25\2\27\1\66\1\0\1\67\7\0\1\1\32\0\1\2\337"+
    "\0\1\2\177\0\13\2\35\0\2\1\5\0\1\2\57\0\1\2\40\0");

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\1\2\5\3\1\4\1\1\2\5\1\6"+
    "\11\4\1\7\1\10\1\11\1\12\1\1\1\13\1\14"+
    "\1\15\1\16\1\17\1\20\1\21\1\0\4\3\1\22"+
    "\1\4\1\0\13\4\3\7\3\10\3\11\1\0\1\23"+
    "\1\24\4\3\1\25\1\4\1\26\13\4\1\0\3\3"+
    "\1\27\3\4\1\30\3\3\3\4\1\3\1\4\1\26"+
    "\1\3\1\4\1\3\4\4\1\26\3\4";

  private static int [] zzUnpackAction() {
    int [] result = new int[113];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\71\0\162\0\253\0\344\0\u011d\0\u0156\0\u018f"+
    "\0\u01c8\0\u0201\0\u023a\0\u023a\0\u0273\0\162\0\u02ac\0\u02e5"+
    "\0\u031e\0\u0357\0\u0390\0\u03c9\0\u0402\0\u043b\0\u0474\0\u04ad"+
    "\0\u04e6\0\u051f\0\u0558\0\u0591\0\162\0\162\0\162\0\162"+
    "\0\162\0\u05ca\0\162\0\u0603\0\u063c\0\u0675\0\u06ae\0\u06e7"+
    "\0\u0201\0\u0720\0\u0759\0\u0792\0\u07cb\0\u0804\0\u083d\0\u0876"+
    "\0\u08af\0\u08e8\0\u0921\0\u095a\0\u0993\0\u09cc\0\u0a05\0\162"+
    "\0\u0a3e\0\u0a77\0\162\0\u0ab0\0\u0ae9\0\162\0\u0b22\0\u0b5b"+
    "\0\162\0\162\0\u0b94\0\u0bcd\0\u0c06\0\u0c3f\0\u0759\0\u0c78"+
    "\0\u0720\0\u0cb1\0\u0cea\0\u0d23\0\u0d5c\0\u0d95\0\u0dce\0\u0e07"+
    "\0\u0e40\0\u0e79\0\u0eb2\0\u0eeb\0\u0f24\0\u0f5d\0\u0f96\0\u0fcf"+
    "\0\u011d\0\u1008\0\u1041\0\u107a\0\162\0\u10b3\0\u10ec\0\u1125"+
    "\0\u115e\0\u1197\0\u11d0\0\u1209\0\u1242\0\u127b\0\u12b4\0\u12ed"+
    "\0\u1326\0\u135f\0\u1398\0\u13d1\0\u140a\0\u1443\0\u147c\0\u14b5"+
    "\0\u14ee";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[113];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\2\4\1\5\1\6\1\7\5\6\1\10\2\6"+
    "\1\11\2\6\1\12\1\13\1\14\1\15\1\6\1\12"+
    "\1\6\1\16\1\3\1\6\1\17\1\20\1\21\6\12"+
    "\1\22\1\23\1\24\1\25\3\12\1\26\1\27\1\30"+
    "\1\31\1\32\1\3\1\33\1\34\1\3\1\35\1\36"+
    "\1\37\1\40\1\41\30\42\1\43\1\44\37\42\72\0"+
    "\2\4\71\0\1\6\1\45\15\6\1\0\5\6\2\0"+
    "\23\6\17\0\17\6\1\0\5\6\2\0\23\6\17\0"+
    "\4\6\1\46\12\6\1\0\5\6\2\0\23\6\17\0"+
    "\5\6\1\47\11\6\1\0\5\6\2\0\23\6\17\0"+
    "\11\6\1\50\5\6\1\0\5\6\2\0\23\6\17\0"+
    "\15\51\2\52\1\0\2\52\1\51\1\52\1\51\2\0"+
    "\1\51\22\52\37\0\2\14\67\0\2\14\1\53\46\0"+
    "\15\51\2\52\1\0\2\52\1\51\1\52\1\51\2\0"+
    "\1\51\1\52\1\54\11\52\1\55\4\52\1\55\1\52"+
    "\17\0\15\51\2\52\1\0\2\52\1\51\1\52\1\51"+
    "\2\0\1\51\13\52\1\56\6\52\17\0\15\51\2\52"+
    "\1\0\2\52\1\51\1\52\1\51\2\0\1\51\2\52"+
    "\1\57\6\52\1\57\1\52\1\57\6\52\17\0\15\51"+
    "\2\52\1\0\2\52\1\51\1\52\1\51\2\0\1\51"+
    "\12\52\1\60\7\52\17\0\15\51\2\52\1\0\2\52"+
    "\1\51\1\52\1\51\2\0\1\51\13\52\1\61\6\52"+
    "\17\0\15\51\2\52\1\0\2\52\1\51\1\52\1\51"+
    "\2\0\1\51\14\52\1\62\5\52\17\0\15\51\2\52"+
    "\1\0\2\52\1\51\1\52\1\51\2\0\1\51\3\52"+
    "\1\63\16\52\17\0\15\51\2\52\1\0\2\52\1\51"+
    "\1\52\1\51\2\0\1\51\11\52\1\64\7\52\1\65"+
    "\17\0\15\51\2\52\1\0\2\52\1\51\1\52\1\51"+
    "\2\0\1\51\5\52\1\66\14\52\17\0\17\67\3\0"+
    "\3\67\2\0\23\67\1\0\1\70\1\71\14\0\17\72"+
    "\3\0\3\72\2\0\23\72\1\0\1\73\1\74\14\0"+
    "\17\75\3\0\3\75\2\0\23\75\1\0\1\76\1\77"+
    "\11\0\1\33\1\0\67\33\63\0\1\100\5\0\30\42"+
    "\2\0\37\42\5\101\1\102\2\101\1\102\1\101\1\102"+
    "\15\101\3\102\36\101\3\0\2\6\1\103\14\6\1\0"+
    "\5\6\2\0\23\6\17\0\5\6\1\104\11\6\1\0"+
    "\5\6\2\0\23\6\17\0\11\6\1\105\5\6\1\0"+
    "\5\6\2\0\23\6\17\0\14\6\1\106\2\6\1\0"+
    "\5\6\2\0\23\6\17\0\17\52\1\0\5\52\2\0"+
    "\23\52\37\0\2\107\1\0\1\107\4\0\1\107\2\0"+
    "\1\107\6\0\2\107\4\0\1\107\20\0\17\52\1\0"+
    "\5\52\2\0\3\52\1\110\17\52\17\0\17\52\1\0"+
    "\5\52\2\0\14\52\1\55\1\52\1\111\2\52\1\55"+
    "\1\52\17\0\17\52\1\0\5\52\2\0\3\52\1\112"+
    "\17\52\17\0\17\52\1\0\5\52\2\0\3\52\1\57"+
    "\6\52\1\57\1\52\1\57\1\52\1\111\4\52\17\0"+
    "\15\52\1\113\1\52\1\0\5\52\2\0\1\52\1\114"+
    "\2\52\1\115\1\52\1\116\1\117\1\52\1\117\11\52"+
    "\17\0\17\52\1\0\5\52\2\0\12\52\1\120\10\52"+
    "\17\0\17\52\1\0\5\52\2\0\15\52\1\121\5\52"+
    "\17\0\17\52\1\0\5\52\2\0\10\52\1\112\12\52"+
    "\17\0\17\52\1\0\5\52\2\0\12\52\1\122\10\52"+
    "\17\0\17\52\1\0\5\52\2\0\22\52\1\123\17\0"+
    "\17\52\1\0\5\52\2\0\3\52\1\124\17\52\17\0"+
    "\17\67\1\0\5\67\2\0\23\67\3\0\1\67\67\0"+
    "\1\70\14\0\17\72\1\0\5\72\2\0\23\72\3\0"+
    "\1\72\67\0\1\73\14\0\17\75\1\0\5\75\2\0"+
    "\23\75\3\0\1\75\67\0\1\76\11\0\63\100\1\125"+
    "\5\100\3\0\1\6\1\126\15\6\1\0\5\6\2\0"+
    "\23\6\17\0\6\6\1\127\10\6\1\0\5\6\2\0"+
    "\23\6\17\0\2\6\1\130\14\6\1\0\5\6\2\0"+
    "\23\6\17\0\4\6\1\131\12\6\1\0\5\6\2\0"+
    "\23\6\17\0\17\52\1\0\5\52\2\0\4\52\1\115"+
    "\1\52\1\116\1\117\1\52\1\117\11\52\17\0\15\52"+
    "\1\132\1\52\1\0\5\52\2\0\23\52\17\0\17\52"+
    "\1\0\5\52\2\0\15\52\1\133\5\52\17\0\17\52"+
    "\1\0\5\52\2\0\2\52\1\54\20\52\17\0\17\52"+
    "\1\0\5\52\2\0\5\52\1\111\15\52\17\0\17\52"+
    "\1\0\5\52\2\0\4\52\1\115\16\52\17\0\17\52"+
    "\1\0\5\52\2\0\4\52\1\111\3\52\1\111\12\52"+
    "\17\0\17\52\1\0\5\52\2\0\7\52\1\111\13\52"+
    "\17\0\17\52\1\0\5\52\2\0\4\52\1\134\16\52"+
    "\17\0\17\52\1\0\5\52\2\0\3\52\1\111\6\52"+
    "\1\122\10\52\17\0\17\52\1\0\5\52\2\0\3\52"+
    "\1\111\16\52\1\123\17\0\17\52\1\0\5\52\2\0"+
    "\3\52\1\57\6\52\1\57\1\52\1\57\6\52\76\0"+
    "\1\135\11\0\3\6\1\136\13\6\1\0\5\6\2\0"+
    "\23\6\17\0\2\6\1\137\14\6\1\0\5\6\2\0"+
    "\23\6\17\0\1\6\1\140\15\6\1\0\5\6\2\0"+
    "\23\6\17\0\17\52\1\0\5\52\2\0\1\52\1\141"+
    "\21\52\17\0\17\52\1\0\5\52\2\0\17\52\1\142"+
    "\3\52\17\0\17\52\1\0\5\52\2\0\16\52\1\143"+
    "\4\52\17\0\4\6\1\144\12\6\1\0\5\6\2\0"+
    "\23\6\17\0\7\6\1\131\7\6\1\0\5\6\2\0"+
    "\23\6\17\0\12\6\1\106\4\6\1\0\5\6\2\0"+
    "\23\6\17\0\17\52\1\0\5\52\2\0\14\52\1\55"+
    "\4\52\1\55\1\52\17\0\17\52\1\0\5\52\2\0"+
    "\2\52\1\145\20\52\17\0\17\52\1\0\5\52\2\0"+
    "\10\52\1\146\12\52\17\0\5\6\1\147\11\6\1\0"+
    "\5\6\2\0\23\6\17\0\17\52\1\0\5\52\2\0"+
    "\4\52\1\111\16\52\17\0\15\52\1\150\1\52\1\0"+
    "\5\52\2\0\23\52\17\0\4\6\1\151\12\6\1\0"+
    "\5\6\2\0\23\6\17\0\17\52\1\0\5\52\2\0"+
    "\1\52\1\114\2\52\1\115\1\52\1\152\1\153\1\52"+
    "\1\117\3\52\1\133\1\154\4\52\17\0\2\6\1\131"+
    "\14\6\1\0\5\6\2\0\23\6\17\0\17\52\1\0"+
    "\5\52\2\0\4\52\1\115\12\52\1\155\3\52\17\0"+
    "\17\52\1\0\5\52\2\0\4\52\1\156\3\52\1\111"+
    "\12\52\17\0\17\52\1\0\5\52\2\0\12\52\1\157"+
    "\10\52\17\0\17\52\1\0\5\52\2\0\6\52\1\145"+
    "\14\52\17\0\17\52\1\0\5\52\2\0\13\52\1\160"+
    "\7\52\17\0\17\52\1\0\5\52\2\0\11\52\1\161"+
    "\11\52\17\0\17\52\1\0\5\52\2\0\10\52\1\111"+
    "\12\52\17\0\17\52\1\0\5\52\2\0\20\52\1\160"+
    "\2\52\14\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5415];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String[] ZZ_ERROR_MSG = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\11\12\1\1\11\16\1\5\11\1\1\1\11"+
    "\1\0\6\1\1\0\14\1\1\11\2\1\1\11\2\1"+
    "\1\11\1\1\1\0\2\11\22\1\1\0\7\1\1\11"+
    "\24\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[113];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
  public _MichelsonLexer() {
    this((java.io.Reader)null);
  }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public _MichelsonLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    int size = 0;
    for (int i = 0, length = packed.length(); i < length; i += 2) {
      size += packed.charAt(i);
    }
    char[] map = new char[size];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < packed.length()) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart() {
    return zzStartRead;
  }

  public final int getTokenEnd() {
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end, int initialState) {
    zzBuffer = buffer;
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position <tt>pos</tt> from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + ZZ_CMAP(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
        return null;
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { return BAD_CHARACTER;
            } 
            // fall through
          case 25: break;
          case 2: 
            { return WHITE_SPACE;
            } 
            // fall through
          case 26: break;
          case 3: 
            { return TYPE_NAME;
            } 
            // fall through
          case 27: break;
          case 4: 
            { return INSTRUCTION_TOKEN;
            } 
            // fall through
          case 28: break;
          case 5: 
            { return INT;
            } 
            // fall through
          case 29: break;
          case 6: 
            { yybegin(S_STRING); return QUOTE;
            } 
            // fall through
          case 30: break;
          case 7: 
            { return TYPE_ANNOTATION_TOKEN;
            } 
            // fall through
          case 31: break;
          case 8: 
            { return VAR_ANNOTATION_TOKEN;
            } 
            // fall through
          case 32: break;
          case 9: 
            { return FIELD_ANNOTATION_TOKEN;
            } 
            // fall through
          case 33: break;
          case 10: 
            { return COMMENT_LINE;
            } 
            // fall through
          case 34: break;
          case 11: 
            { return LEFT_PAREN;
            } 
            // fall through
          case 35: break;
          case 12: 
            { return RIGHT_PAREN;
            } 
            // fall through
          case 36: break;
          case 13: 
            { return LEFT_CURLY;
            } 
            // fall through
          case 37: break;
          case 14: 
            { return RIGHT_CURLY;
            } 
            // fall through
          case 38: break;
          case 15: 
            { return SEMI;
            } 
            // fall through
          case 39: break;
          case 16: 
            { return STRING_CONTENT;
            } 
            // fall through
          case 40: break;
          case 17: 
            { yybegin(YYINITIAL); return QUOTE;
            } 
            // fall through
          case 41: break;
          case 18: 
            { return TAG_TOKEN;
            } 
            // fall through
          case 42: break;
          case 19: 
            { return STRING_ESCAPE_INVALID;
            } 
            // fall through
          case 43: break;
          case 20: 
            { return STRING_ESCAPE;
            } 
            // fall through
          case 44: break;
          case 21: 
            { return BYTE;
            } 
            // fall through
          case 45: break;
          case 22: 
            { return MACRO_TOKEN;
            } 
            // fall through
          case 46: break;
          case 23: 
            { return SECTION_NAME;
            } 
            // fall through
          case 47: break;
          case 24: 
            { return COMMENT_MULTI_LINE;
            } 
            // fall through
          case 48: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}

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

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
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
    "\11\0\1\2\4\1\22\0\1\2\1\0\1\33\1\62\4\0\1\65\1\66\1\64\2\0\1\31\1\0\1\63"+
    "\1\34\11\32\1\0\1\71\5\0\1\52\1\36\1\40\1\60\1\43\1\51\1\50\1\57\1\53\2\37"+
    "\1\46\1\41\1\45\1\56\1\42\1\44\1\55\1\54\1\47\1\61\5\37\4\0\1\26\1\0\1\4\1"+
    "\22\1\16\1\17\1\7\1\30\1\15\1\27\1\20\1\30\1\24\1\23\1\6\1\12\1\14\1\3\1\30"+
    "\1\5\1\13\1\10\1\11\2\30\1\35\1\25\1\21\1\67\1\0\1\70\7\0\1\1\32\0\1\2\337"+
    "\0\1\2\177\0\13\2\35\0\2\1\5\0\1\2\57\0\1\2\40\0");

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\12\3\1\1\1\4\1\1\1\4"+
    "\12\5\1\6\1\1\1\7\1\10\1\11\1\12\1\13"+
    "\11\3\1\0\1\14\1\0\1\15\5\5\2\15\6\5"+
    "\1\0\2\3\1\16\6\3\1\17\1\5\1\20\1\5"+
    "\1\21\2\15\6\5\1\0\5\3\1\22\1\0\4\5"+
    "\1\23\1\15\1\24\2\5\1\25\1\26\1\27\1\30"+
    "\5\3\1\0\1\5\1\31\2\5\2\3\1\0\1\5"+
    "\1\24\1\5\2\3\1\0\3\5\2\3\1\16\1\32"+
    "\5\5\1\33\1\5\1\24\5\5";

  private static int [] zzUnpackAction() {
    int [] result = new int[143];
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
    "\0\0\0\72\0\164\0\256\0\350\0\u0122\0\u015c\0\u0196"+
    "\0\u01d0\0\u020a\0\u0244\0\u027e\0\u02b8\0\u02f2\0\u02f2\0\u032c"+
    "\0\u0366\0\u03a0\0\u03da\0\u0414\0\u044e\0\u0488\0\u04c2\0\u04fc"+
    "\0\u0536\0\u0570\0\u05aa\0\u05e4\0\u061e\0\72\0\72\0\72"+
    "\0\72\0\72\0\u0658\0\u0692\0\u06cc\0\u0706\0\u0740\0\u077a"+
    "\0\u07b4\0\u07ee\0\u0828\0\u032c\0\72\0\u0862\0\u089c\0\u08d6"+
    "\0\u0910\0\u094a\0\u0984\0\u09be\0\u09f8\0\u0a32\0\u0a6c\0\u0aa6"+
    "\0\u0ae0\0\u0b1a\0\u0b54\0\u0b8e\0\u0bc8\0\u0c02\0\u0c3c\0\350"+
    "\0\u0c76\0\u0cb0\0\u0cea\0\u0d24\0\u0d5e\0\u0d98\0\u0862\0\u0dd2"+
    "\0\u08d6\0\u0e0c\0\u08d6\0\u0e46\0\u0e80\0\u0eba\0\u0ef4\0\u0f2e"+
    "\0\u0f68\0\u0fa2\0\u0fdc\0\u1016\0\u1050\0\u108a\0\u10c4\0\u10fe"+
    "\0\u1138\0\350\0\u1172\0\u11ac\0\u11e6\0\u1220\0\u125a\0\u089c"+
    "\0\u1294\0\u08d6\0\u12ce\0\u1308\0\u08d6\0\u08d6\0\u0fdc\0\72"+
    "\0\u1342\0\u137c\0\u13b6\0\u13f0\0\u142a\0\u1464\0\u149e\0\u089c"+
    "\0\u14d8\0\u1512\0\u154c\0\u1586\0\u15c0\0\u15fa\0\u1634\0\u166e"+
    "\0\u16a8\0\u16e2\0\u171c\0\u1756\0\u1790\0\u17ca\0\u1804\0\u183e"+
    "\0\72\0\u08d6\0\u1878\0\u18b2\0\u18ec\0\u1926\0\u1960\0\u08d6"+
    "\0\u199a\0\u19d4\0\u1a0e\0\u1a48\0\u1a82\0\u1abc\0\u1af6";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[143];
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
    "\1\2\2\3\1\4\1\5\1\6\2\5\1\7\1\5"+
    "\1\10\1\11\2\5\1\12\1\5\1\13\1\5\1\14"+
    "\1\5\1\15\1\5\1\2\2\5\1\16\1\17\1\20"+
    "\1\21\1\5\2\22\1\23\1\24\1\25\4\22\1\26"+
    "\1\22\1\27\1\30\1\22\1\31\3\22\1\32\1\33"+
    "\1\34\1\35\1\2\1\36\1\37\1\40\1\41\1\42"+
    "\73\0\2\3\72\0\1\5\1\43\21\5\1\0\2\5"+
    "\4\0\1\5\37\0\23\5\1\0\2\5\4\0\1\5"+
    "\37\0\4\5\1\44\16\5\1\0\2\5\4\0\1\5"+
    "\37\0\4\5\1\45\10\5\1\46\5\5\1\0\2\5"+
    "\4\0\1\5\37\0\1\5\1\47\21\5\1\0\2\5"+
    "\4\0\1\5\37\0\5\5\1\50\15\5\1\0\2\5"+
    "\4\0\1\5\37\0\11\5\1\51\11\5\1\0\2\5"+
    "\4\0\1\5\37\0\7\5\1\47\13\5\1\0\2\5"+
    "\4\0\1\5\37\0\11\5\1\52\11\5\1\0\2\5"+
    "\4\0\1\5\37\0\4\5\1\53\16\5\1\0\2\5"+
    "\4\0\1\5\66\0\1\17\1\0\1\17\35\0\33\54"+
    "\1\55\36\54\32\0\1\17\1\0\1\17\1\56\37\0"+
    "\23\57\1\60\2\57\1\0\1\60\1\0\1\60\1\57"+
    "\24\60\13\0\23\57\1\60\2\57\1\0\1\60\1\0"+
    "\1\60\1\57\3\60\1\61\10\60\1\62\5\60\1\62"+
    "\1\60\13\0\23\57\1\60\2\57\1\0\1\60\1\0"+
    "\1\60\1\57\14\60\1\63\7\60\13\0\23\57\1\60"+
    "\2\57\1\0\1\60\1\0\1\60\1\57\4\60\1\64"+
    "\7\60\2\64\6\60\13\0\2\57\1\65\20\57\1\60"+
    "\2\57\1\0\1\60\1\0\1\60\1\57\24\60\13\0"+
    "\1\57\1\66\21\57\1\60\2\57\1\0\1\60\1\0"+
    "\1\60\1\57\14\60\1\67\7\60\13\0\23\57\1\60"+
    "\2\57\1\0\1\60\1\0\1\60\1\57\16\60\1\70"+
    "\5\60\13\0\23\57\1\60\2\57\1\0\1\60\1\0"+
    "\1\60\1\57\5\60\1\71\16\60\13\0\23\57\1\60"+
    "\2\57\1\0\1\60\1\0\1\60\1\57\15\60\1\72"+
    "\5\60\1\73\13\0\23\57\1\60\2\57\1\0\1\60"+
    "\1\0\1\60\1\57\7\60\1\74\14\60\10\0\1\34"+
    "\1\0\70\34\64\0\1\75\10\0\2\5\1\76\20\5"+
    "\1\0\2\5\4\0\1\5\37\0\5\5\1\77\15\5"+
    "\1\0\2\5\4\0\1\5\37\0\16\5\1\100\4\5"+
    "\1\0\2\5\4\0\1\5\37\0\3\5\1\101\17\5"+
    "\1\0\2\5\4\0\1\5\37\0\5\5\1\100\15\5"+
    "\1\0\2\5\4\0\1\5\37\0\2\5\1\102\6\5"+
    "\1\103\11\5\1\0\2\5\4\0\1\5\37\0\14\5"+
    "\1\104\6\5\1\0\2\5\4\0\1\5\37\0\11\5"+
    "\1\105\11\5\1\0\2\5\4\0\1\5\37\0\22\5"+
    "\1\106\1\0\2\5\4\0\1\5\66\0\1\107\1\0"+
    "\1\107\1\0\1\107\1\0\1\107\2\0\1\107\5\0"+
    "\2\107\5\0\1\107\14\0\23\57\1\0\2\57\4\0"+
    "\1\57\62\0\1\60\3\0\1\60\1\0\1\60\1\0"+
    "\24\60\36\0\1\60\3\0\1\60\1\0\1\60\1\0"+
    "\4\60\1\110\17\60\36\0\1\60\3\0\1\60\1\0"+
    "\1\60\1\0\14\60\1\62\2\60\1\111\2\60\1\62"+
    "\1\60\36\0\1\60\3\0\1\60\1\0\1\60\1\0"+
    "\4\60\1\112\17\60\36\0\1\60\3\0\1\60\1\0"+
    "\1\60\1\0\4\60\1\64\7\60\2\64\1\60\1\113"+
    "\4\60\13\0\6\57\1\114\14\57\1\0\2\57\4\0"+
    "\1\57\37\0\20\57\1\115\2\57\1\0\2\57\4\0"+
    "\1\57\62\0\1\60\3\0\1\60\1\0\1\60\1\0"+
    "\15\60\1\116\6\60\36\0\1\60\3\0\1\60\1\0"+
    "\1\60\1\0\16\60\1\117\5\60\36\0\1\60\3\0"+
    "\1\60\1\0\1\60\1\0\11\60\1\120\12\60\36\0"+
    "\1\60\3\0\1\60\1\0\1\60\1\0\15\60\1\121"+
    "\6\60\36\0\1\60\3\0\1\60\1\0\1\60\1\0"+
    "\23\60\1\122\36\0\1\60\3\0\1\60\1\0\1\60"+
    "\1\0\4\60\1\123\17\60\10\0\64\75\1\124\5\75"+
    "\3\0\1\5\1\125\21\5\1\0\2\5\4\0\1\5"+
    "\37\0\6\5\1\126\14\5\1\0\2\5\4\0\1\5"+
    "\37\0\4\5\1\127\16\5\1\0\2\5\4\0\1\5"+
    "\37\0\15\5\1\130\5\5\1\0\2\5\4\0\1\5"+
    "\37\0\2\5\1\131\20\5\1\0\2\5\4\0\1\5"+
    "\37\0\4\5\1\132\16\5\1\0\2\5\4\0\1\5"+
    "\37\0\20\5\1\100\2\5\1\0\2\5\4\0\1\5"+
    "\37\0\23\5\1\133\2\5\4\0\1\5\62\0\1\60"+
    "\3\0\1\60\1\0\1\60\1\0\5\60\1\134\1\60"+
    "\1\135\1\136\1\60\1\136\11\60\36\0\1\137\3\0"+
    "\1\60\1\0\1\60\1\0\24\60\13\0\4\57\1\140"+
    "\16\57\1\0\2\57\4\0\1\57\37\0\10\57\1\141"+
    "\12\57\1\0\2\57\4\0\1\57\62\0\1\60\3\0"+
    "\1\60\1\0\1\60\1\0\10\60\1\142\13\60\36\0"+
    "\1\60\3\0\1\60\1\0\1\60\1\0\5\60\1\143"+
    "\16\60\36\0\1\144\3\0\1\60\1\0\1\60\1\0"+
    "\24\60\36\0\1\60\3\0\1\60\1\0\1\60\1\0"+
    "\4\60\1\145\10\60\1\121\6\60\36\0\1\60\3\0"+
    "\1\60\1\0\1\60\1\0\4\60\1\146\16\60\1\122"+
    "\36\0\1\60\3\0\1\60\1\0\1\60\1\0\14\60"+
    "\2\123\1\60\1\147\4\60\73\0\1\150\11\0\3\5"+
    "\1\151\17\5\1\0\2\5\4\0\1\5\37\0\2\5"+
    "\1\152\20\5\1\0\2\5\4\0\1\5\37\0\10\5"+
    "\1\153\12\5\1\0\2\5\4\0\1\5\37\0\7\5"+
    "\1\154\13\5\1\0\2\5\4\0\1\5\37\0\1\5"+
    "\1\155\21\5\1\0\2\5\4\0\1\5\63\0\1\156"+
    "\70\0\1\60\3\0\1\60\1\0\1\60\1\0\6\60"+
    "\1\142\15\60\36\0\1\60\3\0\1\60\1\0\1\60"+
    "\1\0\5\60\1\134\16\60\36\0\1\60\3\0\1\60"+
    "\1\0\1\60\1\0\5\60\1\142\3\60\1\142\12\60"+
    "\36\0\1\60\3\0\1\60\1\0\1\60\1\0\2\60"+
    "\1\157\21\60\13\0\4\57\1\160\16\57\1\0\2\57"+
    "\4\0\1\57\62\0\1\60\3\0\1\60\1\0\1\60"+
    "\1\0\17\60\1\161\4\60\36\0\1\60\3\0\1\60"+
    "\1\0\1\60\1\0\2\60\1\162\21\60\13\0\4\5"+
    "\1\163\16\5\1\0\2\5\4\0\1\5\37\0\7\5"+
    "\1\132\13\5\1\0\2\5\4\0\1\5\37\0\5\5"+
    "\1\164\15\5\1\0\2\5\4\0\1\5\37\0\12\5"+
    "\1\100\10\5\1\0\2\5\4\0\1\5\37\0\12\5"+
    "\1\104\10\5\1\0\2\5\4\0\1\5\40\0\1\165"+
    "\113\0\1\60\3\0\1\60\1\0\1\60\1\0\14\60"+
    "\1\166\5\60\1\166\1\60\36\0\1\60\3\0\1\60"+
    "\1\0\1\60\1\0\11\60\1\167\12\60\36\0\1\60"+
    "\3\0\1\60\1\0\1\60\1\0\14\60\1\170\5\60"+
    "\1\170\1\60\13\0\5\5\1\171\15\5\1\0\2\5"+
    "\4\0\1\5\37\0\1\5\1\172\21\5\1\0\2\5"+
    "\4\0\1\5\47\0\1\173\104\0\1\60\3\0\1\60"+
    "\1\0\1\60\1\0\14\60\1\174\2\60\1\142\2\60"+
    "\1\174\1\60\36\0\1\175\3\0\1\60\1\0\1\60"+
    "\1\0\24\60\36\0\1\60\3\0\1\60\1\0\1\60"+
    "\1\0\14\60\1\176\2\60\1\142\2\60\1\176\1\60"+
    "\13\0\4\5\1\177\16\5\1\0\2\5\4\0\1\5"+
    "\37\0\3\5\1\200\17\5\1\0\2\5\4\0\1\5"+
    "\63\0\1\201\70\0\1\60\3\0\1\60\1\0\1\60"+
    "\1\0\14\60\1\174\2\60\1\202\2\60\1\174\1\60"+
    "\36\0\1\60\3\0\1\60\1\0\1\60\1\0\2\60"+
    "\1\203\2\60\1\134\1\60\1\204\1\205\1\60\1\136"+
    "\3\60\1\206\1\207\4\60\36\0\1\60\3\0\1\60"+
    "\1\0\1\60\1\0\14\60\1\176\2\60\1\210\2\60"+
    "\1\176\1\60\13\0\2\5\1\132\20\5\1\0\2\5"+
    "\4\0\1\5\37\0\1\100\22\5\1\0\2\5\4\0"+
    "\1\5\62\0\1\60\3\0\1\60\1\0\1\60\1\0"+
    "\3\60\1\61\20\60\36\0\1\60\3\0\1\60\1\0"+
    "\1\60\1\0\5\60\1\134\12\60\1\211\3\60\36\0"+
    "\1\60\3\0\1\60\1\0\1\60\1\0\5\60\1\212"+
    "\3\60\1\142\12\60\36\0\1\60\3\0\1\60\1\0"+
    "\1\60\1\0\20\60\1\213\3\60\36\0\1\60\3\0"+
    "\1\60\1\0\1\60\1\0\15\60\1\214\6\60\36\0"+
    "\1\60\3\0\1\60\1\0\1\60\1\0\7\60\1\215"+
    "\14\60\36\0\1\60\3\0\1\60\1\0\1\60\1\0"+
    "\13\60\1\216\10\60\36\0\1\60\3\0\1\60\1\0"+
    "\1\60\1\0\3\60\1\215\20\60\36\0\1\60\3\0"+
    "\1\60\1\0\1\60\1\0\12\60\1\217\11\60\36\0"+
    "\1\60\3\0\1\60\1\0\1\60\1\0\5\60\1\142"+
    "\16\60\36\0\1\60\3\0\1\60\1\0\1\60\1\0"+
    "\11\60\1\142\12\60\36\0\1\60\3\0\1\60\1\0"+
    "\1\60\1\0\21\60\1\216\2\60\10\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[6960];
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
    "\1\0\1\11\33\1\5\11\11\1\1\0\1\11\1\0"+
    "\16\1\1\0\26\1\1\0\6\1\1\0\14\1\1\11"+
    "\5\1\1\0\6\1\1\0\5\1\1\0\5\1\1\11"+
    "\16\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[143];
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
          case 28: break;
          case 2: 
            { return WHITE_SPACE;
            } 
            // fall through
          case 29: break;
          case 3: 
            { return TYPE_NAME;
            } 
            // fall through
          case 30: break;
          case 4: 
            { return INT;
            } 
            // fall through
          case 31: break;
          case 5: 
            { return INSTRUCTION_TOKEN;
            } 
            // fall through
          case 32: break;
          case 6: 
            { return COMMENT_LINE;
            } 
            // fall through
          case 33: break;
          case 7: 
            { return LEFT_PAREN;
            } 
            // fall through
          case 34: break;
          case 8: 
            { return RIGHT_PAREN;
            } 
            // fall through
          case 35: break;
          case 9: 
            { return LEFT_CURLY;
            } 
            // fall through
          case 36: break;
          case 10: 
            { return RIGHT_CURLY;
            } 
            // fall through
          case 37: break;
          case 11: 
            { return SEMI;
            } 
            // fall through
          case 38: break;
          case 12: 
            { return STRING;
            } 
            // fall through
          case 39: break;
          case 13: 
            { return TAG;
            } 
            // fall through
          case 40: break;
          case 14: 
            { return TYPE_NAME_COMPARABLE;
            } 
            // fall through
          case 41: break;
          case 15: 
            { return BYTE;
            } 
            // fall through
          case 42: break;
          case 16: 
            { return MACRO_PAIR_ACCESS_TOKEN;
            } 
            // fall through
          case 43: break;
          case 17: 
            { return MACRO_PAIRS_TOKEN;
            } 
            // fall through
          case 44: break;
          case 18: 
            { return SECTION_NAME;
            } 
            // fall through
          case 45: break;
          case 19: 
            { return TRUE;
            } 
            // fall through
          case 46: break;
          case 20: 
            { return MACRO_TOKEN;
            } 
            // fall through
          case 47: break;
          case 21: 
            { return MACRO_DIIP_TOKEN;
            } 
            // fall through
          case 48: break;
          case 22: 
            { return MACRO_DUUP_TOKEN;
            } 
            // fall through
          case 49: break;
          case 23: 
            { return MACRO_NESTED_TOKEN;
            } 
            // fall through
          case 50: break;
          case 24: 
            { return COMMENT_MULTI_LINE;
            } 
            // fall through
          case 51: break;
          case 25: 
            { return FALSE;
            } 
            // fall through
          case 52: break;
          case 26: 
            { return MACRO_MAP_CADR_TOKEN;
            } 
            // fall through
          case 53: break;
          case 27: 
            { return MACRO_SET_CADR_TOKEN;
            } 
            // fall through
          case 54: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}

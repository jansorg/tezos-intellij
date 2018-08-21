package com.tezos.lang.michelson.lexer

import com.intellij.lexer.FlexAdapter
import com.tezos.lang.michelson.parser._MichelsonLexer

/**
 * @author jansorg
 */
class MichelsonLexer : FlexAdapter(_MichelsonLexer())
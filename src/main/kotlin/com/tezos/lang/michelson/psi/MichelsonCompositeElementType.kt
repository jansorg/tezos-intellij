package com.tezos.lang.michelson.psi

import com.intellij.psi.tree.IElementType
import com.tezos.lang.michelson.MichelsonLanguage

/**
 * Composite element type which uses the Michelson language.
 * @author jansorg
 */
class MichelsonCompositeElementType(debugName: String) : IElementType(debugName, MichelsonLanguage)
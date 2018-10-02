package com.tezos.lang.michelson.editor.completion

/**
 * @author jansorg
 */
class MichelsonInstructionCompletionContributor : AbstractInstructionCompletionContributor(true) {
    init {
        extend(null, PATTERN_IN_MICHELSON_FILE, MichelsonInstructionNameCompletion())
    }
}
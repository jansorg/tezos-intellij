package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionContributor

/**
 * @author jansorg
 */
class MichelsonCompletionContributor : CompletionContributor() {
    init {
        extend(null, TOPLEVEL_PATTERN, MichelsonSectionCompletion())
    }
}


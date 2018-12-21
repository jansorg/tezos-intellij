package com.tezos.lang.michelson.editor.intention

import com.tezos.lang.michelson.psi.PsiType

/**
 * Intention to remove a single type from the PSI tree.
 * @author jansorg
 */
class RemoveTypeIntention(element: PsiType) : RemovePsiElementIntention(element, LABEL) {
    companion object {
        const val LABEL = "Remove type"
    }
}
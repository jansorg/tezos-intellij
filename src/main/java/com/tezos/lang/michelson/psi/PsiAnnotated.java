package com.tezos.lang.michelson.psi;

import java.util.Collections;
import java.util.List;

/**
 * PSI element which have attached annotations implement this interface.
 *
 * @author jansorg
 */
interface PsiAnnotated extends MichelsonComposite {
    /**
     * @return The annotations of the current element, default to an empty list.
     */
    default List<PsiAnnotation> getAnnotations() {
        return Collections.emptyList();
    }
}
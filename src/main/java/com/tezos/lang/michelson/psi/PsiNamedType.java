package com.tezos.lang.michelson.psi;

import com.tezos.lang.michelson.lang.type.TypeMetadata;

/**
 * @author jansorg
 */
public interface PsiNamedType extends PsiType {
    String getTypeNameString();

    TypeMetadata getTypeMetadata();
}

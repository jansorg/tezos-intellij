package com.tezos.lang.michelson.psi

/**
 * The possible types of a section.
 * @author jansorg
 */
enum class PsiSectionType {
    PARAMETER,
    //RETURN, //unknown section for now
    STORAGE,
    CODE;
}
package com.tezos.lang.michelson.psi

/**
 * The possible types of a section.
 * @author jansorg
 */
enum class PsiSectionType {
    PARAMETER,
    STORAGE,
    CODE;

    fun codeName() = name.toLowerCase()
}
package com.tezos.lang.michelson.psi

import com.tezos.lang.michelson.lang.instruction.InstructionMetadata

/**
 * @author jansorg
 */
interface PsiInstructionWithMeta : PsiInstruction {
    fun getInstructionMetadata(): InstructionMetadata?
}
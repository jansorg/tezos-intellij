package com.tezos.client

import com.google.common.collect.Maps
import com.intellij.psi.PsiFile
import com.tezos.client.stack.MichelsonStackTransformations

object MockTezosClient : TezosClient {
    val stacks: MutableMap<String, MichelsonStackTransformations> = Maps.newConcurrentMap()

    fun addTypes(file: PsiFile, stack: MichelsonStackTransformations) {
        stacks[file.text] = stack
    }

    fun addTypes(content: String, stack: MichelsonStackTransformations) {
        stacks[content] = stack
    }

    fun reset() {
        stacks.clear()
    }

    override fun typecheck(content: String): MichelsonStackTransformations? {
        return stacks[content]
    }
}
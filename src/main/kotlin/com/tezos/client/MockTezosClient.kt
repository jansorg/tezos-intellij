package com.tezos.client

import com.google.common.collect.Maps
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.tezos.client.stack.MichelsonStack
import com.tezos.client.stack.MichelsonStackTransformation
import com.tezos.client.stack.MichelsonStackTransformations
import java.nio.file.Files
import java.nio.file.Path

object MockTezosClient : TezosClient {
    private val stacks: MutableMap<String, MichelsonStackTransformations> = Maps.newConcurrentMap()

    fun addTypes(file: PsiFile, stack: MichelsonStackTransformations) {
        stacks[file.text] = stack
    }

    fun addTypes(content: String, stack: MichelsonStackTransformations) {
        stacks[content] = stack
    }

    fun addTypes(element: PsiElement, before: MichelsonStack, after: MichelsonStack) {
        val content = element.containingFile.text
        val transformations = stacks.getOrDefault(content, MichelsonStackTransformations(emptyList(), emptyList()))
        stacks[content] = transformations.copy(transformations = transformations.transformations + listOf(MichelsonStackTransformation(element.textOffset, element.textRange.endOffset, before, after)))
    }

    fun loadStackInfo(file: PsiFile, dataFile: Path) {
        val content = Files.readAllBytes(dataFile)
        addTypes(file, StandaloneTezosClient.parseStdout(String(content)))
    }

    fun reset() {
        stacks.clear()
    }

    override fun typecheck(content: String): MichelsonStackTransformations? {
        return stacks[content]
    }
}
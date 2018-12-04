package com.tezos.lang.michelson.stackInfo

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.tezos.client.MockTezosClient
import com.tezos.client.TezosClient

@Suppress("ComponentNotRegistered")
class MockMichelsonStackInfoManager : MichelsonStackInfoManagerImpl() {
    companion object {
        fun getInstance(project: Project?): MockMichelsonStackInfoManager = MichelsonStackInfoManager.getInstance(project) as MockMichelsonStackInfoManager
    }

    // returns the global mock tezos client
    override fun defaultTezosClient(): TezosClient? {
        return MockTezosClient
    }

    override fun triggerStackUpdate(document: Document) {
        //don't use alarm, execute immediately
        updateStackInfo(document)
    }

    fun forceUpdate(doc: Document) {
        updateStackInfo(doc)
    }

    fun forceUpdate(editor: Editor) {
        updateStackInfo(editor.document)
    }
}
package com.tezos.lang.michelson.stackInfo

import com.google.common.collect.Maps
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.tezos.client.MockTezosClient
import com.tezos.client.TezosClient
import com.tezos.intellij.settings.TezosSettingService

@Suppress("ComponentNotRegistered")
class MockMichelsonStackInfoManager(project: Project) : MichelsonStackInfoManagerImpl(project) {
    companion object {
        fun getInstance(project: Project?): MockMichelsonStackInfoManager = MichelsonStackInfoManager.getInstance(project) as MockMichelsonStackInfoManager
    }

    private val docUpdateCount: MutableMap<String, Int> = Maps.newConcurrentMap()

    fun reset() {
        this.client = defaultTezosClient()
        this.docUpdateCount.clear()
    }

    // returns the global mock tezos client
    override fun defaultTezosClient(): TezosClient? {
        return when (TezosSettingService.getSettings().getDefaultClient()) {
            null -> null
            else -> MockTezosClient
        }
    }

    override fun triggerStackUpdate(document: Document) {
        client ?: return
        val path = documentPath(document) ?: return

        docUpdateCount.put(path.toString(), docUpdateCount.getOrDefault(path.toString(), 0) + 1)

        //don't use alarm, execute immediately
        updateStackInfo(document)
    }

    fun forceUpdate(doc: Document) {
        updateStackInfo(doc)
    }

    fun forceUpdate(editor: Editor) {
        updateStackInfo(editor.document)
    }

    fun documentUpdateCount(document: Document) : Int {
        val path = documentPath(document) ?: 0
        return docUpdateCount.getOrDefault(path.toString(), 0)
    }
}
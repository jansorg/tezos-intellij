package com.tezos.lang.michelson.stackInfo

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project

interface StackInfoUpdateListener {
    fun stackInfoUpdated(doc: Document)
}

/**
 * Keeps track of stack info data for a set of registered files.
 *
 * @author jansorg
 */
interface MichelsonStackInfoManager {
    companion object {
        /**
         * Returns the application-wide instance of this stack info manager.
         */
        fun getInstance(project: Project?): MichelsonStackInfoManager {
            return project!!.getComponent(MichelsonStackInfoManager::class.java)
        }
    }

    /**
     * Registers a file. Stack info will be requested from the default tezos client when the file changes.
     */
    fun registerDocument(document: Document, parentDisposable: Disposable)

    /**
     * Adds a listener to be notified when new stack info data is available.
     * The listener will be automatically removed when the parent disposable is disposed.
     * The listener will be called on the Swing UI dispatcher thread.
     */
    fun addListener(listener: StackInfoUpdateListener, parentDisposable: Disposable)

    /**
     * Returns the currently cached stack information for a given file.
     * If the file wasn't registered then a null value will be returned.
     * If the tezos client is being executed then this call of stackInfo will wait until either the data is available
     * or the timeout occurred.
     * @throws DefaultClientUnavailableException
     * @return The stack info, if available.
     */
    fun stackInfo(document: Document): StackInfo?
}
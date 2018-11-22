package com.tezos.lang.michelson.stackInfo

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ComponentManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import java.util.concurrent.TimeUnit

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
    fun registerFile(document: Document)

    /**
     * Deregisters a previously registered file from this manager.
     */
    fun unregisterFile(document: Document)

    /**
     * Returns the currently cached stack information for a given file.
     * If the file wasn't registered then a null value will be returned.
     * If the tezos client is being executed then this call of stackInfo will wait until either the data is available
     * or the timeout occurred.
     * @throws DefaultClientUnavailableException
     * @return The stack info, if available.
     */
    fun stackInfo(document: Document, timeout: Long, timeoutUnit: TimeUnit): StackInfo?
}
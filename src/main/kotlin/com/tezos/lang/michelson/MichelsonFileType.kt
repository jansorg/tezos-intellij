package com.tezos.lang.michelson

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Icons
import javax.swing.Icon

/**
 * @author jansorg
 */
object MichelsonFileType : FileType {
    override fun getDefaultExtension(): String = "tz"

    override fun getIcon(): Icon = Icons.METHOD_ICON

    override fun getCharset(file: VirtualFile, data: ByteArray): String? = "UTF-8"

    override fun getName(): String = "Michelson"

    override fun getDescription(): String = "Michelson language for the Tezos platform"

    override fun isBinary(): Boolean = false

    override fun isReadOnly(): Boolean = false
}
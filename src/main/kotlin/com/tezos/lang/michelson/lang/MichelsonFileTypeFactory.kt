package com.tezos.lang.michelson.lang

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

/**
 * Factory to register the Michelson file type.
 * For now, it only registers *.tz as Michelson files.
 *
 * @author jansorg
 */
class MichelsonFileTypeFactory : FileTypeFactory() {
    override fun createFileTypes(consumer: FileTypeConsumer) {
        consumer.consume(MichelsonFileType, "tz")
    }
}
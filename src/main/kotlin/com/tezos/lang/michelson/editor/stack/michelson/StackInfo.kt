package com.tezos.lang.michelson.editor.stack.michelson

import com.tezos.client.stack.MichelsonStackTransformations
import org.apache.commons.codec.digest.DigestUtils

internal data class StackInfo(val contentMD5: String, val stack: MichelsonStackTransformations) {
    fun matches(content: String): Boolean {
        return contentMD5.equals(md5(content))
    }

    companion object {
        private fun md5(content: String): String {
            return DigestUtils.md5Hex(content)
        }

        fun createFromContent(content: String, stack: MichelsonStackTransformations): StackInfo {
            return StackInfo(md5(content), stack)
        }
    }
}
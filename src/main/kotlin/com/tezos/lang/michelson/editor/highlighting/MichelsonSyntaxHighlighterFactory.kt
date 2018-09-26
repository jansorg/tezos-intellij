package com.tezos.lang.michelson.editor.highlighting

import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * @author jansorg
 */
class MichelsonSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, file: VirtualFile?): SyntaxHighlighter {
        return MichelsonSyntaxHighlighter()
    }
}
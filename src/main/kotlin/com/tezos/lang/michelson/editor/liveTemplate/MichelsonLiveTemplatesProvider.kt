package com.tezos.lang.michelson.editor.liveTemplate

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider

/**
 * @author jansorg
 */
class MichelsonLiveTemplatesProvider : DefaultLiveTemplatesProvider {
    override fun getHiddenLiveTemplateFiles(): Array<String>? = null

    override fun getDefaultLiveTemplateFiles(): Array<String> {
        return arrayOf("/liveTemplates/Michelson")
    }
}

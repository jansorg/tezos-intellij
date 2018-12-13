package com.tezos.lang.michelson.runConfig

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.tezos.lang.michelson.psi.MichelsonPsiFile

/**
 * Produces Michelson run configurations for a context inside of a PSI file.
 * This only accepts configurations of type {@linkk MichelsonRunConfiguration}.
 *
 * @author jansorg
 */
class MichelsonRunConfigurationProducer : RunConfigurationProducer<MichelsonRunConfiguration>(MichelsonRunConfigurationType.getInstance()) {
    override fun isConfigurationFromContext(configuration: MichelsonRunConfiguration, context: ConfigurationContext): Boolean {
        val psiFile = context.psiLocation?.containingFile ?: return false

        return configuration.filePath == filePath(psiFile)
    }

    override fun setupConfigurationFromContext(configuration: MichelsonRunConfiguration, context: ConfigurationContext, sourceElement: Ref<PsiElement>): Boolean {
        val containingFile = sourceElement.get()?.containingFile as? MichelsonPsiFile ?: return false
        sourceElement.set(containingFile)

        configuration.name = containingFile.name
        configuration.filePath = filePath(containingFile)
        return true;
    }

    private fun filePath(containingFile: PsiFile): String {
        return containingFile.virtualFile.path
    }
}

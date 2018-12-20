package com.tezos.lang.michelson.psi

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.util.IncorrectOperationException
import com.tezos.lang.michelson.lang.MichelsonFileType
import com.tezos.lang.michelson.psi.impl.PsiAnnotationListImpl
import org.jetbrains.annotations.NonNls

/**
 * @author jansorg
 */
class MichelsonPsiFactory(private val psiManager: PsiManager) {
    companion object {
        private val DUMMY_FILE_NAME = "dummy.tz"

        fun getInstance(project: Project): MichelsonPsiFactory {
            return ServiceManager.getService(project, MichelsonPsiFactory::class.java)
        }
    }

    /**
     * Creates a complex type psi element from a text. The text must not include the parens.
     */
    fun createSimpleType(text: String): PsiSimpleType {
        val section = createParameterSection(text)?: throw IncorrectOperationException("parameter section could not be created")
        val type = section.type as? PsiSimpleType ?: throw IncorrectOperationException("type is not a simple type")
        return type
    }

    /**
     * Creates a complex type psi element from a text. The text must not include the parens.
     */
    fun createComplexType(text: String): PsiComplexType {
        val section = createParameterSection("($text)")?: throw IncorrectOperationException("parameter section could not be created")
        val type = section.type as? PsiComplexType ?: throw IncorrectOperationException("type is not a complex type")
        return type
    }

    fun createAnnotationList(text: String): PsiAnnotationList {
        val section = createParameterSection("(int $text)")?: throw IncorrectOperationException("parameter section could not be created")
        val type = (section.type as? PsiComplexType)?.annotationList ?: throw IncorrectOperationException("type is not an annotation list")
        return type
    }

    private fun createCodeSection(code: String): PsiCodeSection? {
        val file = createDummyMichelsonFile("code {$code}")
        return file.getContract()?.findCodeSection()
    }

    private fun createParameterSection(code: String): PsiTypeSection? {
        val file = createDummyMichelsonFile("parameter $code")
        return file.getContract()?.findParameterSection()
    }

    private fun createDummyMichelsonFile(@NonNls text: String): MichelsonPsiFile {
        return PsiFileFactory.getInstance(psiManager.project).createFileFromText(DUMMY_FILE_NAME, MichelsonFileType, text) as MichelsonPsiFile
    }
}
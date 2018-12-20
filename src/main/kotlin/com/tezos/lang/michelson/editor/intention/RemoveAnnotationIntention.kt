package com.tezos.lang.michelson.editor.intention

import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.tezos.lang.michelson.psi.*

/**
 * Intention to remove a single, predefined annotation.
 * @author jansorg
 */
class RemoveAnnotationIntention(element: PsiAnnotation) : LocalQuickFixAndIntentionActionOnPsiElement(element) {
    companion object {
        const val LABEL = "Remove annotation"
    }

    override fun getFamilyName(): String = MichelsonIntentions.FAMILY

    override fun getText(): String = LABEL

    override fun invoke(project: Project, file: PsiFile, editor: Editor?, startElement: PsiElement, endElement: PsiElement) {
        startElement.delete()
    }

    override fun startInWriteAction(): Boolean {
        return true
    }
}
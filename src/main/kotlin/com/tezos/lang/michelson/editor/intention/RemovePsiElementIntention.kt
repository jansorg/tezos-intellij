package com.tezos.lang.michelson.editor.intention

import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

/**
 * Intention to remove a single, predefined annotation.
 * @author jansorg
 */
abstract class RemovePsiElementIntention(element: PsiElement, private val label: String) : LocalQuickFixAndIntentionActionOnPsiElement(element) {
    override fun getFamilyName(): String = MichelsonIntentions.FAMILY

    override fun getText(): String = label

    override fun invoke(project: Project, file: PsiFile, editor: Editor?, startElement: PsiElement, endElement: PsiElement) {
        startElement.delete()
    }

    override fun startInWriteAction(): Boolean {
        return true
    }
}
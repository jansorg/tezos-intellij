package com.tezos.lang.michelson.editor.intention

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.tezos.lang.michelson.psi.*

/**
 * @author jansorg
 */
class RemoveTrailingAnnotationsIntention : PsiElementBaseIntentionAction() {
    companion object {
        const val LABEL = "Remove annotations"
    }

    init {
        text = LABEL
    }

    override fun getFamilyName(): String = MichelsonIntentions.FAMILY

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return findAnnotations(element) != null
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val list = findAnnotations(element)
        if (list != null) {
            list.delete()
        }
    }

    private fun findAnnotations(element: PsiElement): PsiTrailingAnnotationList? {
        return PsiTreeUtil.findFirstParent(element) { psi ->
            psi is PsiTrailingAnnotationList
        } as? PsiTrailingAnnotationList
    }
}
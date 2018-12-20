package com.tezos.lang.michelson.editor.intention

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.tezos.lang.michelson.psi.PsiGenericInstruction
import com.tezos.lang.michelson.psi.PsiTrailingAnnotationList

/**
 * @author jansorg
 */
class MoveTrailingAnnotationsIntention : PsiElementBaseIntentionAction() {
    companion object {
        const val LABEL = "Move annotations"
    }

    init {
        text = LABEL
    }

    override fun getFamilyName(): String = "Tezos/Michelson"

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return findAnnotations(element) != null
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val list = findAnnotations(element) ?: return

        val parent = list.parent
        when (parent) {
            is PsiGenericInstruction -> moveIntoInstruction(list, parent)
        }
    }

    private fun moveIntoInstruction(list: PsiTrailingAnnotationList, parent: PsiGenericInstruction) {

    }

    private fun findAnnotations(element: PsiElement): PsiTrailingAnnotationList? {
        return PsiTreeUtil.findFirstParent(element) { psi ->
            psi is PsiTrailingAnnotationList
        } as? PsiTrailingAnnotationList
    }
}
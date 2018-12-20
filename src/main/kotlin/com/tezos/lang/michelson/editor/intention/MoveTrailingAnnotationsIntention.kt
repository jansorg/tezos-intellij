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
            is PsiInstruction -> moveIntoInstruction(list, parent)
            is PsiComplexType -> moveIntoComplexType(list, parent)
        }

        list.delete()
    }

    private fun moveIntoComplexType(list: PsiTrailingAnnotationList, parent: PsiComplexType) {
        val existingList = parent.annotationList
        when (existingList) {
            null -> parent.typeToken?.let {
                val newList = MichelsonPsiFactory.getInstance(list.project).createAnnotationList(list.text)
                parent.addAfter(newList, it)
            }
            else -> {
                for (a in list.annotations) {
                    existingList.add(a)
                }
            }
        }
    }

    private fun moveIntoInstruction(list: PsiTrailingAnnotationList, parent: PsiInstruction) {
        val existingList = when (parent) {
            is PsiGenericInstruction -> parent.annotationList
            is PsiMacroInstruction -> parent.annotationList
            else -> null
        }

        when (existingList) {
            null -> {
                val newList = MichelsonPsiFactory.getInstance(list.project).createAnnotationList(list.text)
                parent.addAfter(newList, parent.instructionToken)
            }
            else -> {
                for (a in list.annotations) {
                    existingList.add(a)
                }
            }
        }
    }

    private fun findAnnotations(element: PsiElement): PsiTrailingAnnotationList? {
        return PsiTreeUtil.findFirstParent(element) { psi ->
            psi is PsiTrailingAnnotationList
        } as? PsiTrailingAnnotationList
    }
}
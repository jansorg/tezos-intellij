package com.tezos.lang.michelson.editor.intention

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.tezos.lang.michelson.psi.MichelsonPsiFactory
import com.tezos.lang.michelson.psi.PsiSimpleType

/**
 * @author jansorg
 */
class WrapSimpleTypeIntention : PsiElementBaseIntentionAction() {
    companion object {
        const val LABEL = "Wrap in parentheses"
    }

    init {
        text = LABEL
    }

    override fun getFamilyName(): String = MichelsonIntentions.FAMILY

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return element.parent is PsiSimpleType
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val replacement = MichelsonPsiFactory.getInstance(project).createComplexType(element.parent.text)
        element.parent.replace(replacement)
    }
}
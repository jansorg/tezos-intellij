package com.tezos.lang.michelson.editor.intention

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.tezos.lang.michelson.psi.MichelsonPsiFactory
import com.tezos.lang.michelson.psi.PsiComplexType

/**
 * @author jansorg
 */
class UnwrapSimpleTypeIntention : PsiElementBaseIntentionAction() {
    init {
        text = "Unwrap type"
    }

    override fun getFamilyName(): String = "Tezos/Michelson"

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        val type = element.parent as? PsiComplexType ?: return false

        return type.typeArguments.isEmpty() && type.typeName != null && type.annotations.isEmpty() && type.typeMetadata?.isSimple ?: false
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val type = element.parent as PsiComplexType

        val replacement = MichelsonPsiFactory.getInstance(project).createSimpleType(type.typeName!!.text)
        type.replace(replacement)
    }
}
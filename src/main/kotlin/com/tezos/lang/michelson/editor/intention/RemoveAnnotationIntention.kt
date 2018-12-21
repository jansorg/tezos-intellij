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
class RemoveAnnotationIntention(element: PsiAnnotation) : RemovePsiElementIntention(element, LABEL) {
    companion object {
        const val LABEL = "Remove annotation"
    }
}
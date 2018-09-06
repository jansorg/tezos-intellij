package com.tezos.lang.michelson.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.tezos.lang.michelson.ui.Icons

/**
 * @author jansorg
 */
class CreateMichelsonFileAction : CreateFileFromTemplateAction("Michelson Contract", "Create a new Michelson smart contract", Icons.Tezos), DumbAware {
    /**
     * This method must be visible for test cases.
     */
    public override fun getActionName(directory: PsiDirectory, newName: String, templateName: String): String {
        return "Michelson Contract"
    }

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("New Michelson smart contract")
                .addKind("File", Icons.Tezos, "Michelson File")
                .addKind("Empty contract", Icons.Tezos, "Michelson Contract")
    }

    /**
     * This methods must be visible for test cases.
     */
    public override fun createFile(name: String, templateName: String, dir: PsiDirectory): PsiFile? {
        return super.createFile(name, templateName, dir)
    }

    override fun equals(other: Any?): Boolean {
        return other is CreateMichelsonFileAction
    }

    override fun hashCode(): Int {
        return 0
    }
}
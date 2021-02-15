package com.tezos.lang.michelson

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.EdtTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import com.intellij.util.ThrowableRunnable
import com.tezos.intellij.settings.TezosClientConfig
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.lang.michelson.psi.MichelsonPsiFile
import com.tezos.lang.michelson.stackInfo.MockMichelsonStackInfoManager

/**
 * @author jansorg
 */
abstract class MichelsonFixtureTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String = MichelsonTestUtils.dataPath().toString()

    override fun setUp() {
        super.setUp()

        TezosSettingService.getSettings().setClients(emptyList())
        TezosSettingService.publishDefaultClientChanged()
        MockMichelsonStackInfoManager.getInstance(project).reset()
    }

    fun setDefaultClient() {
        TezosSettingService.getSettings().setClients(listOf(TezosClientConfig("test client", "/alphanet.sh", true)))
        TezosSettingService.publishDefaultClientChanged()
    }

    open fun configure(code: String, allowWhitespace: Boolean = false): Pair<MichelsonPsiFile, PsiElement?> {
        val file = myFixture.configureByText("file.tz", code)

        return Pair(file as MichelsonPsiFile, getPsiAtCaret(allowWhitespace))
    }

    open fun configureByCode(code: String, allowWhitespace: Boolean = false): Pair<MichelsonPsiFile, PsiElement?> {
        val file = myFixture.configureByText("file.tz", codeTemplate(code))

        return Pair(file as MichelsonPsiFile, getPsiAtCaret(allowWhitespace))
    }

    open fun configureByCodeAndFocus(text: String) {
        configureByCode(text)

        triggerOptionalEditorEvent(myFixture.file.virtualFile)
    }

    open fun triggerOptionalEditorEvent(file: VirtualFile?) {
        val newEditor = file?.let {
            FileEditorManager.getInstance(project).getSelectedEditor(it)
        }

        if (ideBuildSkipsEditorEvents()) {
            val event = FileEditorManagerEvent(FileEditorManager.getInstance(project), null, null, file, newEditor)
            EdtTestUtil.runInEdtAndWait(ThrowableRunnable {
                project.messageBus.syncPublisher(FileEditorManagerListener.FILE_EDITOR_MANAGER).selectionChanged(event)
            })
        }
    }

    open fun ideBuildSkipsEditorEvents() = ApplicationInfo.getInstance().build.baselineVersion <= 181

    fun codeOffset(code: String): Int = Math.max(0, codeTemplate(code).indexOf("<caret>"))

    private fun codeTemplate(code: String): String {
        // add code afterwards because newlines in it break trimIndent()
        return """
                parameter unit;
                storage unit;
                code { _CODE_ }
            """.trimIndent().replace("_CODE_", code)
    }

    fun configureByCode(code: () -> String): Pair<MichelsonPsiFile, PsiElement?> {
        return configureByCode(code())
    }

    fun getPsiAtCaret(allowWhitespace: Boolean = false): PsiElement? {
        val offset = myFixture.caretOffset

        var psi = myFixture.file.findElementAt(offset)
        if (!allowWhitespace && psi is PsiWhiteSpace) {
            psi = myFixture.file.findElementAt(offset - 1)
        }
        if (!allowWhitespace && psi is PsiWhiteSpace) {
            psi = myFixture.file.findElementAt(offset + 1)
        }

        return psi
    }

    /**
     * Returns the psi parent of the expected type when it's range exaclty matches the start element.
     * This mostly happens for wrappers of leaf nodes.
     */
    inline fun <reified T : PsiElement> psiWrappingParent(current: PsiElement): T? {
        val found = PsiTreeUtil.findFirstParent(current, false) { it is T }
        return when {
            found?.textRange == current.textRange -> found as T
            else -> null
        }
    }

    inline fun <reified T : PsiElement> PsiElement?.wrappingParent(): T? {
        return when (this) {
            null -> null
            else -> psiWrappingParent(this)
        }
    }

    inline fun <reified T : PsiElement> PsiElement?.firstParent(): T? {
        return when (this) {
            null -> null
            else -> PsiTreeUtil.findFirstParent(this, false) { it is T } as T?
        }
    }
}


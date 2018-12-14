package com.tezos.lang.michelson.stackInfo

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.tezos.intellij.settings.TezosClientConfig
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.lang.michelson.MichelsonFixtureTest
import com.tezos.lang.michelson.lang.MichelsonFileType
import org.junit.Assert

/**
 * @author jansorg
 */
class MichelsonStackInfoManagerImplTest : MichelsonFixtureTest() {
    // no client at first, document edits must show errors in that case
    // the stack info must be refreshed when the client changed, even without changes to the document
    fun testDefaultClientChange() {
        try {
            val mockMgr = MockMichelsonStackInfoManager.getInstance(project)

            val unfocusedFile = myFixture.configureByText(MichelsonFileType, "")
            val unfocusedDoc = myFixture.editor.document
            // close it because an update to the default client will trigger stack updates for selected files
            FileEditorManager.getInstance(project).closeFile(unfocusedFile.virtualFile)

            // make sure that no client is available
            TezosSettingService.getSettings().clients.clear()

            configureByCodeAndFocus("<caret>DROP;")
            Assert.assertEquals("without a default client opening must not trigger a stack update", 0, mockMgr.documentUpdateCount(myFixture.editor.document))
            Assert.assertEquals("without a default client opening must not trigger a stack update", 0, mockMgr.documentUpdateCount(unfocusedDoc))

            myFixture.type("DROP; ")
            Assert.assertEquals("without a default client a change must not trigger a stack update", 0, mockMgr.documentUpdateCount(myFixture.editor.document))
            Assert.assertEquals("without a default client a change must not trigger a stack update", 0, mockMgr.documentUpdateCount(unfocusedDoc))

            // now set our new default client and check again
            setDefaultClient()

            Assert.assertEquals("A new default client must trigger a stack upddate for the active editor", 1, mockMgr.documentUpdateCount(myFixture.editor.document))
            Assert.assertEquals("A new default client must not trigger updates for unfocused files", 0, mockMgr.documentUpdateCount(unfocusedDoc))
        } finally {
            TezosSettingService.getSettings().setClients(emptyList())
        }
    }

    fun testUpdateOnFocusChange() {
        try {
            val mockMgr = MockMichelsonStackInfoManager.getInstance(project)

            val unfocusedFile = myFixture.configureByText(MichelsonFileType, "")
            val unfocusedDoc = myFixture.editor.document
            // close it because an update to the default client will trigger stack updates for selected files
            FileEditorManager.getInstance(project).closeFile(unfocusedFile.virtualFile)

            // after the unfocused file was opened
            setDefaultClient()

            configureByCodeAndFocus("<caret>DROP;")
            val focusedDoc = myFixture.editor.document

            Assert.assertEquals(1, mockMgr.documentUpdateCount(focusedDoc))
            Assert.assertEquals(0, mockMgr.documentUpdateCount(unfocusedDoc))

            openFileFocused(unfocusedFile)

            Assert.assertEquals(1, mockMgr.documentUpdateCount(focusedDoc))
            Assert.assertEquals(1, mockMgr.documentUpdateCount(unfocusedDoc))
        } finally {
            TezosSettingService.getSettings().setClients(emptyList())
        }
    }

    private fun configureByCodeAndFocus(text: String) {
        configureByCode(text)

        triggerOptionalEditorEvent(myFixture.file.virtualFile)
    }

    private fun openFileFocused(file: PsiFile) {
        myFixture.openFileInEditor(file.virtualFile)
        // older builds don't seem to trigger the message on the message bus
        triggerOptionalEditorEvent(file.virtualFile)
    }

    private fun triggerOptionalEditorEvent(file: VirtualFile?) {
        val newEditor = file?.let {
            FileEditorManager.getInstance(project).getSelectedEditor(it)
        }

        if (ideBuildSkipsEditorEvents()) {
            val event = FileEditorManagerEvent(FileEditorManager.getInstance(project), null, null, file, newEditor)
            project.messageBus.syncPublisher(FileEditorManagerListener.FILE_EDITOR_MANAGER).selectionChanged(event)
        }
    }

    private fun ideBuildSkipsEditorEvents() = ApplicationInfo.getInstance().build.baselineVersion <= 181
}

private fun setDefaultClient() {
    TezosSettingService.getSettings().setClients(listOf(TezosClientConfig("test default", "/home/user/alphanet.sh", true)))
    TezosSettingService.publishDefaultClientChanged()
}

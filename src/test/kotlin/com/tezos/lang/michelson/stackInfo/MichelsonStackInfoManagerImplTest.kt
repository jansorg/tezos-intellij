package com.tezos.lang.michelson.stackInfo

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.PsiFile
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
        val mockMgr = MockMichelsonStackInfoManager.getInstance(project)

        val unfocusedFile = myFixture.configureByText(MichelsonFileType, "")
        val unfocusedDoc = myFixture.editor.document
        // close it because an update to the default client will trigger stack updates for selected files
        FileEditorManager.getInstance(project).closeFile(unfocusedFile.virtualFile)

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
    }

    fun testUpdateOnFocusChange() {
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
    }

    private fun openFileFocused(file: PsiFile) {
        myFixture.openFileInEditor(file.virtualFile)
        // older builds don't seem to trigger the message on the message bus
        triggerOptionalEditorEvent(file.virtualFile)
    }
}
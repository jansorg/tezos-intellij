package com.tezos.lang.michelson.actions

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Computable
import com.tezos.lang.michelson.MichelsonFixtureTest
import com.tezos.lang.michelson.psi.MichelsonPsiFile
import org.junit.Assert
import org.junit.Test

class NewBashFileActionTest : MichelsonFixtureTest() {
    @Test
    fun testNewFile() {
        val actionManager = ActionManager.getInstance()
        val action = actionManager.getAction("tezos.NewMichelsonFile") as CreateMichelsonFileAction

        // @see https://devnet.jetbrains.com/message/5539349#5539349
        val directoryVirtualFile = myFixture.getTempDirFixture().findOrCreateDir("")
        val directory = myFixture.getPsiManager().findDirectory(directoryVirtualFile)!!

        Assert.assertEquals("Michelson Contract", action.templatePresentation.text)

        val result = ApplicationManager.getApplication().runWriteAction(Computable {
            action.createFile("bash_file", "Michelson File", directory)
        })

        assertNotNull("Expected a newly created bash file", result)
        assertTrue("Expected a newly created bash file", result is MichelsonPsiFile)
    }
}
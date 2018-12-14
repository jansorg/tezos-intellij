package com.tezos.lang.michelson.editor.stack.michelsonStackVisualization

import com.intellij.openapi.Disposable
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.util.Disposer
import com.tezos.client.MockTezosClient
import com.tezos.intellij.settings.TezosClientConfig
import com.tezos.intellij.settings.TezosSettingService
import com.tezos.lang.michelson.MichelsonFixtureTest
import com.tezos.lang.michelson.MichelsonTestUtils
import com.tezos.lang.michelson.MichelsonTestUtils.locateMichelsonFiles
import com.tezos.lang.michelson.editor.stack.MichelsonSplitEditor
import com.tezos.lang.michelson.editor.stack.MichelsonSplitEditorProvider
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author jansorg
 */
@RunWith(Parameterized::class)
class MichelsonStackVisualizationEditorTest(val file: String) : MichelsonFixtureTest() {
    companion object {
        private val dataRootPath = MichelsonTestUtils.dataPath()

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Iterable<String> {
            val files = locateMichelsonFiles(Paths.get("stackInfo")).foldRight(mutableListOf()) { path: Path, r: MutableList<String> ->
                r += dataRootPath.relativize(path).toString()
                r
            }
            return files
        }
    }

    @Before
    fun setupTest() = setUp()

    @After
    fun shutdownTest() = tearDown()

    // tests the fallback when the stack info returned by the tezos client doesn't contain the offset
    // atm the client's stack info doesn't cover annotations of instructions in all cases.
    // we still want to display the stack info in such a case, though
    @Test
    fun testFile() {
        WriteCommandAction.runWriteCommandAction(project) {
            testStackInfo(dataRootPath.resolve(file))
        }
    }

    private fun testStackInfo(michelsonFile: Path) {
        val dataFile = michelsonFile.resolveSibling(michelsonFile.fileName.toString().replace(".tz", ".stack.txt"))
        if (!Files.exists(dataFile)) {
            throw IllegalStateException("stack info file not found: $dataFile")
        }

        TezosSettingService.getSettings().setClients(listOf(TezosClientConfig("test default", "/alphanet.sh", true)))
        TezosSettingService.publishDefaultClientChanged()

        myFixture.configureByFile(michelsonFile.toString())
        MockTezosClient.loadStackInfo(myFixture.file, dataFile)

        val provider = MichelsonSplitEditorProvider()
        val splitEditor = provider.createEditor(project, myFixture.file.virtualFile) as MichelsonSplitEditor
        splitEditor.use {
            splitEditor.mainEditor.editor.document.setText(myFixture.editor.document.text)
            splitEditor.mainEditor.editor.caretModel.moveToOffset(myFixture.editor.caretModel.offset)

            splitEditor.triggerStackUpdate()

            val stackEditor = splitEditor.stackEditor
            if (michelsonFile.parent.fileName.toString() == "errors") {
                Assert.assertTrue(stackEditor.isShowingError || stackEditor.isShowingInfo)
                Assert.assertFalse(stackEditor.isShowingHTML)
            } else {
                Assert.assertFalse(stackEditor.isShowingError)
                Assert.assertFalse(stackEditor.isShowingInfo)
                Assert.assertTrue(stackEditor.isShowingHTML)
            }
        }
    }

    override fun runInDispatchThread(): Boolean = false

}

fun Disposable.use(block: Disposable.() -> Unit) {
    try {
        this.block()
    } finally {
        Disposer.dispose(this)
    }
}
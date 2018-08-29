package com.tezos.lang.michelson.highlighting

import com.tezos.lang.michelson.MichelsonFixtureTest
import com.tezos.lang.michelson.MichelsonTestUtils
import java.nio.file.Paths

class MichelsonSyntaxHighlighterTest : MichelsonFixtureTest() {
    fun testFiles() {
        val files = MichelsonTestUtils.locateMichelsonFiles(Paths.get("highlighting")).map { it.toString() }.toTypedArray()
        val result = myFixture.testHighlightingAllFiles(false, false, false, *files)
    }

    override fun isWriteActionRequired(): Boolean = false
}
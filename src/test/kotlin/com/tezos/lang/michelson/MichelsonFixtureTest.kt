package com.tezos.lang.michelson

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.tezos.lang.michelson.MichelsonTestUtils

/**
 * @author jansorg
 */
abstract class MichelsonFixtureTest : LightPlatformCodeInsightFixtureTestCase() {
    override fun getTestDataPath(): String = MichelsonTestUtils.dataPath().toString()
}


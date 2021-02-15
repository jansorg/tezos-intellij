package com.tezos.errorReporter

import java.util.Date

class MockIdeaPluginDescriptor(id: String, name: String, version: String) : AbstractMockIdeaPluginDescriptor(id, name, version) {
    override fun getReleaseDate(): Date? {
        return null
    }

    override fun getProductCode(): String? {
        return null
    }

    override fun getReleaseVersion(): Int {
        return 0
    }
}
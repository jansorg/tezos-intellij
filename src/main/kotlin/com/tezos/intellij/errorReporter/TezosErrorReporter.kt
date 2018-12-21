package com.tezos.intellij.errorReporter

import com.tezos.errorReporter.AbstractErrorReporter

/**
 * @author jansorg
 */
class TezosErrorReporter : AbstractErrorReporter(serverURL) {
    private companion object {
        const val serverURL = "https://www.plugin-dev.com/tezos/errorReceiver.pl"
    }

    override fun getReportActionText(): String = "Report to Tezos Plugin Author"
}

package com.tezos.intellij.errorReporter

import com.tezos.errorReporter.AbstractHttpErrorSubmitter

/**
 * @author jansorg
 */
class TezosErrorReporter : AbstractHttpErrorSubmitter(serverURL) {
    private companion object {
        const val serverURL = "https://www.plugin-dev.com/tezos/errorReceiver.pl"
    }

    override fun getReportActionText(): String = "Report to Tezos Plugin Author"
}

package com.tezos.intellij.errorReporter

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.extensions.PluginDescriptor
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.util.Consumer
import com.tezos.errorReporter.ErrorReporterException
import com.tezos.errorReporter.HttpEndpointReporter
import com.tezos.errorReporter.PlainTextErrorTemplate
import java.awt.Component

/**
 * @author jansorg
 */
class TezosErrorReporter : ErrorReportSubmitter() {
    private companion object {
        const val serverURL = "https://www.ansorg-it.com/tezos/errorReceiver.pl"
    }

    override fun getReportActionText(): String {
        return "Report to Tezos plugin author"
    }

    override fun submit(events: Array<out IdeaLoggingEvent>, additionalInfo: String?, parentComponent: Component, consumer: Consumer<SubmittedReportInfo>): Boolean {
        val dataContext = DataManager.getInstance().getDataContext(parentComponent)
        val project = CommonDataKeys.PROJECT.getData(dataContext)

        val task = SendErrorTask(project!!, pluginDescriptor, additionalInfo, events, parentComponent)
        val indicator = BackgroundableProcessIndicator(task)
        ProgressManager.getInstance().runProcessWithProgressAsynchronously(task, indicator)

        return true
    }

    private class SendErrorTask(project: Project, val pluginDescriptor: PluginDescriptor, val additionalInfo: String?, val events: Array<out IdeaLoggingEvent>, private val parentComponent: Component) : Task.Backgroundable(project, "Tezos Error Report", false) {
        override fun run(indicator: ProgressIndicator) {
            val template = PlainTextErrorTemplate(pluginDescriptor, additionalInfo ?: "", events)
            val reporter = HttpEndpointReporter(serverURL, null, template.toString(), pluginID = pluginDescriptor.pluginId.idString)

            val ok = try {
                reporter.send()
            } catch (e: ErrorReporterException) {
                false
            }

            ApplicationManager.getApplication().invokeLater {
                if (ok) {
                    Messages.showInfoMessage(parentComponent, "Thank you for reporting this! We're looking into it.", "Tezos Error Report")
                } else {
                    Messages.showErrorDialog(parentComponent, "An error occurred while sending the error report. Please try again later or open an issue at our Github project.", "Tezos Error Report")
                }
            }
        }
    }
}

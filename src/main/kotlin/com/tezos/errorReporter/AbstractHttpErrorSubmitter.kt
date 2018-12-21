package com.tezos.errorReporter

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
import java.awt.Component

/**
 * @author jansorg
 */
abstract class AbstractHttpErrorSubmitter(private val serverURL: String) : ErrorReportSubmitter() {
    override fun submit(events: Array<out IdeaLoggingEvent>, additionalInfo: String?, parentComponent: Component, consumer: Consumer<SubmittedReportInfo>): Boolean {
        val project = CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(parentComponent))

        val task = SendErrorTask(project!!, pluginDescriptor, additionalInfo, events, parentComponent, consumer)
        ProgressManager.getInstance().runProcessWithProgressAsynchronously(task, BackgroundableProcessIndicator(task))

        return true
    }

    private inner class SendErrorTask(project: Project, val pluginDescriptor: PluginDescriptor, val additionalInfo: String?, val events: Array<out IdeaLoggingEvent>, private val parentComponent: Component, val consumer: Consumer<SubmittedReportInfo>) : Task.Backgroundable(project, "Tezos Error Report", false) {
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
                    consumer.consume(SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE))
                } else {
                    Messages.showErrorDialog(parentComponent, "An error occurred while sending the error report. Please try again later or open an issue at our Github project.", "Tezos Error Report")
                    consumer.consume(SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.FAILED))
                }
            }
        }
    }
}

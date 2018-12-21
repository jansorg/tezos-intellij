package com.tezos.errorReporter

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.extensions.PluginDescriptor

/**
 * Formats the information about plugins as a string.
 * @author jansorg
 */
class PlainTextErrorTemplate(val plugin: PluginDescriptor, private val notes: String, private val events: Array<out IdeaLoggingEvent>) {
    override fun toString(): String {
        val name: String
        val version: String
        val ide: String

        when (plugin) {
            is IdeaPluginDescriptor -> {
                name = plugin.name
                version = plugin.version
                ide = ApplicationInfo.getInstance().build.toString()
            }
            else -> {
                name = ""
                version = ""
                ide = ""
            }
        }

        val stackDump = events.map {
            val out = StringBuilder()
            if (it.message != null && it.message.isNotEmpty()) {
                out.append(it.message).append("\n")
            }
            if (it.data != null) {
                out.append(it.data.toString()).append("\n")
            }
            if (it.throwableText != null && it.throwableText.isNotEmpty()) {
                out.append(it.throwableText)
            }
            out.toString()
        }.joinToString("\n")

        val prefix = """
            ID: ${plugin.pluginId}
            Name: $name
            Version: $version
            IDE: $ide

            Notes: $notes
        """.trimIndent()

        return prefix + "\n\n" + stackDump
    }
}
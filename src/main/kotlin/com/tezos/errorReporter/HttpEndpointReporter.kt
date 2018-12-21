package com.tezos.errorReporter

import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.net.HttpConfigurable
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

/**
 * Sends content to an HTTP endpoint. The user defined headers x-plugin and x-sender are send with the request
 * if you provide values for the corresponding constructor parameters.
 *
 * It uses IntelliJ's HTTP connection factory to automatically make use of the global settings (proxy, etc.)
 *
 * @author jansorg
 */
open class HttpEndpointReporter(private val url: String, private val senderEmail: String?, val content: String, private val pluginID: String? = null, private val followRedirects: Boolean = true) {
    companion object {
        val LOG = Logger.getInstance("#tezos.errorReporter")
    }

    /**
     * @throws ErrorReporterException
     */
    fun send(): Boolean {
        // open connection
        val connection: HttpURLConnection = HttpConfigurable.getInstance().openHttpConnection(url)
        try {
            connection.setRequestProperty("Content-type", "text/plain")
            connection.useCaches = false
            connection.doInput = true
            connection.doOutput = true
            connection.instanceFollowRedirects = followRedirects
            connection.requestMethod = "POST"
            connection.connectTimeout = TimeUnit.SECONDS.toMillis(2).toInt();
            connection.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
            senderEmail?.let { connection.setRequestProperty("x-sender", it) }
            pluginID?.let { connection.setRequestProperty("x-plugin", it) }

            val out = OutputStreamWriter(connection.outputStream, StandardCharsets.UTF_8)
            out.append(content)
            out.close()

            val responseCode = connection.responseCode
            if (responseCode != 200) {
                LOG.debug("Server returned unexpected status code: $responseCode/${connection.responseMessage}")
            }
            return responseCode == 200
        } catch (e: IOException) {
            LOG.info("Unable to connect to server", e)
            throw ErrorReporterException("Unable to connect to server", e)
        } finally {
            connection.disconnect()
        }
    }
}
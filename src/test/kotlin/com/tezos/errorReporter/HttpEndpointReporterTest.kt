package com.tezos.errorReporter

import com.google.common.collect.Maps
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.tezos.lang.michelson.MichelsonFixtureTest
import fi.iki.elonen.NanoHTTPD
import org.junit.Assert

/**
 * @author jansorg
 */
class HttpEndpointReporterTest : MichelsonFixtureTest() {
    private val httpd = object : NanoHTTPD("127.0.0.1", 0) {
        override fun serve(session: IHTTPSession): Response {
            val body = mutableMapOf<String, String>()
            session.parseBody(body)

            val copy = Maps.newConcurrentMap<String, String>()
            copy.putAll(session.headers)
            receivedHeaders = copy

            receivedContent = body.get("postData")

            if (sendError) {
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "error")
            }
            return newFixedLengthResponse(Response.Status.OK, "text/plain", "ok")
        }

        @Volatile
        var sendError: Boolean = false

        @Volatile
        var receivedContent: String? = null

        @Volatile
        var receivedHeaders: Map<String, String> = emptyMap()

        fun reset(sendError: Boolean) {
            this.sendError = sendError
            this.receivedHeaders = emptyMap()
            this.receivedContent = null
        }
    }

    override fun setUp() {
        super.setUp()
        httpd.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false)
    }

    override fun tearDown() {
        super.tearDown()
        httpd.stop()
    }

    fun testErrorReporting() {
        httpd.reset(false)

        val hostname = httpd.hostname
        val listeningPort = httpd.listeningPort
        val reporter = HttpEndpointReporter("http://$hostname:$listeningPort", "user@example.com", "body content", pluginID = "Tezos")
        val success = reporter.send()
        Assert.assertTrue(success)

        Assert.assertEquals("Tezos", httpd.receivedHeaders["x-plugin"])
        Assert.assertEquals("user@example.com", httpd.receivedHeaders["x-sender"])
        Assert.assertEquals("body content", httpd.receivedContent)
    }

    fun testErrorReportingFailed() {
        httpd.reset(true)

        val reporter = HttpEndpointReporter("http://${httpd.hostname}:${httpd.listeningPort}", null, "body content")
        val success = reporter.send()
        Assert.assertFalse(success)
    }

    fun testPluginDetailsReporting() {
        httpd.reset(false)

        val template = PlainTextErrorTemplate(MockIdeaPluginDescriptor("Tezos", "Tezos Intellij", "1.0.0"), "this is a test note", arrayOf(IdeaLoggingEvent("error!", null)))
        val content = template.toString()
        Assert.assertTrue(content.contains("this is a test note"))
        Assert.assertTrue(content.contains("error!"))

        val reporter = HttpEndpointReporter("http://${httpd.hostname}:${httpd.listeningPort}", null, content)
        val success = reporter.send()
        Assert.assertTrue(success)
        Assert.assertTrue(httpd.receivedContent!!.contains("this is a test note"))
        Assert.assertTrue(httpd.receivedContent!!.contains("error!"))
    }

}
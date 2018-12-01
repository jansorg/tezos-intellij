package com.tezos.client

import com.intellij.execution.configurations.PathEnvironmentVariableUtil
import com.intellij.openapi.diagnostic.Logger
import com.tezos.client.stack.*
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

/**
 * An error which occurred when running the Tezos client.
 */
abstract class TezosClientError(message: String, cause: Throwable?) : Exception(message, cause)

/**
 * A Tezos client error to signal that the client returned a "Node is not running" error
 */
class TezosClientNodeUnavailableError(message: String, cause: Throwable?) : TezosClientError(message, cause)

/**
 * A Tezos client error to signal that the client returned a "Node is not running" error
 */
class TezosClientUnsupportedOutputError(output: String?, cause: Throwable?) : TezosClientError("Invalid output of tezos-client detected: $output", cause)


private fun MichelsonStackParser.StackFrameContext.transform(): MichelsonStackFrame {
    return MichelsonStackFrame(this.type().transform())
}

private fun MichelsonStackParser.TypeContext.transform(): MichelsonStackType {
    val name: String
    val arguments = mutableListOf<MichelsonStackType>()
    val annotations = mutableListOf<MichelsonStackAnnotation>()

    if (this.simpleType() != null) {
        name = this.simpleType().typename.text
        annotations.addAll(this.simpleType().ANNOTATION().map { MichelsonStackAnnotation(it.text) })
    } else {
        //fixme handle single nested type
        name = this.nestedType().typename?.text ?: ""
        arguments.addAll(this.nestedType().type().map { it.transform() })
        annotations.addAll(this.nestedType().ANNOTATION().map { MichelsonStackAnnotation(it.text) })
    }

    return MichelsonStackType(name, arguments, annotations)
}

open class StandaloneTezosClient(private val executable: Path) : TezosClient {
    companion object {
        private val LOG = Logger.getInstance("#tezos.client")!!

        /**
         * Clean up output of Tezos clients.
         * This removes the disclaimer which is printed before the regular content.
         * @throws TezosClientError
         */
        fun fixTezosClientStdout(content: String): String {
            val index = content.indexOf("((types")
            return when {
                index == 0 -> content
                index > 0 -> content.substring(index)
                content.contains("Node is not running") -> throw TezosClientNodeUnavailableError("The Tezos client's not is not running.", null)
                else -> throw TezosClientUnsupportedOutputError(content, null)
            }
        }
    }

    override fun typecheck(content: String): MichelsonStackTransformations {
        // we still need to fix the stdout because older versions of alphanet.sh don't pass TEZOS_CLIENT_UNSAFE_DISABLE_DISCLAIMER to the dockerized clients
        val stdout = execClient(content)
        val fixedContent = fixTezosClientStdout(stdout)

        val parser = MichelsonStackParser(CommonTokenStream(MichelsonStackLexer(CharStreams.fromString(fixedContent))))

        val all = parser.all()
        //fixme handle parser errors
        val list = all.types().stackTransformation().map { c ->
            MichelsonStackTransformation(c.instructionStart.text.toInt(), c.instructionEnd.text.toInt(),
                    MichelsonStack(c.stack(0)!!.stackFrame().map { it.transform() }),
                    MichelsonStack(c.stack(1)!!.stackFrame().map { it.transform() }))
        }

        val errors = all.errors().error().map {
            MichelsonStackError(it.startOffset.text.toInt(), it.endOffset.text.toInt(), it.message.text)
        }

        return MichelsonStackTransformations(list, errors)
    }

    /**
     * Executes the typecheck operation with the current client.
     * @return The stdout as output by the tezos client.
     */
    private fun execClient(content: String): String {
        val outFile = Files.createTempFile("tezos-intellij-", ".txt")
        try {
            val exePath = when (Files.isExecutable(executable)) {
                true -> listOf(executable.toString())
                false -> PathEnvironmentVariableUtil.findInPath("bash")?.let {
                    listOf(it.toString(), executable.toString())
                }
            } ?: throw IllegalStateException("$executable isn't executable and shell wasn't found.")

            val command = exePath + listOf("client", "typecheck", "script", "text:$content", "--emacs", "-v")
            val builder = ProcessBuilder().redirectOutput(outFile.toFile()).command(command)
            setupClientEnv(builder.environment())

            if (LOG.isDebugEnabled) {
                LOG.debug("starting tezos client command: ${builder.command().joinToString(" ")}, env: ${builder.environment()}")
            }

            val p = builder.start()
            // fixme add timeout
            p.waitFor()

            return when {
                p.exitValue() == 0 -> {
                    val out = Files.readAllBytes(outFile).toString(StandardCharsets.UTF_8)
                    if (LOG.isDebugEnabled) {
                        LOG.debug("tezos-client finished with exit code ${p.exitValue()} and stdout ${out}")
                    }
                    out
                }
                else -> throw IllegalStateException("Tezos client exited with code ${p.exitValue()}")
            }
        } finally {
            Files.deleteIfExists(outFile)
        }
    }

    /**
     * Setup environment of the tezos client command.
     * This mirrors the implementation of the emacs mode, see https://gitlab.com/tezos/tezos/blob/master/emacs/michelson-mode.el
     */
    private fun setupClientEnv(env: MutableMap<String, String>) {
        try {
            env.putIfAbsent("TEZOS_CLIENT_UNSAFE_DISABLE_DISCLAIMER", "Y")
            env.putIfAbsent("ALPHANET_EMACS", "true")
            env.putIfAbsent("TEZOS_ALPHANET_DO_NOT_PULL", "yes")
        } catch (e: UnsupportedOperationException) {
            LOG.debug("error setting up environment for tezos-client", e)
        }
    }
}

package com.tezos.client

import com.intellij.execution.configurations.PathEnvironmentVariableUtil
import com.intellij.openapi.diagnostic.Logger
import com.tezos.client.stack.*
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

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
    private companion object {
        val LOG = Logger.getInstance("#tezos.client")
    }

    /**
     * @throws com.tezos.client.stack.TezosClientError When the client returned an error or exited with a non-zero exit code
     */
    override fun typecheck(content: String): MichelsonStackTransformations {
        val clientStdout = execClient(content)
        val fixedContent = MichelsonStackUtils.fixTezosClientStdout(clientStdout)

        val input = CharStreams.fromString(fixedContent)
        val lexer = MichelsonStackLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = MichelsonStackParser(tokens)

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
            if (LOG.isDebugEnabled) {
                LOG.debug("Running client: ${command.joinToString(" ")}")
            }

            val builder = ProcessBuilder().redirectOutput(outFile.toFile()).command(command)
            setupClientEnv(builder.environment())

            if (LOG.isDebugEnabled) {
                LOG.debug("starting tezos client command: ${builder.command().joinToString(" ")}")
            }

            val p = builder.start()
            // fixme add timeout
            p.waitFor()

            return when {
                p.exitValue() == 0 -> Files.readAllBytes(outFile).toString(StandardCharsets.UTF_8)
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
            // ignore
        }
    }
}

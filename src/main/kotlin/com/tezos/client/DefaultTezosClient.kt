package com.tezos.client

import com.intellij.execution.configurations.PathEnvironmentVariableUtil
import com.intellij.openapi.diagnostic.Logger
import com.tezos.client.stack.*
import org.antlr.v4.runtime.ANTLRInputStream
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

open class StandaloneTezosClient(protected val executable: Path) : TezosClient {
    private companion object {
        val LOG = Logger.getInstance("#tezos.client")
    }

    override fun typecheck(content: String): MichelsonStackTransformations {
        val clientStdout = typecheckResult(content)

        val correctedContent = MichelsonStackUtils.fixTezosClientStdout(clientStdout)
        val input = ANTLRInputStream(correctedContent)

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

    private fun typecheckResult(content: String): String {
        val outFile = Files.createTempFile("tezos-intellij-", ".txt")
        try {
            val exePath = when (Files.isExecutable(executable)) {
                true -> listOf(executable.toString())
                false -> PathEnvironmentVariableUtil.findInPath("bash")?.let {
                    listOf(it.toString(), executable.toString())
                }
            } ?: throw IllegalStateException("$executable isn't executable and shell wasn't found.")

            val command = exePath + listOf("client", "typecheck", "script", "text:$content", "--emacs", "-v")
            LOG.warn("Running client: ${command.joinToString(" ")}")

            val builder = ProcessBuilder().redirectOutput(outFile.toFile()).command(command)

            val p = builder.start()
            p.waitFor()
            return when {
                p.exitValue() == 0 -> Files.readAllBytes(outFile).toString(StandardCharsets.UTF_8)
                else -> throw IllegalStateException("Tezos client exited with code ${p.exitValue()}")
            }
        } finally {
            Files.deleteIfExists(outFile)
        }
    }
}

package com.tezos.client

import com.intellij.openapi.diagnostic.Logger
import com.tezos.client.stack.*
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.TimeUnit

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
    companion object {
        val LOG = Logger.getInstance("#tezos.client")
    }

    override fun typecheck(content: String): MichelsonStackTransformations? {
        val clientStdout = typecheckOutput(content) ?: return null

        val fixedContent = MichelsonStackUtils.fixTezosClientStdout(clientStdout)
        val input = ANTLRInputStream(fixedContent)

        val lexer = MichelsonStackLexer(input)
        val tokens = CommonTokenStream(lexer)

        val parser = MichelsonStackParser(tokens)

        val all = parser.all()
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

    protected open fun filename(file: Path): String {
        return file.toString()
    }

    protected open fun clientCommandArgs(vararg args: String): List<String> {
        return args.toList()
    }

    override fun typecheckOutput(content: String): String? {
        val inFile = Files.createTempFile("tezos-intellij-", ".tz")
        val outFile = Files.createTempFile("tezos-intellij-", ".txt")

        try {
            Files.write(inFile, content.toByteArray(StandardCharsets.UTF_8))

            val escaped = content
            val builder = ProcessBuilder()
                    .command(listOf(executable.toAbsolutePath().toString()) + clientCommandArgs("typecheck", "script", "text:$escaped", "--emacs", "-v"))
                    .redirectOutput(outFile.toFile())

            LOG.warn("Running client: ${builder.command()}")

            val p = builder.start()
            p.waitFor()
            return when {
                p.exitValue() == 0 -> Files.readAllBytes(outFile).toString(StandardCharsets.UTF_8)
                else -> null
            }
        } finally {
            Files.deleteIfExists(inFile)
            Files.deleteIfExists(outFile)
        }
    }
}

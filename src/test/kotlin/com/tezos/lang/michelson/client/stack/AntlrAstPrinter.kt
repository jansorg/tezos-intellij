package com.tezos.lang.michelson.client.stack

import org.antlr.v4.runtime.RuleContext
import java.io.StringWriter

object AntlrAstPrinter {
    fun astToString(ctx: RuleContext): String {
        val out = StringWriter()
        explore(out, ctx, 0)
        return out.toString()
    }

    private fun explore(out: StringWriter, ctx: RuleContext, indentation: Int) {
        val ruleName = MichelsonStackParser.ruleNames[ctx.getRuleIndex()]
        for (i in 0 until indentation) {
            out.append("  ")
        }

        out.append(ruleName).append("\n")

        for (i in 0 until ctx.getChildCount()) {
            val element = ctx.getChild(i)
            if (element is RuleContext) {
                explore(out, element, indentation + 1)
            }
        }
    }

}
package com.tezos.lang.michelson.client.stack

import org.antlr.v4.runtime.RuleContext
import java.io.StringWriter

object AntlrAstPrinter {
    fun astToString(ctx: RuleContext, addTokenText: Boolean = false): String {
        val out = StringWriter()
        explore(out, ctx, 0, addTokenText)
        return out.toString()
    }

    private fun explore(out: StringWriter, ctx: RuleContext, indentation: Int, addTokenText: Boolean = false) {
        val ruleName = MichelsonStackParser.ruleNames[ctx.getRuleIndex()]
        for (i in 0 until indentation) {
            out.append("  ")
        }

        out.append(ruleName);
        if (addTokenText) {
            out.append(" : ").append(ctx.text)
        }
        out.append("\n")

        for (i in 0 until ctx.getChildCount()) {
            val element = ctx.getChild(i)
            if (element is RuleContext) {
                explore(out, element, indentation + 1, addTokenText)
            }
        }
    }

}
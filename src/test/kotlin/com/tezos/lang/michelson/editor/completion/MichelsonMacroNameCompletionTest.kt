package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.psi.PsiElement
import com.tezos.client.MockTezosClient
import com.tezos.client.stack.*
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.psi.MichelsonPsiFile
import com.tezos.lang.michelson.stackInfo.MichelsonStackInfoManager
import com.tezos.lang.michelson.stackInfo.MockMichelsonStackInfoManager

/**
 * The created stacks span the whole file and are a simplification of real-work scenarios. They're still valid for these tests, though.
 * @author jansorg
 */
class MichelsonMacroNameCompletionTest : MichelsonCompletionTest() {
    fun testEmpty() {
        configureByCode("<caret>")
        assertCompletionsAtLeast(*MichelsonLanguage.MACRO_NAMES.toTypedArray(), type = CompletionType.BASIC)
    }

    fun testEmptySmart() {
        // no dynamic macro names when missing stack info
        configureByCode("<caret>")
        assertCompletions(type = CompletionType.SMART)
    }

    fun testDepth1() {
        prepareFile("<caret>", "nat".type())
        assertCompletions("DUP", type = CompletionType.SMART)
    }

    fun testDepth2() {
        prepareFile("<caret>", "nat".type(), pair("nat", "nat"))
        assertCompletions("DUP", "DUUP", "DIP", type = CompletionType.SMART)
    }

    fun testDepth1Pair1() {
        // pair(nat (pair nat nat))
        prepareFile("<caret>", pair("nat".type(), pair("nat", "nat")))
        assertCompletions("DUP", "CAR", "CDR", "CDAR", "CDDR", type = CompletionType.SMART)
    }

    fun testDepth1Pair2() {
        // pair(nat (pair (pair nat nat) nat))
        prepareFile("<caret>", pair("nat".type(), pair(pair("nat", "nat"), "nat".type())))
        assertCompletions("DUP", "CAR", "CDR", "CDAR", "CDDR", "CDAAR", "CDADR", type = CompletionType.SMART)
    }

    fun testDepth2Pair1() {
        // pair(nat nat)
        // pair(nat (pair nat nat))
        prepareFile("<caret>", pair("nat", "nat"), pair("nat".type(), pair("nat", "nat")))
        assertCompletions("CAR", "CDR", "DUP", "DUUP", "DIP", type = CompletionType.SMART)
    }

    private fun prepareFile(content: String, vararg stackTypes: MichelsonStackType) {
        val file = configureByCode(content).first
        MockTezosClient.addTypes(myFixture.file, stackOf(file, *stackTypes))
        refreshStackInfo()
    }

    private fun refreshStackInfo() {
        MockMichelsonStackInfoManager.getInstance(project).forceUpdate(myFixture.editor)
    }

    // the fixture's editor isn't a split editor
    // the split editor registers the document in the stack manager, therefore we have to do it here instead
    override fun configureByCode(code: String, allowWhitespace: Boolean): Pair<MichelsonPsiFile, PsiElement?> {
        val result = super.configureByCode(code, allowWhitespace)
        MichelsonStackInfoManager.getInstance(project).registerDocument(myFixture.editor.document, testRootDisposable)
        return result
    }

    private fun stackOf(element: PsiElement?, vararg stackTypes: MichelsonStackType): MichelsonStackTransformations {
        val range = element!!.textRange
        val stack = MichelsonStack(stackTypes.map { MichelsonStackFrame(it) })
        return MichelsonStackTransformations(listOf(MichelsonStackTransformation(range.startOffset, range.endOffset, stack, stack)), emptyList())
    }

    fun MichelsonStackType.frame() = MichelsonStackFrame(this)

    fun String.type(): MichelsonStackType = MichelsonStackType(this)

    fun pair(a: String, b: String): MichelsonStackType = MichelsonStackType("pair", listOf(a.type(), b.type()))
    fun pair(a: MichelsonStackType, b: MichelsonStackType): MichelsonStackType = MichelsonStackType("pair", listOf(a, b))
}
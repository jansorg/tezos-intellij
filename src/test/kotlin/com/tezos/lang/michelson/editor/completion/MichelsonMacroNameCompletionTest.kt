package com.tezos.lang.michelson.editor.completion

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.psi.PsiElement
import com.tezos.client.MockTezosClient
import com.tezos.client.stack.*
import com.tezos.lang.michelson.lang.MichelsonLanguage
import com.tezos.lang.michelson.lang.macro.AssertMacroMetadata
import com.tezos.lang.michelson.lang.macro.CompareMacroMetadata
import com.tezos.lang.michelson.lang.macro.ConditionalMacroMetadata
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
        assertCompletions("FAIL", type = CompletionType.SMART)
    }

    fun testDepth1() {
        prepareFile("<caret>", "nat".type())
        assertCompletionsAtLeast("DUP", type = CompletionType.SMART)
    }

    fun testDepth2() {
        prepareFile("<caret>", "nat".type(), pair("nat", "nat"))
        assertCompletionsAtLeast("DUP", "DUUP", "DIP", type = CompletionType.SMART)
    }

    fun testDepth1Pair1() {
        // pair(nat (pair nat nat))
        prepareFile("<caret>", pair("nat".type(), pair("nat", "nat")))
        assertCompletionsAtLeast("DUP", "CAR", "CDR", "CDAR", "CDDR", type = CompletionType.SMART)
    }

    fun testDepth1Pair2() {
        // pair(nat (pair (pair nat nat) nat))
        prepareFile("<caret>", pair("nat".type(), pair(pair("nat", "nat"), "nat".type())))
        assertCompletionsAtLeast("DUP", "CAR", "CDR", "CDAR", "CDDR", "CDAAR", "CDADR", type = CompletionType.SMART)
    }

    fun testDepth2Pair1() {
        // pair(nat nat)
        // pair(nat (pair nat nat))
        prepareFile("<caret>", pair("nat", "nat"), pair("nat".type(), pair("nat", "nat")))
        assertCompletionsAtLeast("CAR", "CDR", "DUP", "DUUP", "DIP", type = CompletionType.SMART)
    }

    fun testDepth1CompareMacro() {
        // int int
        prepareFile("<caret>", "int".type(), "int".type())
        assertCompletionsAtLeast(*CompareMacroMetadata.NAMES.toTypedArray(), type = CompletionType.SMART)
    }

    fun testDepth1CompareMacroNonComparable() {
        // int int
        prepareFile("<caret>", pair("int", "int"), pair("int", "int"))
        assertCompletionsAtLeast("CAR", "CDR", "DIP", "DUP", "DUUP", type = CompletionType.SMART)
    }

    fun testDepth1AssertBool() {
        // bool
        prepareFile("<caret>", "bool".type())
        assertCompletionsAtLeast(*(setOf("ASSERT") + AssertMacroMetadata.EQ_NAMES).toTypedArray(), type = CompletionType.SMART)
    }

    fun testDepth1AssertComparable() {
        // int
        // int
        prepareFile("<caret>", "int".type(), "int".type())
        assertCompletionsAtLeast(*(AssertMacroMetadata.CMPEQ_NAMES).toTypedArray(), type = CompletionType.SMART)
    }

    fun testDepth1AssertOption() {
        // (option int int)
        prepareFile("<caret>", option("int".type(), "int".type()))
        assertCompletionsAtLeast("ASSERT_SOME", "ASSERT_NONE", type = CompletionType.SMART)
    }

    fun testDepth1AssertOr() {
        // (option int int)
        prepareFile("<caret>", option("int".type(), "int".type()))
        assertCompletionsAtLeast("ASSERT_SOME", "ASSERT_NONE", type = CompletionType.SMART)
    }


    fun testDepth1Conditional1() {
        prepareFile("<caret>", option("int", "int"))
        assertCompletionsAtLeast("IF_SOME", type = CompletionType.SMART)
    }

    fun testDepth1Conditional2() {
        prepareFile("<caret>", "int".type())
        assertCompletionsAtLeast(*ConditionalMacroMetadata.NAMES_INT.toTypedArray(), type = CompletionType.SMART)
    }

    fun testDepth1Conditional3() {
        prepareFile("<caret>", "bool".type())
        assertCompletionsNoneOf(*ConditionalMacroMetadata.NAMES_INT.toTypedArray(), type = CompletionType.SMART)
    }

    fun testDepth1Conditional4() {
        prepareFile("<caret>", "int".type(), "int".type())
        assertCompletionsAtLeast(*ConditionalMacroMetadata.NAMES_CMP.toTypedArray(), type = CompletionType.SMART)
        assertCompletionsAtLeast(*ConditionalMacroMetadata.NAMES_INT.toTypedArray(), type = CompletionType.SMART)
        assertCompletionsNoneOf("IF_SOME", type = CompletionType.SMART)
    }

    fun testDepth2Pair() {
        prepareFile("<caret>", "int".type(), "int".type())
        assertCompletionsAtLeast("PAIR", type = CompletionType.SMART)
    }

    fun testDepth3Pair() {
        prepareFile("<caret>", "int".type(), "int".type(), "int".type())
        assertCompletionsAtLeast("PAIR", "PAPAIR", "PPAIIR", type = CompletionType.SMART)
    }

    fun testDepth4Pair() {
        prepareFile("<caret>", "int".type(), "int".type(), "int".type(), "int".type())
        assertCompletionsAtLeast("PAIR", "PAPAIR", "PPAIIR", "PAPAPAIR", "PAPPAIIR", "PPAPAIIR", "PPPAIIIR", type = CompletionType.SMART)
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

    fun nested(name: String, a: String, b: String): MichelsonStackType = MichelsonStackType(name, listOf(a.type(), b.type()))
    fun nested(name: String, a: MichelsonStackType, b: MichelsonStackType): MichelsonStackType = MichelsonStackType(name, listOf(a, b))

    fun pair(a: String, b: String) = nested("pair", a, b)
    fun pair(a: MichelsonStackType, b: MichelsonStackType) = nested("pair", a, b)

    fun option(a: String, b: String) = nested("option", a, b)
    fun option(a: MichelsonStackType, b: MichelsonStackType) = nested("option", a, b)

    fun or(a: String, b: String) = nested("or", a, b)
    fun or(a: MichelsonStackType, b: MichelsonStackType) = nested("or", a, b)
}
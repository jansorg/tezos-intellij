package com.tezos.lang.michelson.editor.parameterInfo

import com.intellij.lang.parameterInfo.ParameterInfoHandler
import com.intellij.psi.PsiElement
import com.tezos.lang.michelson.MichelsonFixtureTest
import org.jetbrains.annotations.NotNull

/**
 * @author jansorg
 */
abstract class AbstractMichelsonInstructionParameterInfoTest : MichelsonFixtureTest() {
    /**
     * Imitates pressing of Ctrl+P; fails if results are not as expected.
     * @param offset offset of 'cursor' where Ctrl+P is pressed.
     * @return a [Collector] with collected hint info.
     */
    @NotNull
    protected fun requestParamInfo(offset: Int): ParamInfoCollector {
        val handler = createParamInfo()
        val collector = ParamInfoCollector(myFixture.file, offset, myFixture.editor)
        collector.parameterOwner = handler.findElementForParameterInfo(collector)

        if (collector.parameterOwner != null) {
            handler.updateParameterInfo(collector.parameterOwner as PsiElement, collector)

            assert(collector.itemsToShow!!.size == 1)
            handler.updateUI(collector.itemsToShow!![0] as? PsiElement, collector)
        }

        return collector
    }

    protected open fun createParamInfo(): ParameterInfoHandler<PsiElement, PsiElement> = MichelsonParameterInfoHandler()

}
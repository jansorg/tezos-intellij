package com.tezos.lang.michelson.editor.parameterInfo

import com.google.common.collect.Maps
import com.intellij.lang.parameterInfo.CreateParameterInfoContext
import com.intellij.lang.parameterInfo.ParameterInfoHandler
import com.intellij.lang.parameterInfo.UpdateParameterInfoContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.testFramework.utils.parameterInfo.MockParameterInfoUIContext
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
    protected fun requestParamInfo(offset: Int): Collector {
        val handler = createParamInfo()
        val collector = Collector(myFixture.file, offset, myFixture.editor)
        collector.parameterOwner = handler.findElementForParameterInfo(collector)

        if (collector.parameterOwner != null) {
            handler.updateParameterInfo(collector.parameterOwner as PsiElement, collector)

            assert(collector.itemsToShow!!.size == 1)
            handler.updateUI(collector.itemsToShow!![0] as? PsiElement, collector)
        }

        return collector
    }

    protected open fun createParamInfo(): ParameterInfoHandler<PsiElement, PsiElement> = MichelsonParameterInfoHandler() as ParameterInfoHandler<PsiElement, PsiElement>

    class Collector(private val file: PsiFile, private val offset: Int, private val editor: Editor) : MockParameterInfoUIContext<PsiElement?>(null), CreateParameterInfoContext, UpdateParameterInfoContext {
        private var highlightedParam: Any? = null
        private var hidden: Boolean = true
        private var paramIndex: Int = -1
        private val componentStatus: MutableMap<Int, Boolean> = Maps.newConcurrentMap()
        private var paramOwner: PsiElement? = null
        private var items: Array<out Any>? = null
        private var highlighted: PsiElement? = null

        override fun setParameterOwner(o: PsiElement?) {
            this.paramOwner = o
        }

        override fun setHighlightedParameter(parameter: Any?) {
            this.highlightedParam = parameter
        }

        override fun getParameterOwner(): PsiElement? = this.paramOwner

        override fun removeHint() {
            this.hidden = true
        }

        override fun setUIComponentEnabled(index: Int, enabled: Boolean) {
            this.componentStatus[index] = enabled
        }

        override fun isUIComponentEnabled(index: Int): Boolean {
            return this.componentStatus.getOrDefault(index, false)
        }

        override fun getObjectsToView(): Array<Any> {
            return emptyArray()
        }

        override fun setCurrentParameter(index: Int) {
            this.paramIndex = index
        }

        override fun getFile(): PsiFile = file

        override fun getEditor(): Editor = editor

        override fun getProject(): Project = file.project

        override fun getOffset(): Int = offset

        override fun getItemsToShow(): Array<out Any>? = items

        override fun setHighlightedElement(elements: PsiElement?) {
            this.highlighted = elements
        }

        override fun getHighlightedElement(): PsiElement? = this.highlighted

        override fun showHint(element: PsiElement?, offset: Int, handler: ParameterInfoHandler<*, *>?) {}

        override fun setItemsToShow(items: Array<out Any>?) {
            this.items = items
        }

        override fun getParameterListStart(): Int = 0
    }
}
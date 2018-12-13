package com.tezos.intellij.ui

import com.intellij.openapi.diagnostic.Logger
import com.intellij.ui.components.JBTextField
import com.intellij.util.Function

/**
 * A factory for text fields.
 * Expandable text fields are not available on all builds we're supporting. Therefore we have to use
 * reflection to return a expandable text field when it's available.
 * @author jansorg
 */
object TextFieldFactory {
    private val LOG = Logger.getInstance("#tezos.ui")
    private const val CLASS_NAME = "com.intellij.ui.components.fields.ExpandableTextField"

    fun createExpandableTextField(): JBTextField {
        try {
            val clazz = TextFieldFactory.javaClass.classLoader.loadClass(CLASS_NAME)
            val ctor = clazz.getConstructor(Function::class.java, Function::class.java)
            return ctor.newInstance(Parser(), Joiner()) as JBTextField
        } catch (e: Exception) {
            LOG.debug("Exception in createExpandableTextField()", e)
        }

        return JBTextField()
    }

    // implement interface to create a compatible parameter value at runtime
    // reflection isn't accepting lambdas
    private class Parser : Function<String, MutableList<String>> {
        override fun `fun`(value: String): MutableList<String> {
            return mutableListOf(value)
        }
    }

    private class Joiner: Function<MutableList<String>, String> {
        override fun `fun`(lines: MutableList<String>): String {
            return lines.joinToString()
        }
    }
}
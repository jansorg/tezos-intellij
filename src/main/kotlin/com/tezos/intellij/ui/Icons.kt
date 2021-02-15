package com.tezos.intellij.ui

import com.intellij.openapi.util.IconLoader

/**
 * The icons of the Tezos plugin.
 *
 * @author jansorg
 */
object Icons {
    val Tezos = IconLoader.getIcon("/icons/tezos.png", Icons.javaClass)

    val StackAnnotations = IconLoader.getIcon("/icons/stack-annotations.png", Icons.javaClass)
    val StackUnchanged = IconLoader.getIcon("/icons/stack-unchanged.png", Icons.javaClass)
    val StackColored = IconLoader.getIcon("/icons/stack-colored.png", Icons.javaClass)
    val StackIndentation = IconLoader.getIcon("/icons/stack-indentation.png", Icons.javaClass)
}
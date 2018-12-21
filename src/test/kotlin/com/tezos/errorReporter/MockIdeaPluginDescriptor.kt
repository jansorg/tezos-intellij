package com.tezos.errorReporter

import com.intellij.ide.plugins.HelpSetPath
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.components.ComponentConfig
import com.intellij.openapi.extensions.PluginId
import org.jdom.Element
import java.io.File

data class MockIdeaPluginDescriptor(val id: String, private val name: String, private val version: String) : IdeaPluginDescriptor {
    override fun getChangeNotes(): String = ""

    override fun getVendor(): String = ""

    override fun getName(): String = name

    override fun getOptionalDependentPluginIds(): Array<PluginId> = emptyArray()

    override fun getActionsDescriptionElements(): MutableList<Element> = mutableListOf()

    override fun getHelpSets(): Array<HelpSetPath> = emptyArray()

    override fun getCategory(): String = ""

    override fun getPluginId(): PluginId = PluginId.getId(id)

    override fun allowBundledUpdate(): Boolean = false
    override fun setEnabled(enabled: Boolean) {}

    override fun getVersion(): String = version

    override fun getDescription(): String? = null

    override fun getVendorLogoPath(): String = ""

    override fun getDownloads(): String = ""

    override fun getUrl(): String = ""

    override fun isBundled(): Boolean = false

    override fun getResourceBundleBaseName(): String = ""

    override fun getVendorEmail(): String = ""

    override fun getVendorUrl(): String = ""

    override fun getAppComponents(): Array<ComponentConfig> = emptyArray()

    override fun getPath(): File = File("")

    override fun isEnabled(): Boolean = true

    override fun getUntilBuild(): String = ""

    override fun getDependentPluginIds(): Array<PluginId> = emptyArray()

    override fun getSinceBuild(): String = ""

    override fun getModuleComponents(): Array<ComponentConfig> = emptyArray()

    override fun getPluginClassLoader(): ClassLoader = TODO("")

    override fun getUseIdeaClassLoader(): Boolean = false

    override fun getProjectComponents(): Array<ComponentConfig> = emptyArray()
}
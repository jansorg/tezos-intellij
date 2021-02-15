package com.tezos.errorReporter

import com.intellij.ide.plugins.IdeaPluginDependency
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.extensions.PluginId
import java.io.File
import java.nio.file.Path
import java.util.Date

abstract class AbstractMockIdeaPluginDescriptor(private val id: String, private val name: String, private val version: String) : IdeaPluginDescriptor {
    override fun getChangeNotes(): String = ""

    override fun getVendor(): String = ""

    override fun getName(): String = name
    override fun getProductCode(): String? {
        TODO("Not yet implemented")
    }

    override fun getReleaseDate(): Date? {
        TODO("Not yet implemented")
    }

    override fun getReleaseVersion(): Int {
        return 0
    }

    override fun isLicenseOptional(): Boolean {
        return false
    }

    override fun getOptionalDependentPluginIds(): Array<PluginId> = emptyArray()

    override fun getCategory(): String = ""

    override fun getPluginId(): PluginId = PluginId.getId(id)

    override fun getPluginClassLoader(): ClassLoader {
        TODO("Not yet implemented")
    }

    override fun allowBundledUpdate(): Boolean = false
    override fun setEnabled(enabled: Boolean) {}

    override fun getDependencies(): MutableList<IdeaPluginDependency> {
        return mutableListOf()
    }

    override fun getVersion(): String = version

    override fun getDescription(): String? = null

    override fun getUrl(): String = ""

    override fun isBundled(): Boolean = false

    override fun getResourceBundleBaseName(): String = ""

    override fun getVendorEmail(): String = ""

    override fun getVendorUrl(): String = ""

    override fun getPath(): File = File("")

    override fun getPluginPath(): Path {
        TODO("Not yet implemented")
    }

    override fun isEnabled(): Boolean = true

    override fun getUntilBuild(): String = ""

    override fun getDependentPluginIds(): Array<PluginId> = emptyArray()

    override fun getSinceBuild(): String = ""
}
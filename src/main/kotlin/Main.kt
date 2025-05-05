package com.theendercore

import com.theendercore.data.PackSettings
import com.theendercore.utils.workInTempDir
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File


// SETTING
val PACK_NAME = "AncientDebris"
val PACK_SETTINGS = PackSettings("§5by Ender", 200, "1.5.0")
val JSON = Json { prettyPrint = true }

fun main() = workInTempDir { tempDir ->
    // Create pack.mcmeta file
    val packMeta = tempDir.resolve("pack.mcmeta")
    packMeta.writeText(JSON.encodeToString(PACK_SETTINGS.pack()))

    // Create export Folders
    val exportFolder = File("./expo")
    exportFolder.resolve("./rainbow").mkdirs()


}

fun t() {}
package com.theendercore

import com.theendercore.data.PackSettings
import com.theendercore.utils.createZipFile2
import com.theendercore.utils.getOrCreateDir
import com.theendercore.utils.putFile
import com.theendercore.utils.workInTempDir
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileInputStream


// SETTING
const val PACK_NAME = "AncientDebris"
val PACK_SETTINGS = PackSettings("§5by Ender", 200, "1.5.0")
val JSON = Json { prettyPrint = true }

val COLORS = File("colors")
val EXPORT = File("./expo")

val PACKS = mutableMapOf<String, File>()

fun main() {
    // Makes packs
    workInTempDir { tempDir ->
        // Create pack.mcmeta file
        val packMeta = tempDir.resolve("pack.mcmeta")
        packMeta.writeText(JSON.encodeToString(PACK_SETTINGS.pack()))

        // Make sure export folder is empty
        getOrCreateDir(EXPORT.path)

        // creates packs
        COLORS.listFiles()?.forEach { processColorDir(it, packMeta) }
    }
    // Upload packs

}

fun processColorDir(dir: File, packMeta: File) {
    if (!dir.isDirectory) return
    var expo = EXPORT
    if (dir.name.endsWith("Rainbow")) expo = expo.resolve("rainbow")
    expo.mkdirs()
    val zip = expo.resolve("${dir.name}$PACK_NAME-${PACK_SETTINGS.packVersion}.zip")
    PACKS[dir.name] = zip
    zip.createZipFile2 { zip ->
        zip.putFile("pack.mcmeta") { FileInputStream(packMeta).use { fis -> fis.copyTo(it) } }
        for (file in dir.walk()) {
            if (!file.isFile) continue
            zip.putFile("assets/minecraft/textures/block/ancient_debris_${file.name}") {
                FileInputStream(file).use { fis -> fis.copyTo(it) }
            }
            if (file.name == "side.png") {
                zip.putFile("pack.png") { FileInputStream(file).use { fis -> fis.copyTo(it) } }
            }
        }
    }
}

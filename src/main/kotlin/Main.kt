package com.theendercore

import com.theendercore.data.PackSettings
import com.theendercore.utils.createZipFile2
import com.theendercore.utils.getOrCreateDir
import com.theendercore.utils.putFile
import com.theendercore.utils.workInTempDir
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.hypherionmc.curseupload.CurseUploadApi
import me.hypherionmc.curseupload.constants.CurseChangelogType
import me.hypherionmc.curseupload.constants.CurseReleaseType
import me.hypherionmc.curseupload.requests.CurseArtifact
import java.io.File
import java.io.FileInputStream


// SETTING
const val PACK_NAME = "AncientDebris"
val PACK_SETTINGS = PackSettings("§5by Ender", 200, "1.5.0")
val JSON = Json { prettyPrint = true }

val COLORS = File("colors")
val EXPORT = File("./expo")

const val MAIN = "OriginalGreen"
const val RAINBOW = "Rainbow"

const val CHANGELOG = "- 1.21 update"
val VERSIONS = setOf(
    "1.20", "1.20.1", "1.20.2", "1.20.3", "1.20.4", "1.20.5", "1.20.6",
    "1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4", "1.21.5", // "1.21.6"
)
const val CURSE_DEBUG = false

// IDS
const val CURSE_ID = 474155L
const val MODRINTH_ID = "yIUjpHq1"

// VARS
var CURSE_KEY = ""
var MODRINTH_KEY = ""
val PACKS = mutableMapOf<String, File>()
fun main() {
    loadKeys()
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
    val sortedFiles = sortFiles()
    postToPlatforms(sortedFiles)
}

fun postToPlatforms(sortedFiles: List<Pair<File, MutableSet<File>>>) {
    val uploadApi = CurseUploadApi(CURSE_KEY)
    uploadApi.isDebug = CURSE_DEBUG
    var counter = 0
    for ((main, additional) in sortedFiles) {
        val ca = CurseArtifact(main, CURSE_ID)
        ca.displayName(main.name.replace("-", " ").replace(".zip", ""))
        ca.changelogType(CurseChangelogType.MARKDOWN)
        ca.changelog(CHANGELOG)
        if (counter != 0) ca.releaseType(CurseReleaseType.ALPHA)
        for (ver in VERSIONS) ca.addGameVersion(ver)
        for (add in additional) ca.addAdditionalFile(add)
        uploadApi.upload(ca)
        counter++
    }
}

fun loadKeys() {
    val keys = File("./.env")
    require(keys.exists()) { "\".env\" File doesn't exists!" }
    require(keys.isFile) { "\".env\" is not a file!" }
    keys.readLines().forEach {
        val rawVar = it.split("=")
        val pair = rawVar[0] to rawVar.drop(1).joinToString()
        when (pair.first) {
            "CURSE" -> CURSE_KEY = pair.second
            "MODRINTH" -> MODRINTH_KEY = pair.second
        }
    }
    require(CURSE_KEY.length > 5) { " Failed to load CURSE_KEY!" }
    require(MODRINTH_KEY.length > 5) { " Failed to load MODRINTH_KEY!" }
}

fun sortFiles(): List<Pair<File, MutableSet<File>>> {
    var main: File? = null
    val mainAdditional = mutableSetOf<File>()
    var rainbow: File? = null
    val rainbowAdditional = mutableSetOf<File>()

    for ((name, file) in PACKS) {
        if (name.contains(RAINBOW)) {
            if (name == RAINBOW) rainbow = file
            else rainbowAdditional.add(file)
            continue
        }
        if (name == MAIN) {
            main = file
            continue
        }
        mainAdditional.add(file)

    }

    require(main != null) { "Failed to get Main File!" }
    require(rainbow != null) { "Failed to get Rainbow File!" }

    return listOf(main to mainAdditional, rainbow to rainbowAdditional)
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

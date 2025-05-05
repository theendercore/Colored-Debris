package com.theendercore.utils

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.PathWalkOption
import kotlin.io.path.walk

const val TEMP_DIR = "./temp"

fun workInTempDir(fn: (tempDir: File) -> Unit) {
    val tempDir = File(TEMP_DIR)
    if (tempDir.exists()) tempDir.deleteRecursively()
    tempDir.mkdirs()
    fn(tempDir)
}

fun getOrCreateDir(path: String): File {
    val dir = File(path)
    if (dir.exists()) dir.deleteRecursively()
    dir.mkdirs()
    return dir
}

fun File.getFirstFileIn(subfolder: String): File? {
    val folder = this.resolve(subfolder)
    return if (folder.exists()) folder.listFiles()?.firstOrNull() else null
}

// Zip Helper
fun File.zipToFs(): FileSystem = this.toPath().zipToFs()
fun Path.zipToFs(): FileSystem = FileSystems.newFileSystem(this)

@OptIn(ExperimentalPathApi::class)
fun File.walkZip(vararg options: PathWalkOption, forEach: (path: Path, rootPath: Path) -> Unit) =
    this.toPath().walkZip(*options, forEach = forEach)

@OptIn(ExperimentalPathApi::class)
fun Path.walkZip(vararg options: PathWalkOption, forEach: (path: Path, rootPath: Path) -> Unit) {
    val root = this.zipToFs().rootDirectories.first() ?: error("No root directory")
    root.walk(*options).iterator().forEach { forEach(it, root) }
}

@OptIn(ExperimentalPathApi::class)
fun File.walkZip(vararg options: PathWalkOption): Pair<Path, Sequence<Path>> = this.toPath().walkZip(*options)

@OptIn(ExperimentalPathApi::class)
fun Path.walkZip(vararg options: PathWalkOption): Pair<Path, Sequence<Path>> {
    val root = this.zipToFs().rootDirectories.first() ?: error("No root directory")
    return root to root.walk(*options)
}

fun createZipFile(outputPath: File, fn: (ZipOutputStream) -> Unit): Boolean {
    return try {
        ZipOutputStream(BufferedOutputStream(FileOutputStream(outputPath))).use(fn)
        true
    } catch (e: Exception) {
        println("Error creating ZIP file: ${e.message}")
        false
    }
}

fun ZipOutputStream.putFile(path: String, fn: (ZipOutputStream) -> Unit) = try {
    this.putNextEntry(ZipEntry(path))
    fn(this)
    this.closeEntry()
} catch (e: Exception) {
    println("Error adding file [$path] to zip.\n${e.message}")
}


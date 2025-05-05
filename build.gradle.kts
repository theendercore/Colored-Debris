plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.0.10"
}

group = "com.theendercore"
version = "1.5.0"

repositories {
    mavenCentral()
    maven("https://maven.firstdarkdev.xyz/releases")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

    implementation("dev.masecla:Modrinth4J:2.2.0")
    implementation("me.hypherionmc.modutils:CurseUpload4j:1.0.12")

//    testImplementation(kotlin("test"))
}

/*tasks.test {
    useJUnitPlatform()
}*/

kotlin {
    jvmToolchain(21)
}
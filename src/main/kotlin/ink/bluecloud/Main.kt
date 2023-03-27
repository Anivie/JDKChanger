package ink.bluecloud

import java.io.File
import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val pathList = File(System.getenv("JAVA_HOME")).parentFile.listFiles()?.associate {
        it.name to it.absolutePath
    } ?: throw IllegalArgumentException("JAVA_HOME is not set")

    val javaHome = System.getenv("JAVA_HOME")

    //generate powershell script
    pathList.forEach {
        val powershell = """
            $ env:JAVA_HOME = "${it.value}"
            $ env:PATH = "${System.getenv("PATH").replace(javaHome, it.value)}"
        """.trimIndent().replace(" ", "")

        Files.writeString(Paths.get("${it.key}.ps1"), powershell)
    }

    //generate bat script
    pathList.forEach {
        val bat = """
            set JAVA_HOME=${it.value}
            set PATH=${System.getenv("PATH").replace(javaHome, it.value)}
        """.trimIndent()

        Files.writeString(Paths.get("${it.key}.bat"), bat)
    }
}
package ru.tbank.education.school.lesson9

import java.io.File
import java.util.zip.ZipOutputStream
import java.io.FileOutputStream
import java.io.FileInputStream
import java.util.zip.ZipEntry

class Archiever {
    private val Extentions = setOf(".txt", ".log")

    fun create(sourceDirPath: String, zipFilePath: String) {
        val sourceDir = File(sourceDirPath)
        val zipFile = File(zipFilePath)

        if (!(sourceDir.exists() and sourceDir.isDirectory)) {
            println("Ошибка с исходным каталогом: $sourceDirPath")
            return
        }

        if (!zipFile.name.endsWith("zip")) {
            println("Имя архива имеет недопустимое разрешение.")
            return
        }

        println("Создание архива: $zipFilePath, из каталога: $sourceDirPath")

        var zipOutputStream: ZipOutputStream? = null
        var fileOutputStream: FileOutputStream? = null

        try {
            fileOutputStream = FileOutputStream(zipFile)
            zipOutputStream = ZipOutputStream(fileOutputStream)

            val filesToArchieve = mutableListOf<File>()
            collectFiles(sourceDir, sourceDir, filesToArchieve)

            if (filesToArchieve.isEmpty()) {
                println("Нет файлов для архивирования.")
                return
            }

            for (file in filesToArchieve) {
                addFileToZip(file, sourceDir, zipOutputStream)
            }

            println("Архив был успешно создан: ${zipFile.absolutePath}")
            println("Всего файлов: ${filesToArchieve.size}")
        } catch (e: Exception) {
            println("Ошибка при создании архива: ${e.message}")
            e.printStackTrace()
        } finally {
            try {
                zipOutputStream?.close()
            } catch (e: Exception) {
                println("Ошибка при закрытии ZipOutputStream: ${e.message}")
            }

            try {
                fileOutputStream?.close()
            } catch (e: Exception) {
                println("Ошибка при закрытии FileOutputStream: ${e.message}")
            }
        }
    }

    private fun collectFiles(currentDir: File, baseDir: File, result: MutableList<File>) {
        val files = currentDir.listFiles() ?: return

        for (file in files) {
            if (file.isDirectory) {
                collectFiles(file, baseDir, result)
            } else if (file.isFile && hasAllowedExtension(file)) {
                result.add(file)
            }
        }
    }

    private fun hasAllowedExtension(file: File): Boolean {
        val fileName = file.name.lowercase()
        return Extentions.any { fileName.endsWith(it) }
    }

    private fun addFileToZip(file: File, baseDir: File, zipOutputStream: ZipOutputStream) {
        var fileInputStream: FileInputStream? = null

        try {
            val relativePath = file.relativeTo(baseDir).path.replace("\\", "/")

            val zipEntry = ZipEntry(relativePath)
            zipOutputStream.putNextEntry(zipEntry)

            fileInputStream = FileInputStream(file)
            val buffer = ByteArray(1024)
            var length: Int
            var totalBytes = 0L

            while (fileInputStream.read(buffer).also { length = it } > 0) {
                zipOutputStream.write(buffer, 0, length)
                totalBytes += length
            }

            zipOutputStream.closeEntry()

            println("Добавлен файл: $relativePath $totalBytes байт.")
        } catch (e: Exception) {
            println("Ошибка при добавлении файла: ${file.name} ${e.message}")
        } finally {
            try {
                fileInputStream?.close()
            } catch (e: Exception) {
                println("Ошибка при закрытии потока для: ${file.name} ${e.message}")
            }
        }
    }
}

fun main() {
    val archiever = Archiever()

    archiever.create("/Users/anasty/faculty_day_7/lesson9/testdir", "/Users/anasty/faculty_day_7/lesson9/archive.zip")
}
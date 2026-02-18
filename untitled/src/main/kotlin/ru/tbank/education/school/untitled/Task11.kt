package ru.tbank.education.school.untitled

import kotlinx.coroutines.*
import java.io.File
import java.net.URL

data class DownloadStats(
    val totalTimeMs: Long,
    val successCount: Int,
    val failureCount: Int
)

object ImageDownloader {

    fun run(urls: List<String>, outputDir: String): DownloadStats = runBlocking {

        val folder = File(outputDir)
        folder.mkdirs()

        val startTime = System.currentTimeMillis()

        var successCount = 0
        var failureCount = 0

        val tasks = mutableListOf<Deferred<Boolean>>()

        for (i in urls.indices) {
            val url = urls[i]
            val number = i + 1

            val task = async(Dispatchers.IO) {
                downloadOneImage(url, outputDir, number)
            }

            tasks.add(task)
        }

        for (i in tasks.indices) {
            val task = tasks[i]
            val number = i + 1

            val isSuccess = task.await()

            if (isSuccess) {
                successCount++
            } else {
                failureCount++
            }

            println("Downloaded $number/${urls.size}")
        }

        val totalTime = System.currentTimeMillis() - startTime

        println()
        println("Statistics:")
        println("Total time: $totalTime ms")
        println("Successful: $successCount")
        println("Failure: $failureCount")

        DownloadStats(totalTime, successCount, failureCount)
    }

    private fun downloadOneImage(url: String, outputDir: String, number: Int): Boolean {
        return try {
            val imageBytes = URL(url).readBytes()

            val file = File(outputDir, "image_$number.jpg")
            file.writeBytes(imageBytes)

            true
        } catch (error: Exception) {
            println("Error $number: ${error.message}")
            false
        }
    }
}

fun main() {
    val urls = mutableListOf<String>()
    for (i in 1..10) {
        urls.add("https://picsum.photos/200/300?random=$i")
    }

    ImageDownloader.run(urls, "downloads")
}
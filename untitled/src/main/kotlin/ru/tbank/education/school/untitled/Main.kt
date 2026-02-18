package ru.tbank.education.school.untitled

import kotlinx.coroutines.*
import java.math.BigInteger
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.io.File

object CreateThreads {
    fun run(): List<Thread> {
        val threadNames = listOf("Thread-A", "Thread-B", "Thread-C")

        val threads = threadNames.map { name ->
            Thread {
                repeat(5) {
                    println("Hello from: $name")
                    Thread.sleep(500)
                }
            }.apply {
                this.name = name
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        return threads
    }
}

object RaceCondition {
    fun run(): Int {
        var counter: Int = 0
        val threadList = List(10) {
            Thread {
                repeat(1000) {
                    counter += 1
                }
            }.apply { start() }
        }
        threadList.forEach { (it.join()) }
        println(counter)
        return counter
    }
}

object SynchronizedCounter {
    fun run(): Int {
        var counter: Int = 0
        val lock = Any()
        val threadList = List(10) {
            Thread {
                repeat(1000) {
                    synchronized(lock) {
                        counter += 1
                    }
                }
            }.apply { start() }
        }
        threadList.forEach { (it.join()) }
        println(counter)
        return counter
    }
}

object Deadlock {
    val lock1 = Any()
    val lock2 = Any()

    fun runDeadlock() {
        val thread1 = Thread {
            synchronized(lock1) {
                println("Locked resA")
                Thread.sleep(100)

                synchronized(lock2) {
                    println("Locked resB")
                }
            }
        }

        val thread2 = Thread {
            synchronized(lock2) {
                println("Locked resB")
                Thread.sleep(100)

                synchronized(lock1) {
                    println("Locked resA")
                }
            }
        }

        thread1.start()
        thread2.start()

        thread1.join()
        thread2.join()
    }

    fun runFixed(): Boolean {
        val thread1 = Thread {
            synchronized(lock1) {
                println("Locked resA")
                Thread.sleep(100)

                synchronized(lock2) {
                    println("Locked resB")
                }
            }
        }

        val thread2 = Thread {
            synchronized(lock1) {
                println("Locked resA")
                Thread.sleep(100)

                synchronized(lock2) {
                    println("Locked resB")
                }
            }
        }

        thread1.start()
        thread2.start()

        thread1.join()
        thread2.join()

        return true
    }
}

//5task

object ExecutorServiceExample {
    fun run(): List<String> {
        val executor = Executors.newFixedThreadPool(4)
        val results = mutableListOf<String>()

        val tasks = List(20) { index ->
            executor.submit<String> {
                val threadName = Thread.currentThread().name
                val message = "Task $index on $threadName"
                println(message)
                Thread.sleep(200)
                message
            }
        }

        tasks.forEach { future ->
            results.add(future.get())
        }

        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.MINUTES)

        return results
    }
}

//6task

object FutureFactorial {
    fun run(): Map<Int, BigInteger> {
        val executor = Executors.newFixedThreadPool(4)
        val futures = mutableMapOf<Int, Future<BigInteger>>()

        for (i in 1..10) {
            futures[i] = executor.submit<BigInteger> {
                var result = BigInteger.ONE
                for (j in 2..i) {
                    result = result.multiply(BigInteger.valueOf(j.toLong()))
                }
                result
            }
        }

        val results = mutableMapOf<Int, BigInteger>()
        for ((num, future) in futures) {
            results[num] = future.get()
        }

        executor.shutdown()

        return results
    }
}

//7task

object CoroutineLaunch {
    fun run(): List<String> = runBlocking {
        val results = mutableListOf<String>()
        val jobs = List(3) { index ->
            val name = "Coroutine-${'A' + index}"
            launch {
                repeat(5) {
                    delay(500)
                    results.add("$name: ${it + 1}")
                }
            }
        }
        jobs.joinAll()
        results
    }
}

//8task

object AsyncAwait {
    fun run(): Long = runBlocking {
        val total = 1_000_000L
        val parts = 4
        val partSize = total / parts

        val deferreds = List(parts) { partIndex ->
            async {
                val start = partIndex * partSize + 1
                val end = if (partIndex == parts - 1) total else (partIndex + 1) * partSize
                (start..end).sum()
            }
        }

        deferreds.sumOf { it.await() }
    }
}

//9task

object StructuredConcurrency {
    fun run(failingCoroutineIndex: Int): Int = runBlocking {
        var completedCount = 0
        try {
            coroutineScope {
                List(5) { index ->
                    launch {
                        delay(100)
                        if (index == failingCoroutineIndex) {
                            throw RuntimeException("Child $index failed")
                        }
                        completedCount++
                    }
                }.joinAll()
            }
        } catch (e: Exception) {
            println("Caught exception: ${e.message}")
        }
        completedCount
    }
}


//10task

object WithContextIO {
    fun run(filePaths: List<String>): Map<String, String> = runBlocking {
        val deferreds = filePaths.map { filePath ->
            async(Dispatchers.IO) {
                filePath to File(filePath).readText()
            }
        }

        deferreds.associate { it.await() }
    }
}


fun main() {
    CreateThreads.run()
    RaceCondition.run()
    SynchronizedCounter.run()
    //Deadlock.runDeadlock()
    Deadlock.runFixed()
    ExecutorServiceExample.run()
    FutureFactorial.run()
    CoroutineLaunch.run()
    AsyncAwait.run()
    StructuredConcurrency.run(3)
    //WithContextIO.run()
}
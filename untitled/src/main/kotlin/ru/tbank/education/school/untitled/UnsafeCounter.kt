package ru.tbank.education.school.untitled

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UnsafeCounter {

    private var value = 0
    private val mutex = Mutex()

    suspend fun increment() {
        delay(1)
        mutex.withLock {
            value++
        }
    }

    fun getValue(): Int = value

    suspend fun runConcurrentIncrements(
        coroutineCount: Int = 10,
        incrementsPerCoroutine: Int = 1000
    ): Int = coroutineScope {

        val jobs = List(coroutineCount) {
            launch(Dispatchers.Default) {
                repeat(incrementsPerCoroutine) {
                    increment()
                }
            }
        }
        jobs.joinAll()
        getValue()
    }
}
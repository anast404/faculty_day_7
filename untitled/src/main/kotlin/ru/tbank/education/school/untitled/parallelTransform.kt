package ru.tbank.education.school.untitled

import kotlinx.coroutines.*

suspend fun <T, R> parallelTransform(
    items: List<T>,
    transform: suspend (T) -> R
): List<R> = coroutineScope {
    val deferreds = items.map { item ->
        async {
            transform(item)
        }
    }
    deferreds.awaitAll()
}
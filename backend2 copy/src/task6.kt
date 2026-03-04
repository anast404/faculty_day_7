import java.net.HttpURLConnection
import java.net.URI

// Задача 6. Клиент для сервера заметок

const val BASE = "http://localhost:8080/api/notes"

fun request(url: String, method: String, body: String? = null): Pair<Int, String> {
    val connection = URI(url).toURL().openConnection() as HttpURLConnection
    connection.requestMethod = method
    connection.setRequestProperty("Accept", "application/json")

    if (body != null) {
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")
        connection.outputStream.write(body.toByteArray())
    }

    val code = connection.responseCode
    val responseBody = if (code in 200..299)
        connection.inputStream.bufferedReader().readText()
    else
        connection.errorStream?.bufferedReader()?.readText() ?: "No body"

    connection.disconnect()
    return Pair(code, responseBody)
}

fun main() {
    // Шаг 1: получить все заметки
    println("=== 1. GET /api/notes — все заметки ===")
    val (c1, b1) = request(BASE, "GET")
    println("Статус: $c1\n$b1")

    // Шаг 2: создать новую заметку
    println("\n=== 2. POST /api/notes — создать заметку ===")
    val newNote = """{"title":"Домашка","content":"Сделать задание по сетям","tag":"учёба"}"""
    val (c2, b2) = request(BASE, "POST", newNote)
    println("Статус: $c2\n$b2")

    // Шаг 3: получить заметку по id
    println("\n=== 3. GET /api/notes/1 — одна заметка ===")
    val (c3, b3) = request("$BASE/1", "GET")
    println("Статус: $c3\n$b3")

    // Шаг 4: обновить заметку
    println("\n=== 4. PUT /api/notes/1 — обновить заметку ===")
    val updated = """{"title":"Покупки (обновлено)","content":"Молоко, хлеб, яйца, сыр","tag":"личное"}"""
    val (c4, b4) = request("$BASE/1", "PUT", updated)
    println("Статус: $c4\n$b4")

    // Шаг 5: фильтр по тегу
    println("\n=== 5. GET /api/notes?tag=учёба — фильтр по тегу ===")
    val tag = java.net.URLEncoder.encode("учёба", "UTF-8")
    val (c5, b5) = request("$BASE?tag=$tag", "GET")
    println("Статус: $c5\n$b5")

    // Шаг 6: удалить заметку
    println("\n=== 6. DELETE /api/notes/1 — удалить заметку ===")
    val (c6, b6) = request("$BASE/1", "DELETE")
    println("Статус: $c6\n$b6")

    // Шаг 7: запросить несуществующую заметку (ожидаем 404)
    println("\n=== 7. GET /api/notes/999 — несуществующая заметка ===")
    val (c7, b7) = request("$BASE/999", "GET")
    println("Статус: $c7\n$b7")

    // Шаг 8: финальное состояние
    println("\n=== 8. GET /api/notes — финальное состояние ===")
    val (c8, b8) = request(BASE, "GET")
    println("Статус: $c8\n$b8")
}
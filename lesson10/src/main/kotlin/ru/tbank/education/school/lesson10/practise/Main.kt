package ru.tbank.education.school.lesson10.practise

import java.time.temporal.ChronoUnit
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

/*
1) Строки + регулярные выражения
["Name: Ivan, score=17", ...]
Извлечь имя и score, собрать пары, вывести победителя.
*/
fun task1() {
    val lines = listOf(
        "Name: Ivan, score=17",
        "Name: Olga, score=23",
        "Name: Max, score=5"
    )

    val re = Regex("""^Name:\s*([A-Za-z]+)\s*,\s*score=(\d+)\s*$""")

    val pairs: List<Pair<String, Int>> = lines.mapNotNull { s ->
        val m = re.find(s) ?: return@mapNotNull null
        val name = m.groupValues[1]
        val score = m.groupValues[2].toInt()
        name to score
    }

    println("Task 1 pairs: $pairs")

    val best = pairs.maxByOrNull { it.second }
    if (best != null) {
        println("Task 1 best: ${best.first} (${best.second})")
    } else {
        println("Task 1: no valid lines")
    }
}

/*
2) Даты + коллекции
["2026-01-22", ...]
Преобразовать в даты, отсортировать, посчитать сколько в январе 2026.
*/
fun task2() {
    val dateStrings = listOf(
        "2026-01-22",
        "2026-02-01",
        "2025-12-31",
        "2026-01-05"
    )

    val fmt = DateTimeFormatter.ISO_LOCAL_DATE

    val dates = dateStrings.map { LocalDate.parse(it, fmt) }.sorted()

    println("Task 2 sorted dates: ${dates.joinToString { it.format(fmt) }}")

    val countJan2026 = dates.count { it.year == 2026 && it.month == Month.JANUARY }
    println("Task 2 count in Jan 2026: $countJan2026")
}

/*
3) Коллекции + строки
"apple orange apple banana orange apple"
Частоты слов, вывести слова с частотой > 1 по алфавиту.
*/
fun task3() {
    val text = "apple orange apple banana orange apple"

    val words = text.trim().split(Regex("""\s+""")).filter { it.isNotEmpty() }

    val freq = mutableMapOf<String, Int>()
    for (w in words) {
        freq[w] = (freq[w] ?: 0) + 1
    }

    println("Task 3 freq: $freq")

    val repeated = freq
        .filter { (_, c) -> c > 1 }
        .keys
        .sorted()

    println("Task 3 repeated words: ${repeated.joinToString(", ")}")
}


fun task4() {
    val strings = listOf("A-123", "B-7", "AA-12", "C-001", "D-99x")

    val pattern = Regex("""^[A-Z]-\d{1,3}$""")
    val filtered = strings.filter { pattern.matches(it) }

    println("Task 4. Filtered list: $filtered")
}


fun task5() {
    val strings = listOf("  Hello   world  ", "A   B    C", "   one")

    val normalized = strings.map {
        it.trim().replace(Regex("\\s+"), " ")
    }

    println("Task 5. Normalized strings:")
    normalized.forEachIndexed { index, str ->
        println("[$index]: \"$str\"")
    }
}


fun task6() {
    val datePairs = listOf(
        Pair("2026-01-01", "2026-01-10"),
        Pair("2025-12-31", "2026-01-01"),
        Pair("2026-02-01", "2026-01-22")
    )

    val differences = datePairs.map { (firstStr, secondStr) ->
        val firstDate = LocalDate.parse(firstStr)
        val secondDate = LocalDate.parse(secondStr)
        ChronoUnit.DAYS.between(firstDate, secondDate)
    }

    println("Task 6. Date differences (in days): $differences")
}

fun task7() {
    val strings = listOf("math:Ivan", "bio:Olga", "math:Max", "bio:Ivan", "cs:Olga")

    val subjectMap = mutableMapOf<String, MutableList<String>>()

    strings.forEach { entry ->
        val (subject, student) = entry.split(":")

        if (!subjectMap.containsKey(subject)) {
            subjectMap[subject] = mutableListOf()
        }
        subjectMap[subject]?.add(student)
    }

    println("Task 7. Students map: $subjectMap")
}

/*
Домашнее задание
*/

data class LogEntry(
    val dt: String,
    val id: Int,
    val status: String
)

data class ProcessedId(
    val id: Int,
    val sentTime: LocalDateTime? = null,
    val deliveredTime: LocalDateTime? = null,
    val duration: Long? = null,
    val hasError: Boolean = false,
    val errorType: String? = null
)

fun normalize(line: String): LogEntry? {
    val trimmedLine = line.trim()

    val patternA = """(\d{4}-\d{2}-\d{2}\s+\d{2}:\d{2})\s*\|\s*[Ii][Dd]:(\d+)\s*\|\s*[Ss][Tt][Aa][Tt][Uu][Ss]:(\w+)""".toRegex()

    val patternB = """[Tt][Ss]\s*=\s*(\d{2}/\d{2}/\d{4})-(\d{2}:\d{2})\s*;\s*[Ss][Tt][Aa][Tt][Uu][Ss]\s*=\s*(\w+)\s*;\s*#(\d+)""".toRegex()

    val patternC = """\[(\d{2}\.\d{2}\.\d{4})\s+(\d{2}:\d{2})\]\s+(\w+)\s*\(\s*[Ii][Dd]:(\d+)\s*\)""".toRegex()

    patternA.find(trimmedLine)?.let { match ->
        val (datetime, idStr, statusStr) = match.destructured
        val status = statusStr.lowercase()
        if (status == "sent" || status == "delivered") {
            return LogEntry(datetime, idStr.toInt(), status)
        }
    }

    patternB.find(trimmedLine)?.let { match ->
        val (date, time, statusStr, idStr) = match.destructured
        val status = statusStr.lowercase()
        if (status == "sent" || status == "delivered") {
            val day = date.substring(0, 2)
            val month = date.substring(3, 5)
            val year = date.substring(6, 10)
            val datetime = "$year-$month-$day $time"
            return LogEntry(datetime, idStr.toInt(), status)
        }
    }

    patternC.find(trimmedLine)?.let { match ->
        val (date, time, statusStr, idStr) = match.destructured
        val status = statusStr.lowercase()
        if (status == "sent" || status == "delivered") {
            val day = date.substring(0, 2)
            val month = date.substring(3, 5)
            val year = date.substring(6, 10)
            val datetime = "$year-$month-$day $time"
            return LogEntry(datetime, idStr.toInt(), status)
        }
    }

    return null
}


fun main() {
    task1()
    println()
    task2()
    println()
    task3()
    println()
    task4()
    println()
    task5()
    println()
    task6()
    println()
    task7()
    println()

    val logs = listOf(
        "2026-01-22 09:14 | ID:042 | STATUS:sent",
        "TS=22/01/2026-09:27; status=delivered; #042",
        "2026-01-22 09:10 | ID:043 | STATUS:sent",
        "2026-01-22 09:18 | ID:043 | STATUS:delivered",
        "TS=22/01/2026-09:05; status=sent; #044",
        "[22.01.2026 09:40] delivered (id:044)",
        "2026-01-22 09:20 | ID:045 | STATUS:sent",
        "[22.01.2026 09:33] delivered (id:045)",
        "   ts=22/01/2026-09:50; STATUS=Sent; #046   ",
        " [22.01.2026 10:05]   DELIVERED   (ID:046) "
    )

    val normalizedEntries = mutableListOf<LogEntry>()
    val brokenLines = mutableListOf<String>()

    println("Нормализация логов")
    logs.forEach { line ->
        val normalized = normalize(line)
        if (normalized != null) {
            normalizedEntries.add(normalized)
            println("Нормализовано: $normalized")
        } else {
            brokenLines.add(line)
            println("Битая строка: $line")
        }
    }

    println("\nРасчёт времени доставки")
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val processedIds = mutableMapOf<Int, ProcessedId>()
    val incompleteIds = mutableListOf<Int>()
    val timeErrorIds = mutableListOf<Int>()
    val duplicateEvents = mutableMapOf<Int, MutableMap<String, Int>>()

    val groupedById = normalizedEntries.groupBy { it.id }

    groupedById.forEach { (id, entries) ->
        val statusCount = entries.groupingBy { it.status }.eachCount()
        if (statusCount.any { it.value > 1 }) {
            duplicateEvents[id] = statusCount.toMutableMap()
        }

        val sentEntries = entries.filter { it.status == "sent" }
        val deliveredEntries = entries.filter { it.status == "delivered" }

        if (sentEntries.isEmpty() || deliveredEntries.isEmpty()) {
            incompleteIds.add(id)
            processedIds[id] = ProcessedId(id, hasError = true, errorType = "неполный")
        } else {
            val sentTime = LocalDateTime.parse(sentEntries.last().dt, dateFormatter)
            val deliveredTime = LocalDateTime.parse(deliveredEntries.last().dt, dateFormatter)

            if (deliveredTime.isBefore(sentTime)) {
                timeErrorIds.add(id)
                processedIds[id] = ProcessedId(id, sentTime, deliveredTime, hasError = true, errorType = "ошибка времени")
            } else {
                val duration = ChronoUnit.MINUTES.between(sentTime, deliveredTime)
                processedIds[id] = ProcessedId(id, sentTime, deliveredTime, duration)
                println("ID $id: отправлено в ${sentTime.format(dateFormatter)}, доставлено в ${deliveredTime.format(dateFormatter)}, время: $duration мин.")
            }
        }
    }
    println("\nОтчёт")

    val validIds = processedIds.values
        .filter { !it.hasError && it.duration != null }
        .sortedByDescending { it.duration }

    println("\nСписок всех ID с длительностью доставки (по убыванию):")
    validIds.forEach {
        println("ID ${it.id}: ${it.duration} минут")
    }

    val longestOrder = validIds.firstOrNull()
    if (longestOrder != null) {
        println("\nСамый долгий заказ: ID ${longestOrder.id}, время: ${longestOrder.duration} минут")
    }

    val violators = validIds.filter { it.duration!! > 20 }
    if (violators.isNotEmpty()) {
        println("\nНарушители правила (доставка дольше 20 минут):")
        violators.forEach {
            println("ID ${it.id}: ${it.duration} минут")
        }
    } else {
        println("\nНарушителей правила (доставка дольше 20 минут) нет.")
    }

    if (incompleteIds.isNotEmpty()) {
        println("\nНеполные ID (отсутствует sent или delivered): $incompleteIds")
    }

    if (timeErrorIds.isNotEmpty()) {
        println("\nID с ошибкой времени (delivered раньше sent): $timeErrorIds")
    }

    val hourStats = mutableMapOf<Int, Int>()
    normalizedEntries
        .filter { it.status == "delivered" }
        .forEach { entry ->
            val hour = entry.dt.substring(11, 13).toInt()
            hourStats[hour] = hourStats.getOrDefault(hour, 0) + 1
        }

    println("\nСводка доставок по часам (delivered):")
    hourStats.entries.sortedBy { it.key }.forEach { (hour, count) ->
        println("Час $hour: $count доставок")
    }

    val maxHour = hourStats.maxByOrNull { it.value }
    if (maxHour != null) {
        println("Больше всего доставок в час ${maxHour.key}: ${maxHour.value} событий")
    }

    if (duplicateEvents.isNotEmpty()) {
        println("\nДубли:")
        duplicateEvents.forEach { (id, statusCount) ->
            println("ID $id имеет дубли:")
            statusCount.forEach { (status, count) ->
                if (count > 1) {
                    println(" $status: $count события")
                }
            }
        }
    }
}
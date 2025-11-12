package ru.tbank.education.school.lesson1

fun main() {
    println("5 + 2".calculate())
    println("10 * 3".calculate())
    println("8 / 2".calculate())
    println("5 - 3".calculate())
    println("5 & 2".calculate())
    println("5 2".calculate())
    println("5 / 0".calculate())
}

fun calculate(a: Double, b: Double, operation: OperationType = OperationType.ADD): Double? {
    return when (operation) {
        OperationType.ADD -> a + b
        OperationType.SUBTRACT -> a - b
        OperationType.MULTIPLY -> a * b
        OperationType.DIVIDE -> if (b != 0.0) a / b else null
    }
}

@Suppress("ReturnCount")
fun String.calculate(): Double? {
    val parts = this.split(" ")

    if (parts.size == 2) {
        val a = parts[0].toDoubleOrNull() ?: return null
        val b = parts[1].toDoubleOrNull() ?: return null
        return calculate(a, b)
    }

    if (parts.size != 3) return null

    val a = parts[0].toDoubleOrNull() ?: return null
    val b = parts[2].toDoubleOrNull() ?: return null
    val operation = when (parts[1]) {
        "+" -> OperationType.ADD
        "-" -> OperationType.SUBTRACT
        "*" -> OperationType.MULTIPLY
        "/" -> OperationType.DIVIDE
        else -> return null
    }

    return calculate(a, b, operation)
}

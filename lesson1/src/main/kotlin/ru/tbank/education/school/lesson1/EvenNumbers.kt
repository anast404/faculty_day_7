package ru.tbank.education.school.lesson1

fun sumEvenNumbers(numbers: Array<Int>): Int {
    var sum = 0
    for (number in numbers) {
        if (number % 2 == 0) {
            sum += number
        }
    }
    return sum
}

fun main() {
    val test1 = arrayOf(1, 2, 3, 4, 5, 6)
    println(sumEvenNumbers(test1))

    val test2 = arrayOf<Int>()
    println(sumEvenNumbers(test2))

    val test3 = arrayOf(1, 3, 5, 7, 9)
    println(sumEvenNumbers(test3))
}
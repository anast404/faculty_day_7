package com.example.demo

import com.example.demo.service.BookService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@Component
class DemoRunner(private val bookService: BookService) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val b1 = bookService.create("Мастер и Маргарита", "Булгаков", "Fiction")
        val b2 = bookService.create("Краткая история времени", "Хокинг", "Science")
        val b3 = bookService.create("История Рима", "Тит Ливий", "History")
        println("После создания: ${bookService.getAll()}")

        println("Найдена по id: ${bookService.getById(b1.id)}")

        bookService.update(b2.id, "Краткая история времени (изд. 2024)", "Хокинг", "Science")
        println("После обновления: ${bookService.getAll()}")

        bookService.delete(b3.id)
        println("После удаления: ${bookService.getAll()}")
    }
}
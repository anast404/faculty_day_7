package com.example.demo

import com.example.demo.service.BookService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
@EnableConfigurationProperties
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@Component
class DemoRunner(private val bookService: BookService) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(DemoRunner::class.java)

    override fun run(vararg args: String?) {
        log.info("Демонстрация CRUD")

        val b1 = bookService.create("Мастер и Маргарита", "Булгаков", "Fiction")
        val b2 = bookService.create("Краткая история времени", "Хокинг", "Science")
        val b3 = bookService.create("История Рима", "Тит Ливий", "History")

        log.info("Все книги: ${bookService.getAll()}")

        bookService.update(b1.id, "Мастер и Маргарита (изд. 2024)", "Булгаков", "Fiction")
        log.info("После обновления: ${bookService.getById(b1.id)}")

        bookService.delete(b3.id)
        log.info("После удаления: ${bookService.getAll()}")

        try {
            bookService.create("Запрещённая книга", "Автор", "Fiction")
        } catch (e: IllegalStateException) {
            log.warn("Ожидаемая ошибка: ${e.message}")
        }

        try {
            bookService.create("Хорошая книга", "Автор", "Horror")
        } catch (e: IllegalStateException) {
            log.warn("Ожидаемая ошибка: ${e.message}")
        }

        log.info("Демонстрация завершена")
    }
}

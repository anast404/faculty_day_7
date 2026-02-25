package com.example.demo.service

import com.example.demo.config.BookServiceConfig
import com.example.demo.model.Book
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BookService(private val config: BookServiceConfig) {

    private val log = LoggerFactory.getLogger(BookService::class.java)
    private val books: MutableMap<Long, Book> = mutableMapOf()
    private var idCounter: Long = 1

    fun create(title: String, author: String, genre: String): Book {
        if (books.size >= config.maxBooks) {
            log.warn("Достигнут лимит книг: ${config.maxBooks}")
            error("Достигнут лимит: нельзя добавить более ${config.maxBooks} книг")
        }
        if (config.forbiddenTitles.any { it.equals(title, ignoreCase = true) }) {
            log.warn("Попытка добавить запрещённую книгу: '$title'")
            error("Название '$title' находится в списке запрещённых")
        }
        if (config.allowedGenres.isNotEmpty() && !config.allowedGenres.contains(genre)) {
            log.warn("Жанр '$genre' не разрешён")
            error("Жанр '$genre' не входит в список разрешённых: ${config.allowedGenres}")
        }
        val book = Book(id = idCounter++, title = title, author = author, genre = genre)
        books[book.id] = book
        log.info("Добавлена книга: $book")
        return book
    }

    fun getAll(): List<Book> {
        log.info("Запрос всех книг (всего: ${books.size})")
        return books.values.toList()
    }

    fun getById(id: Long): Book {
        return books[id] ?: error("Книга с id=$id не найдена")
    }

    fun update(id: Long, title: String, author: String, genre: String): Book {
        books[id] ?: error("Книга с id=$id не найдена")
        if (config.forbiddenTitles.any { it.equals(title, ignoreCase = true) }) {
            error("Название '$title' находится в списке запрещённых")
        }
        if (config.allowedGenres.isNotEmpty() && !config.allowedGenres.contains(genre)) {
            error("Жанр '$genre' не входит в список разрешённых: ${config.allowedGenres}")
        }
        val updated = Book(id = id, title = title, author = author, genre = genre)
        books[id] = updated
        log.info("Обновлена книга: $updated")
        return updated
    }

    fun delete(id: Long) {
        books.remove(id) ?: error("Книга с id=$id не найдена")
        log.info("Удалена книга с id=$id")
    }
}

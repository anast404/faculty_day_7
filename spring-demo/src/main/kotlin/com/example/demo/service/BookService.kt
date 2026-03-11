package com.example.demo.service

import com.example.demo.model.Book
import org.springframework.stereotype.Service

@Service
class BookService {

    private val books: MutableList<Book> = mutableListOf()
    private var idCounter: Long = 1

    fun create(title: String, author: String, genre: String): Book {
        val book = Book(id = idCounter++, title = title, author = author, genre = genre)
        books.add(book)
        return book
    }

    fun getAll(): List<Book> = books.toList()

    fun getById(id: Long): Book =
        books.find { it.id == id } ?: error("Книга с id=$id не найдена")

    fun update(id: Long, title: String, author: String, genre: String): Book {
        val index = books.indexOfFirst { it.id == id }
        if (index == -1) error("Книга с id=$id не найдена")
        val updated = Book(id = id, title = title, author = author, genre = genre)
        books[index] = updated
        return updated
    }

    fun delete(id: Long) {
        val removed = books.removeIf { it.id == id }
        if (!removed) error("Книга с id=$id не найдена")
    }
}
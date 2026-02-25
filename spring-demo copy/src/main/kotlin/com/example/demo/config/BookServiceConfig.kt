package com.example.demo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "book-service")
class BookServiceConfig {
    var maxBooks: Int = 10
    var forbiddenTitles: List<String> = emptyList()
    var allowedGenres: List<String> = emptyList()
}

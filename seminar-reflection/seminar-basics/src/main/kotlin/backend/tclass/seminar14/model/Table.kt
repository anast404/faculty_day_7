package backend.tclass.seminar14.model

import backend.tclass.seminar14.annotations.Validation

data class Table(
    val name: String,

    @Validation(min = 0, max = 10000)
    val size: Int,
)

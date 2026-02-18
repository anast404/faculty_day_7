package backend.tclass.seminar14.model

import backend.tclass.seminar14.annotations.MyRuntimeAnnotation
import backend.tclass.seminar14.annotations.Validation

/**
 * Data class Person с пользовательской аннотацией.
 * В Kotlin data class аналогичен record в Java - автоматически генерирует
 * equals, hashCode, toString и copy методы.
 */
@MyRuntimeAnnotation(metainfo = "Person metainformation")
data class Person(

    val name: String,

    val surname: String,

    @Validation(min = 0, max = 100)
    val age: Int

) {

    /**
     * Приватный метод — чтобы показать вызов закрытых методов.
     */
    private fun sayHelloTo(target: String): String {
        return "Hello, $target! I'm $name $surname."
    }

}


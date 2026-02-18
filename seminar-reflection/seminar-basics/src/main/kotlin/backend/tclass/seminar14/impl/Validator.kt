package backend.tclass.seminar14.impl

import backend.tclass.seminar14.annotations.Validation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object Validator {

    fun validate(obj: Any) {
        val kClass = obj::class

        for (property in kClass.memberProperties) {
            property.isAccessible = true

            val annotation = property.annotations
                .filterIsInstance<Validation>()
                .firstOrNull() ?: continue

            val value = property.getter.call(obj)

            if (value is Int) {
                if (value < annotation.min || value > annotation.max) {
                    throw IllegalArgumentException(
                        "Поле '${property.name}' должно быть в диапазоне " +
                            "${annotation.min}..${annotation.max}, текущее значение: $value"
                    )
                }
            }
        }
    }
}

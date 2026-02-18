package backend.tclass.seminar14.annotations

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Validation(
    val min: Int = Int.MIN_VALUE,
    val max: Int = Int.MAX_VALUE
)

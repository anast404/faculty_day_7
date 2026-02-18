package backend.tclass.reflection.services

import backend.tclass.reflection.annotations.Component
import backend.tclass.reflection.model.Message
import backend.tclass.reflection.model.Person

/**
 * Реализация сервиса отправки сообщений.
 *
 * Помечен аннотацией @Component, поэтому будет автоматически
 * зарегистрирован в DI-контейнере при сканировании пакетов.
 */
@Component
class MessageServiceImpl : MessageService {
    /**
     * Отправляет сообщение пользователю.
     * В реальном приложении здесь была бы бизнес-логика отправки
     * (например, через email, push-уведомления и т.д.)
     */
    override fun sendMessage(person: Person, message: Message) {
        // BUSINESS LOGIC
        println("Message was successfully sent")
    }
}

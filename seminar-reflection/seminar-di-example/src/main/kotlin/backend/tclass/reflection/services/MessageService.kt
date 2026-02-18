package backend.tclass.reflection.services

import backend.tclass.reflection.model.Message
import backend.tclass.reflection.model.Person

/**
 * Интерфейс сервиса для отправки сообщений.
 * Определяет контракт для бизнес-логики отправки сообщений пользователям.
 */
interface MessageService {
    /**
     * Отправляет сообщение указанному пользователю.
     *
     * @param person получатель сообщения
     * @param message содержимое сообщения
     */
    fun sendMessage(person: Person, message: Message)
}

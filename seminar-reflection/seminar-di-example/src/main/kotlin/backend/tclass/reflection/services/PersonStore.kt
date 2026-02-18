package backend.tclass.reflection.services

import backend.tclass.reflection.model.Person

/**
 * Интерфейс хранилища пользователей.
 * Определяет контракт для работы с хранилищем пользователей.
 */
interface PersonStore {
    /**
     * Получает пользователя по имени.
     *
     * @param name имя пользователя
     * @return Optional с пользователем, если найден, иначе пустой Optional
     */
    fun getPerson(name: String): Person?

    /**
     * Сохраняет пользователя в хранилище.
     *
     * @param p пользователь для сохранения
     */
    fun save(p: Person)

    /**
     * Возвращает список всех пользователей.
     *
     * @return список всех сохранённых пользователей
     */
    fun getPersons(): List<Person>


    /**
     * Удаляет пользователя из хранилища.
     */
    fun remove(p: Person): Boolean
}

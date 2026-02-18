package backend.tclass.seminar14

import backend.tclass.seminar14.impl.CardService
import backend.tclass.seminar14.impl.PersonService
import backend.tclass.seminar14.impl.Validator
import backend.tclass.seminar14.model.Person
import backend.tclass.seminar14.model.Table

fun main() {
    val person = PersonService()

    person.addPerson(Person("John ", "Doe", 30))

    val pesron2 = PersonService()

    pesron2.addPerson(Person("John ", "Doe", 20))


    val cardService = CardService()
    cardService.addCard("1234567890", person.getPerson("John ")!!)
}

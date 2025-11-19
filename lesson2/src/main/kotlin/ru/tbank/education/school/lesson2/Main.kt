package ru.tbank.education.school.lesson2

abstract class HotelRoom(
    val number: Int,
    val capacity: Int,
    var isOccupied: Boolean = false
) {
    abstract val type: String
    abstract val pricePerNight: Double

    val status: String
        get() = if (isOccupied) "Занят" else "Свободен"

    fun checkIn() {
        if (!isOccupied) {
            isOccupied = true
            println("Номер $number заселен")
        } else {
            println("Номер $number уже занят")
        }
    }

    fun checkOut() {
        if (isOccupied) {
            isOccupied = false
            println("Номер $number освобожден")
        } else {
            println("Номер $number уже свободен")
        }
    }

    abstract fun getDescription(): String
}

class StandardRoom(
    number: Int,
    hasBalcony: Boolean = false
) : HotelRoom(number, 2) {
    override val type = "Стандартный"
    override val pricePerNight = 3000.0
    private val balcony = hasBalcony

    constructor(number: Int) : this(number, false)

    override fun getDescription(): String {
        return "Стандартный номер №$number (вместимость: $capacity чел.)" +
                if (balcony) " с балконом" else " без балкона"
    }
}

class LuxuryRoom(
    number: Int,
    val hasJacuzzi: Boolean = true
) : HotelRoom(number, 4) {
    override val type = "Люкс"
    override val pricePerNight = if (hasJacuzzi) 8000.0 else 7500.0

    override fun getDescription(): String {
        if (hasJacuzzi) {
            return "Люкс номер №$number (вместимость: $capacity чел.) с джакузи"
        } else {
            return "Люкс номер №$number (вместимость: $capacity чел.)"
        }
    }

    fun orderChampagne(): String {
        return "Шампанское доставлено в номер $number"
    }
}

data class Guest(
    val name: String,
    val passportNumber: String,
    val phone: String
) {
    val displayInfo: String
        get() = "$name (паспорт: $passportNumber)"
}

sealed class BookingStatus {
    object Confirmed : BookingStatus()
    object Cancelled : BookingStatus()
    object Completed : BookingStatus()
}

class Booking(
    val guest: Guest,
    val room: HotelRoom,
    val nights: Int
) {
    private var status: BookingStatus = BookingStatus.Confirmed

    val totalPrice: Double
        get() = room.pricePerNight * nights

    fun cancelBooking() {
        status = BookingStatus.Cancelled
        room.checkOut()
        println("Бронь отменена для ${guest.name}")
    }

    fun completeBooking() {
        status = BookingStatus.Completed
        room.checkOut()
        println("Бронь завершена для ${guest.name}. Итоговая стоимость: $totalPrice руб.")
    }

    fun getStatusInfo(): String {
        return when (status) {
            is BookingStatus.Confirmed -> "Подтверждена"
            is BookingStatus.Cancelled -> "Отменена"
            is BookingStatus.Completed -> "Завершена"
        }
    }
}

class Hotel(val name: String) {
    private val rooms = mutableListOf<HotelRoom>()
    val bookings = mutableListOf<Booking>()

    fun addRoom(room: HotelRoom) {
        rooms.add(room)
        println("Добавлен номер: ${room.getDescription()}")
    }

    fun showAvailableRooms() {
        println("\nСвободные номера в отеле '$name': ")
        rooms.filter { !it.isOccupied }.forEach { room ->
            println("${room.getDescription()} - ${room.pricePerNight} руб./ночь")
        }
    }

    fun bookRoom(guest: Guest, roomNumber: Int, nights: Int): Booking? {
        val room = rooms.find { it.number == roomNumber && !it.isOccupied }

        return if (room != null) {
            room.checkIn()
            val booking = Booking(guest, room, nights)
            bookings.add(booking)
            println("Бронь создана для ${guest.name} в номер $roomNumber на $nights ночей")
            booking
        } else {
            println("Номер $roomNumber недоступен для бронирования")
            null
        }
    }

    fun showAllBookings() {
        println("\nВсе бронирования: ")
        bookings.forEach { booking ->
            println("Гость: ${booking.guest.displayInfo}, " +
                    "Номер: ${booking.room.number}, " +
                    "Ночей: ${booking.nights}, " +
                    "Статус: ${booking.getStatusInfo()}")
        }
    }
}

fun main() {
    val hotel = Hotel("Престиж")

    hotel.addRoom(StandardRoom(101))
    hotel.addRoom(StandardRoom(102, true))
    hotel.addRoom(LuxuryRoom(201, hasJacuzzi = false))
    hotel.addRoom(LuxuryRoom(202))

    val guest1 = Guest("Иван П.", "4010123456", "+79161234567")
    val guest2 = Guest("Мария С.", "4010654321", "+79167654321")

    hotel.showAvailableRooms()

    println("\nНачинаем процесс бронирования: ")
    val booking1 = hotel.bookRoom(guest1, 101, 3)
    val booking2 = hotel.bookRoom(guest2, 201, 5)

    val booking3 = hotel.bookRoom(guest1, 101, 2)


    hotel.showAvailableRooms()

    println("\nДополнительные услуги: ")
    val luxuryRoom = hotel.bookings[1].room as? LuxuryRoom
    luxuryRoom?.let {
        println(it.orderChampagne())
    }

    hotel.showAllBookings()

    println("\nЗавершение бронирования: ")
    booking1?.completeBooking()

    booking2?.cancelBooking()

    hotel.showAllBookings()
    hotel.showAvailableRooms()
}
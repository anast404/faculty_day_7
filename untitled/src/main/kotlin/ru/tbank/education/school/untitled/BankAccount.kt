package ru.tbank.education.school.untitled

class BankAccount(val id: String, var balance: Int) {

    fun transfer(to: BankAccount, amount: Int) {
        val first = if (this.id < to.id) this else to
        val second = if (this.id < to.id) to else this

        synchronized(first) {
            synchronized(second) {
                if (balance >= amount) {
                    balance -= amount
                    to.balance += amount
                }
            }
        }
    }
}
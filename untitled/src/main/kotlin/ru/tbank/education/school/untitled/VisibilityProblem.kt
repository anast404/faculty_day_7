package ru.tbank.education.school.untitled

class VisibilityProblem {

    @Volatile
    private var running = true

    fun startWriter(): Thread {
        return Thread {
            repeat(100) {
                Thread.sleep(10)
                Thread.yield()
            }

            running = false
            println("Writer: установил running = false")
        }
    }

    fun startReader(): Thread {
        return Thread {
            println("Reader: начал работу (ждет running = false)")

            while (running) {
                Thread.sleep(1)
            }

            println("Reader: завершил работу (увидел running = false)")
        }
    }
}
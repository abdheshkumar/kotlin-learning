package basic

object WhenApp {

    private val `10` = when (10) {
        10 -> println("It's ten!")
        else -> println("Something else")
    }

    private fun numberName(number: Int) = when (number) {
        2 -> "two"
        4 -> "four"
        6 -> "six"
        8 -> "eight"
        10 -> "ten"
        else -> {
            println("Unknown number")
            "Unknown"
        }
    }

    private fun timeOfDay(hourOfDay: Int) = when (hourOfDay) {
        0, 1, 2, 3, 4, 5 -> "Early morning"
        6, 7, 8, 9, 10, 11 -> "Morning"
        12, 13, 14, 15, 16 -> "Afternoon"
        17, 18, 19 -> "Evening"
        20, 21, 22, 23 -> "Late evening"
        else -> "INVALID HOUR!"
    }

    private fun timeOfDayV1(hourOfDay: Int) = when (hourOfDay) {
        in 0..5 -> "Early morning"
        in 6..11 -> "Morning"
        in 12..16 -> "Afternoon"
        in 17..19 -> "Evening"
        in 20..23 -> "Late evening"
        else -> "INVALID HOUR!"
    }

    private fun isEven(number: Int) = when {
        number % 2 == 0 -> println("Even")
        else -> println("Odd")
    }

    private fun xyz(x: Int, y: Int, z: Int) = when {
        x == 0 && y == 0 && z == 0 -> println("Origin")
        y == 0 && z == 0 -> println("On the x-axis at x = $x")
        x == 0 && z == 0 -> println("On the y-axis at y = $y")
        x == 0 && y == 0 -> println("On the z-axis at z = $z")
        else -> println("Somewhere in space at x = $x, y = $y, z =$z")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        (`10`)
        println(numberName(10))
        val hourOfDay = 12
        println(timeOfDay(hourOfDay))
        println(timeOfDayV1(hourOfDay))
        isEven(2)
        xyz(0, 0, 0)
    }
}

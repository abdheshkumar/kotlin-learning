package basic

import arrow.core.Either
import arrow.core.Option

object LetApp {

    private fun mapString(s: String): Int = 1
    data class NotValidInt(val message: String?)

    private fun mapInt(s: String): Either<NotValidInt, Int> =
        Either.catch { s.toInt() }.mapLeft { ex -> NotValidInt(ex.message) }

    @JvmStatic
    fun main(args: Array<String>) {
        Option
        val st: String? = "null"
        val number: Int? = st?.let { mapString(it) }
        println(number)
        val s1: String? = null
        val s2: String? = null
        val s: Either<NotValidInt, Int>? = s2?.let { mapInt(it) }

        val ss: Pair<String, String>? = s1?.let { v ->
            s2?.let { Pair(v, it) }
        }

        // Log message
        // Filter something
        // map to something
        fun getSt(): String? = null
        val an: String? = null
        an ?: run { println("null...") }
        val re: String? = getSt()?.takeIf { it.length >= 2 }
    }
}

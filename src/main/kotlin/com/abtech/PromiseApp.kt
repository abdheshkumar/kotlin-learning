package com.abtech

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.handleError
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.combine
import java.util.*

object PromiseApp {
    fun f1(completableDeferred: CompletableDeferred<Unit>) {
        Thread.sleep(1000)
        completableDeferred.complete(Unit)
    }

    fun square(x: Int): Int = x * x // 1
    fun double(x: Int): Int = 2 * x // 2

    val result = double(square(5)) // 3
    fun squareAndDouble(x: Int): Int = double(square(x)) // 4
    infix fun <A, B, C> ((A) -> B).andThen(g: (B) -> C): (A) -> C = { a -> g(this(a)) } // 5
    val squareAndDoubleV: (Int) -> Int = ::square andThen ::double // 4
    val resultV = squareAndDoubleV(5) // 50

    fun multipleOf(x: Int): Int = x * 5
    val multipleAndSquareAndDouble: (Int) -> Int = ::multipleOf andThen squareAndDoubleV
    val result1 = multipleAndSquareAndDouble(3) // 450

    var count = 0
    fun impure(value: Int): Int {
        count++
        return value + count
    }

    /*  fun addAndLog(x: Int): Int {
          val result = x + 1
          println("New Value is $result")
          return result
      }

      fun addAndLog(x: Int): Pair<Int, String> {
          val result = x + 1
          return result to "New Value is $result"
      }*/
    fun assertOrThrow(message: String, fn: () -> Boolean) {
        if (!fn()) {
            throw AssertionError(message)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        /*println("Start")
        val c = CompletableDeferred<Unit>()
        f1(c)
        println("Finished")*/
        config().handleError {
            when (it) {
                is InvalidPort -> TODO()
                PortNotAvailable -> TODO()
                is SystemError -> TODO()
            }
        }
    }

    class Student(val name: String, val surname: String, val passing: Boolean, val averageGrade: Double)

    fun getStudent(students: List<Student>): List<Student> {
        val passingStudents = mutableListOf<Student>()
        // Filtering student
        for (student in students) {
            if (student.passing && student.averageGrade > 4.0) {
                passingStudents.add(student)
            }
        }
        // Sorting
        Collections.sort(
            passingStudents
        ) { o1, o2 ->
            if (o1.averageGrade == o2.averageGrade) 0
            else if (o1.averageGrade > o2.averageGrade) 1
            else -1
        }
        // Take 10 students
        val result = mutableListOf<Student>()
        for (i in 1..10) {
            result.add(passingStudents[i])
        }
        return result
    }

    fun getStudentV(students: List<Student>): List<Student> {
        higherOrder(::combine)
        higherOrder({ a, b -> a + b })
        higherOrder { a, b -> a + b }
        return students.filter { it.passing && it.averageGrade > 4.0 }
            .sortedBy { it.averageGrade }
            .take(10)
    }

    fun combine(a: Int, b: Int): Int = a + b
    fun higherOrder(f: (Int, Int) -> Int) {
    }

    fun higherOrderV(a: Int): (Int) -> Int = { b -> a * b }
    fun callTjs(): Result<String> = kotlin.runCatching {
        // It can fail because of network connectivity
        // Response parsing
        // Authentication
        //
        "response"
    }

    fun envOrNull(name: String): String? = kotlin.runCatching { System.getenv(name) }.getOrNull()
    fun config1(): Config? = envOrNull("port")?.toIntOrNull()?.let(::Config)

    fun callUise(): Result<String> = kotlin.runCatching {
        throw UiseError("Failed to get response from uise")
    }

    /*fun buildPersonalizationContext(): Result<PersonalizationContext> = kotlin.runCatching {
        callTjs().fold({ tjc ->
            callUise().fold({ uise -> PersonalizationContext(tjc, uise) }) {
                when (it) { //  all Throwables
                    is UiseError -> {
                        println(it.message)
                        return "default-uise-reponse"
                    }

                    is RuntimeException -> {
                        println(it.message)
                        return "default-uise-reponse"
                    }

                    else -> {
                        return ""
                    }
                }
            }
        }) {
            throw it // It hides type error
        }
    }*/

    fun failingFn(i: Int): Int {
        val y: Int = throw Exception("boom")
        return try {
            val x = 42 + 5
            x + y
        } catch (e: Exception) {
            43
        }
    }

    fun imperativeSum(list: List<String>): Int {
        var sum = 0
        for (item in list) {
            try {
                sum += item.toInt()
            } catch (ex: NumberFormatException) {
                // Skip
            }
        }
        return sum
    }

    fun failingFn2(i: Int): Int =
        try {
            val x = 42 + 5
            x + (throw Exception("boom!")) as Int
        } catch (e: Exception) {
            43
        }

    fun config(): Either<ConfigError, Config> =
        Either.catch { System.getenv("port") }
            .mapLeft(::SystemError)
            .flatMap {
                it?.let { Either.Right(it) } ?: Either.Left(PortNotAvailable)
            }
            .flatMap { Either.catch { it.toInt() }.mapLeft { InvalidPort(it) } }
            .map { Config(it) }
}

sealed interface ConfigError
data class SystemError(val underlying: Throwable) : ConfigError
object PortNotAvailable : ConfigError
data class InvalidPort(val underlying: Throwable) : ConfigError
data class Config(val port: Int)

data class PersonalizationContext(val tjcResponse: String, val uiseResponse: String)

sealed class PersonalisationError(message: String) : Exception(message)
data class UiseError(override val message: String) : PersonalisationError(message)
data class TjcError(override val message: String) : PersonalisationError(message)

class Student(
    val name: String,
    val surname: String,
    val passing: Boolean,
    val averageGrade: Double
)

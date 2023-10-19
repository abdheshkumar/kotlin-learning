package basic

import java.time.LocalDate
import java.util.Properties

fun fibonacci(): Sequence<Int> {
    return generateSequence(Pair(0, 1)) {
        Pair(
            it.second,
            it.first + it.second,
        )
    }.map { it.first }
}

val <T> List<T>.lastIndex: Int get() = size - 1

data class User(val login: String, val email: String, val birthday: LocalDate)

fun getUser() = User("Agata", "ag@t.pl", LocalDate.of(1990, 1, 18))

infix fun <A, B> A.join(b: B): Pair<A, B> = Pair(this, b)

// make a type argument accessible at runtime, use reified

data class ApiResponse(val name: String)
data class Error(val message: String)

fun main() {
    println(fibonacci().take(20).toList())
    listOf(1, 2).lastIndex

    // Destructuring types
    val (name, mail, birthday) = getUser()
    println("$name was born on $birthday, email: $mail")
    "hello" join "test"

    // let, also, apply and run
    /*
    let -> explicit `it` is available in block and can return a different type
    also -> explicit `it` is available in block and will return the same object on which it called
    apply -> apply is similar to also but implicit `this` is available in block
    run -> similar to let but implicit `this` is available in block and can return a different type
     */
    val letEx: Int = "hello".let { it.length } // Int
    val alsoEx: String = "hello".also { println(it) } // String
    val applyEx: Properties = Properties().apply { put("a", 1) }
    val runEx: Int = "hello".run { length }

    fun <P1, P2, P3, R> ((P1, P2, P3) -> R).curried(): (P1) -> (P2) -> (P3) -> R =
        { p1 -> { p2 -> { p3 -> this(p1, p2, p3) } } }

    fun sum(a: Int, b: Int, c: Int): Int = a + b + c
    val fSum = ::sum.curried()
    val a = fSum(1)(2)(3)
    // more verbose form with explicit types declarations
    val sum3: (Int) -> (Int) -> (Int) -> Int = ::sum.curried()
    val sum2: (b: Int) -> (c: Int) -> Int = sum3(1)
    val sum1: (c: Int) -> Int = sum2(2)
    val result: Int = sum1(3)

    fun length(word: String) = word.length
    fun isEven(x: Int): Boolean = x.rem(2) == 0

    infix fun <P1, R, R2> ((P1) -> R).and(function: (R) -> R2): (P1) -> R2 = {
        function(this(it))
    }

    val isCharCountEven: (word: String) -> Boolean = ::length and ::isEven
}

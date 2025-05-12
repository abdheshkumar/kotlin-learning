package basic

import java.math.BigDecimal
import kotlin.contracts.contract

/*
Lambdas
Higher-order functions
Extension functions
Lambda with a receiver
Scope functions
Builder design pattern
Infix notation
Operator overloading
Inline functions
Singleton design pattern/antipattern
 */

//Lambdas
val myLambda = {
    println("This is my lambda!")
}

val myLambdaV: () -> Unit = {
    println("This is my lambda!")
}

val squareNumber: (Int) -> Int = { x -> x * x }

val squareNumberV: (Int) -> Int = { it * it }

val repetitions: (List<Int>, Int) -> Int = { list, element ->
    var reps = 0
    for (i in list) {
        if (i == element)
            reps++
    }
    reps // return value
}

// High-order functions
fun highOrderFunction(
    x: Int,
    y: Int,
    operation: (Int, Int) -> Int
): Int {
    return operation(x, y)
} // returns the result of the operation

fun printAndExecuteTask(text: String, task: () -> Unit) {
    println(text)
    task()
}

// Extension functions
fun String.addExclamationMark(): String {
    return "$this!"
} // this refers to the receiver object

// Lambdas with receiver
fun StringBuilder.buildString(block: StringBuilder.() -> Unit): String {
    block()
    return toString()
} // this refers to the receiver object

val shuffled: String.() -> String = {
    val indexes = (0 until this.length).shuffled()
    val sb = StringBuilder()
    indexes.forEach { index ->
        sb.append(this[index])
    }
    sb.toString()
}

//Scope functions, apply, let, run, also, with
fun StringBuilder.buildStringWithScope(
    block: StringBuilder.() -> Unit
): String {
    block()
    return toString()
} // this refers to the receiver object

//Infix notation
infix fun String.remove(char: Char): String {
    return this.filter { it != char }
}

@Suppress("INVALID_CHARACTERS")
@JvmName("diamond")
infix fun String.`<>`(x: String): String {
    return this + x
}


data class Amount(val value: BigDecimal, val currency: String)

val Double.USD get() = Amount(BigDecimal("100.00"), "USD")
val Double.EUR
    get() = Amount(BigDecimal("100.00"), "EUR")

val usdAmount: Amount = 22.2.USD
val eurAmount: Amount = 22.2.EUR

infix fun <T> T.shouldBe(expected: T) {
    require(expected === expected) { "Expected $expected, but was $this" }
}


fun testIfExpected(s: String) {
    s.shouldBe("expected") // normal syntax
    s shouldBe "expected" // infix syntax
}

infix fun <P, Q, R> Pair<List<P>, List<Q>>.zipWith(body: (P, Q) -> R): List<R> {
    return first.zip(second, body)
}

val numbers = listOf(1, 2, 3, 4, 5)
val strings = listOf("one", "two", "three", "four")
val result = numbers to strings zipWith { i, c -> "$i - $c" }

// Functional interface
interface StringCheck {
    fun check(s: String): Boolean
}

// verbose code
val stringCheck: StringCheck = object : StringCheck {
    override fun check(s: String): Boolean {
        return s.length > 5
    }
}

fun interface StringCheckV {
    fun check(s: String): Boolean
}

val stringCheckV: StringCheckV = StringCheckV { s -> s.length > 5 }

//Generics
interface Euro
interface BritishPound
data class Currency<T>(val value: BigDecimal)

val Double.EURO get() = Currency<Euro>(BigDecimal("100.00"))
val Double.BRITISH_POUND get() = Currency<BritishPound>(BigDecimal("100.00"))

operator fun <T> Currency<T>.plus(other: Currency<T>): Currency<T> {
    return Currency(this.value + other.value)
}

val works = 22.2.EURO + 4.5.EURO
val worksToo = 22.2.BRITISH_POUND + 4.5.BRITISH_POUND

//This doesn't compile
//val oops = 3.1.EURO + 2.2.BRITISH_POUND

fun <T : Any> T.measureTime(block: T.() -> Unit): Double {
    val start = System.currentTimeMillis()
    repeat(10) { block() }
    val end = System.currentTimeMillis()
    return (end - start) / 1000.0
}

fun complicatedStuff() = "Hello world!"
val env = object {
    val x = complicatedStuff()
}

fun someCall(s: String): String = s

//val nsSomeCall = env.measureTime { someCall(x) }

// Annotations

//Context receiver
interface EnvironmentContext {
    fun getProperty(name: String): String
}

context(env: EnvironmentContext)
fun methodWithContext() {
    val userName = env.getProperty("userName")
}


fun main() {
    val result = highOrderFunction(2, 3) { x, y -> x + y }
    println(result)

    val list = listOf(1, 2, 3, 4, 5)
    val repetitionsResult = repetitions(list, 3)
    println(repetitionsResult)

    val myString = "Hello"
    println(myString.addExclamationMark())

    val stringBuilder = StringBuilder()
    stringBuilder.buildString {
        append("Hello")
        append(" ")
        append("World")
    }
    println(stringBuilder.toString())

    val shuffledString = "Kotlin".shuffled()
    println(shuffledString)

    val resultWithScope = StringBuilder().buildStringWithScope {
        append("Hello")
        append(" ")
        append("World")
    }
    println(resultWithScope)

    val infixResult = "Hello" remove 'o'
    println(infixResult)
    println("hello" `<>` "world")

    val ctx = object : EnvironmentContext {
        override fun getProperty(name: String) = "value-for-$name"
    }

    with(ctx) {
        methodWithContext() // ✅ no need to pass ctx explicitly
    }

    val conditionFullfilled = true
    //immutable List
    val newList = buildList {
        add(1)
        if (conditionFullfilled) {
            add(2)
        }
    }


    val newString = buildString {
        append("Some")

        if (conditionFullfilled) {
            append(" string")
        }
    }

    // immutable Set
    val newSet = buildSet {
        add(1)

        if (conditionFullfilled) {
            addAll(listOf(12))
        }
    }


    // immutable Map
    val newMap = buildMap {
        put("key", "value")

        if (conditionFullfilled) {
            putAll(mapOf("12" to "value"))
        }
    }
}

/*
a[b]=x - fun A.set(b:B,x:X): Unit
a[b, c] - fun A.set(b:B,c:C,x:X): Unit
a() - fun A.invoke(): R
a(b) - fun A.invoke(b:B): R
a(b, c) - fun A.invoke(b:B,c:C): R

a ==b - fun A.equals(b: Any): Boolean
a < b - fun A.compareTo(b: A): Int
a <= b - fun A.compareTo(b: A): Int
a > b - fun A.compareTo(b: A): Int
a >= b - fun A.compareTo(b: A): Int
a + b - fun A.plus(b: A): A
a - b - fun A.minus(b: A): A
a * b - fun A.times(b: A): A
a / b - fun A.div(b: A): A
a % b - fun A.rem(b: A): A
a in b - fun A.contains(b: A): Boolean
a !in b - fun A.contains(b: A): Boolean
a[b] - fun A.get(b: B): X
a[b, c] - fun A.get(b: B, c: C): X
 */
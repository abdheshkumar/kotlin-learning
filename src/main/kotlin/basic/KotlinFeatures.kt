package basic

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
}
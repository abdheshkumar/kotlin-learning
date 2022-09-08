import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

// https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-core/jvm/test/guide
fun main(args: Array<String>) {
    println("Hello World!")

    println("Program arguments: ${args.joinToString()}")

    val person = person {
        name = "John"
        this.age = 25
    }

    person.run { println(this) }

    runBlocking { // this: CoroutineScope
        launch { // launch a new coroutine and continue
            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
            println("World!") // print after delay
        }
        println("Hello") // main coroutine continues while a previous one is delayed
    }
    val counter = AtomicInteger()
    runBlocking {
        withContext(Dispatchers.Default) {
            massiveRun {
                counter.incrementAndGet()
            }
        }
        println("Counter = $counter")
    }
}

suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 100 // number of coroutines to launch
    val k = 1000 // times an action is repeated by each coroutine
    val time = measureTimeMillis {
        coroutineScope { // scope for coroutines
            repeat(n) {
                launch {
                    repeat(k) { action() }
                }
            }
        }
    }
    println("Completed ${n * k} actions in $time ms")
}

data class Person(
    var name: String? = null,
    var age: Int? = null,
    var address: Address? = null
)

data class Address(
    var street: String? = null,
    var number: Int? = null,
    var city: String? = null
)

fun person(block: (Person).() -> Unit): Person = Person().apply(block)

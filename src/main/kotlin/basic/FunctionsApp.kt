package basic

import java.util.*

object FunctionsApp {
    private val capitalizeV1 =
        { str: String -> str.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }

    private val capitalizeV2 = object : Function1<String, String> {
        override fun invoke(p1: String): String {
            return p1.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }

    private fun reverse(str: String): String {
        return str.reversed()
    }

    private fun <T> transform(t: T, f: (T) -> T): T = f(t)
    private inline fun <T, R> withV1(receiver: T, block: T.() -> R): R {
        // return receiver.block()
        return block(receiver)
    }
    private inline fun <T, R> T.withV2(block: T.() -> R): R {
        // return receiver.block()
        return block(this)
    }
    private fun add(a: Int, b: Int): Int {
        return a + b
    }

    private fun printResult(function: (Int, Int) -> Int, a: Int, b: Int) {
        val result = function(a, b)
        print(result)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(transform("hello", capitalizeV1))
        println(transform("hello", capitalizeV2))
        println(transform("hello", ::reverse))
        transform("hello") { reverse(it) }
        transform("hello") { v -> reverse(v) }
        transform("hello", { reverse(it) })
        transform("hello", { v -> reverse(v) })

        val transformer = Transformer()
        println(transform("kotlin", transformer::upperCased))
        println(transform("kotlin", Transformer::lowerCased))
        println(transform("kotlin", Transformer.Companion::lowerCased))

        useMachine(5, PrintMachine())
        useMachine(5, ::println)

        val talbot = Wolf("Talbot")
        talbot(WolfActions.SLEEP) // talbot.invoke(WolfActions.SLEEP)
        talbot[WolfRelationships.ENEMY] = talbot
        talbot[WolfRelationships.ENEMY, WolfRelationships.FRIEND] = talbot

        println(
            withV1("hello") {
                lowercase(Locale.getDefault())
            },
        )
        println(
            "hello".withV2 {
                lowercase(Locale.getDefault())
            },
        )
        "hello".apply { }

        val function = ::add
        println(function(1, 2))
        printResult(::add, 1, 2)
    }
}

class Transformer {
    fun upperCased(str: String): String {
        return str.uppercase(Locale.getDefault())
    }

    companion object {
        fun lowerCased(str: String): String {
            return str.lowercase(Locale.getDefault())
        }
    }
}

typealias Machine<T> = (T) -> Unit

fun <T> useMachine(t: T, machine: Machine<T>) {
    machine(t)
}

class PrintMachine<T> : Machine<T> {
    override fun invoke(p1: T) {
        println(p1)
    }
}
enum class WolfActions {
    SLEEP, WALK, BITE
}
class Wolf(val name: String) {
    operator fun invoke(action: WolfActions) = when (action) {
        WolfActions.SLEEP -> "$name is sleeping"
        WolfActions.WALK -> "$name is walking"
        WolfActions.BITE -> "$name is biting"
    }

    operator fun set(relationship: WolfRelationships, wolf: Wolf) {
        println("${wolf.name} is my new $relationship")
    }
    operator fun set(relationship: WolfRelationships, relationshipV1: WolfRelationships, wolf: Wolf) {
        println("${wolf.name} is my new $relationship,$relationshipV1")
    }
}
enum class WolfRelationships {
    FRIEND, SIBLING, ENEMY, PARTNER
}

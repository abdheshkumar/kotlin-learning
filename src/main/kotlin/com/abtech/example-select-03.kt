package com.abtech // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.channels.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.selects.* // ktlint-disable no-wildcard-imports

fun main() = run {
    fun CoroutineScope.produceNumbers(side: SendChannel<Int>) = produce<Int> {
        for (num in 1..10) { // produce 10 numbers from 1 to 10
            delay(100) // every 100 ms
            select<Unit> {
                onSend(num) {} // Send to the primary channel
                side.onSend(num) {} // or to the side channel
            }
        }
    }
    runBlocking<Unit> {
        val side = Channel<Int>() // allocate side channel
        launch { // this is a very fast consumer for the side channel
            side.consumeEach { println("Side channel has $it") }
        }
        produceNumbers(side).consumeEach {
            println("Consuming $it")
            delay(250) // let us digest the consumed number properly, do not hurry
        }
        println("Done consuming")
        coroutineContext.cancelChildren()
    }
}

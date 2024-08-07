package com.abtech.flowexample // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.channels.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.selects.* // ktlint-disable no-wildcard-imports

fun main() = run {
    fun CoroutineScope.switchMapDeferreds(input: ReceiveChannel<Deferred<String>>) = produce<String> {
        var current = input.receive() // start with first received deferred value
        while (isActive) { // loop while not cancelled/closed
            val next = select<Deferred<String>?> { // return next deferred value from this select or null
                input.onReceiveCatching { update ->
                    update.getOrNull()
                }
                current.onAwait { value ->
                    send(value) // send value that current deferred has produced
                    input.receiveCatching().getOrNull() // and use the next deferred from the input channel
                }
            }
            if (next == null) {
                println("Channel was closed")
                break // out of loop
            } else {
                current = next
            }
        }
    }

    fun CoroutineScope.asyncStringAsync(str: String, time: Long) = async {
        delay(time)
        str
    }

    runBlocking<Unit> {
        val chan = Channel<Deferred<String>>() // the channel for test
        launch { // launch printing coroutine
            for (s in switchMapDeferreds(chan))
                println(s) // print each received string
        }
        chan.send(asyncStringAsync("BEGIN", 100))
        delay(200) // enough time for "BEGIN" to be produced
        chan.send(asyncStringAsync("Slow", 500))
        delay(100) // not enough time to produce slow
        chan.send(asyncStringAsync("Replace", 100))
        delay(500) // give it time before the last one
        chan.send(asyncStringAsync("END", 500))
        delay(1000) // give it time to process
        chan.close() // close the channel ...
        delay(500) // and wait some time to let it finish
    }
}

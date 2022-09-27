package com.abtech // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.channels.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.selects.* // ktlint-disable no-wildcard-imports

fun main() = run {
    suspend fun selectAorB(a: ReceiveChannel<String>, b: ReceiveChannel<String>): String =
        select<String> {
            a.onReceiveCatching { it ->
                val value = it.getOrNull()
                if (value != null) {
                    "a -> '$value'"
                } else {
                    "Channel 'a' is closed"
                }
            }
            b.onReceiveCatching { it ->
                val value = it.getOrNull()
                if (value != null) {
                    "b -> '$value'"
                } else {
                    "Channel 'b' is closed"
                }
            }
        }
    runBlocking<Unit> {
        val a = produce<String> {
            repeat(4) { send("Hello $it") }
        }
        val b = produce<String> {
            repeat(4) { send("World $it") }
        }
        repeat(8) { // print first eight results
            println(selectAorB(a, b))
        }
        coroutineContext.cancelChildren()
    }
}

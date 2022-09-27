package com.abtech // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import kotlin.system.* // ktlint-disable no-wildcard-imports

val counterContext = newSingleThreadContext("CounterContext")
var counterZero = 0

fun main() = run {
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
    runBlocking {
        withContext(Dispatchers.Default) {
            massiveRun {
                // confine each increment to a single-threaded context
                withContext(counterContext) {
                    counterZero++
                }
            }
        }
        println("Counter = $counterZero")
    }
}

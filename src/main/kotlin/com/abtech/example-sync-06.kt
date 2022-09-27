package com.abtech // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.sync.* // ktlint-disable no-wildcard-imports
import kotlin.system.* // ktlint-disable no-wildcard-imports

val mutex = Mutex()
var counterM = 0

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
                // protect each increment with lock
                mutex.withLock {
                    counterM++
                }
            }
        }
        println("Counter = $counterM")
    }
}

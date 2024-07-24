@file:Suppress("ktlint:standard:filename", "ktlint:standard:no-wildcard-imports")

package com.abtech

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*
import kotlin.system.*

val mutex = Mutex()
var counterM = 0

fun main() =
    run {
        suspend fun massiveRun(action: suspend () -> Unit) {
            val n = 100 // number of coroutines to launch
            val k = 1000 // times an action is repeated by each coroutine
            val time =
                measureTimeMillis {
                    coroutineScope {
                        // scope for coroutines
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

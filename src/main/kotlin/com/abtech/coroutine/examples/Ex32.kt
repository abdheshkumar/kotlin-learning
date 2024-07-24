package com.abtech.coroutine.examples

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/*
Dispatchers.IO has a special behavior defined for the limitedParallelism function.
It creates a new dispatcher with an independent pool of threads.
 */

suspend fun printCoroutinesTime(dispatcher: CoroutineDispatcher) {
    val test =
        measureTimeMillis {
            coroutineScope {
                repeat(100) {
                    launch(dispatcher) {
                        Thread.sleep(1000)
                    }
                }
            }
        }
    println("$dispatcher took: $test")
}

suspend fun main(): Unit =
    coroutineScope {
        launch {
            printCoroutinesTime(Dispatchers.IO)
            // Dispatchers.IO took: 2074
        }
        launch {
            val dispatcher =
                Dispatchers.IO
                    .limitedParallelism(100)
            printCoroutinesTime(dispatcher)
            // LimitedDispatcher@XXX took: 1082
        }
    }

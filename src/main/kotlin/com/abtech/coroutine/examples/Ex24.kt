package com.abtech.coroutine.examples

import kotlinx.coroutines.*

suspend fun longTaskV() =
    coroutineScope {
        launch {
            delay(1000)
            val name = coroutineContext[CoroutineName]?.name
            println("[$name] Finished task 1")
        }
        launch {
            delay(2000)
            val name = coroutineContext[CoroutineName]?.name
            println("[$name] Finished task 2")
        }
    }

fun main(): Unit =
    runBlocking {
        val job =
            launch(CoroutineName("Parent")) {
                longTaskV()
            }
        delay(1500)
        job.cancel()
    }
/*
you can observe how cancellation works. A cancelled parent leads to the cancellation of unfinished children.
 */

package com.abtech.coroutine.examples

import kotlinx.coroutines.*

/*
The result of ensureActive() and yield() seem similar, but they are very different.
The function ensureActive() needs to be called on a CoroutineScope (or CoroutineContext, or Job).
All it does is throw an exception if the job is no longer active.

yield is more often used just in suspending functions that are CPU intensive or are blocking threads.
 */
suspend fun main(): Unit =
    coroutineScope {
        val job = Job()
        launch(job) {
            repeat(10) { num ->
                Thread.sleep(10)
                ensureActive()
                println("Printing $num")
            }
        }
        delay(10)
        job.cancelAndJoin()
        println("Cancelled successfully")
    }

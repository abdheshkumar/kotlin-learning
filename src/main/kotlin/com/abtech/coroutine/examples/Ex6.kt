package com.abtech.coroutine.examples

import kotlinx.coroutines.*

/*
Stopping the unstoppable
Cancellation happens at the suspension points, it will not happen if there is no suspension point.
To simulate such a situation, we could use Thread.sleep instead of delay
 */
suspend fun main(): Unit =
    coroutineScope {
        val job = Job()
        launch(job) {
            repeat(10) { i ->
                Thread.sleep(10) // We might have some complex operations or reading files here
                // yield() // This is a suspension point
                println("Printing $i")
            }
        }
        delay(10)
        job.cancelAndJoin()
        println("Cancelled successfully")
        delay(10)
    }
/*
It is a good practice to use yield in suspend functions, between blocks of non-suspended CPU-intensive or time-intensive operations.
 */

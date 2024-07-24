package com.abtech.coroutine.examples

import kotlinx.coroutines.*

suspend fun main(): Unit =
    coroutineScope {
        val job =
            launch {
                delay(20)
            }
        job.invokeOnCompletion { exception: Throwable? ->
            // null if the job finished with no exception;
            // CancellationException if the coroutine was cancelled;
            // the exception that finished a coroutine
            println("Finished with exception: $exception")
        }
        delay(10)
        job.cancelAndJoin()
    }

package com.abtech.coroutine.examples

import kotlinx.coroutines.*

suspend fun main(): Unit =
    coroutineScope {
        val job = Job()
        launch(job) {
            try {
                delay(20)
                println("Job is done")
            } finally {
                println("Finally")
                withContext(NonCancellable) {
                    delay(10) // here exception is thrown
                    println("Cleaning up job done")
                }
            }
        }
        delay(10)
        job.cancelAndJoin()
        println("Cancel done")
    }

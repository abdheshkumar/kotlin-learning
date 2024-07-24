package com.abtech.coroutine.examples

import kotlinx.coroutines.*

fun main(): Unit =
    runBlocking {
        val job = SupervisorJob()
        launch(job) {
            delay(10)
            throw Error("Some error")
        }
        launch(job) {
            delay(20)
            println("Will be printed")
        }
        job.join()
    }

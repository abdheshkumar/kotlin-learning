package com.abtech.coroutine.examples

import kotlinx.coroutines.*

// supervisorScope cannot be replaced with withContext(SupervisorJob())
/*
SupervisorJob is a parent of withContext coroutine. When a child has an exception,
it propagates to coroutine coroutine, cancels its Job, cancels children, and throws an exception.
The fact that SupervisorJob is a parent changes nothing.
 */
fun main(): Unit =
    runBlocking {
        withContext(SupervisorJob()) {
            launch {
                delay(10)
                throw Error("Some error")
            }
            launch {
                delay(20)
                println("Will be printed")
            }
        }
        delay(10)
        println("Done")
    }

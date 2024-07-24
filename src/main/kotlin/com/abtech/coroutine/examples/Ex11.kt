package com.abtech.coroutine.examples

import kotlinx.coroutines.*

// SupervisorJob is generally used as part of a scope in which we start multiple coroutines,
// and we want to isolate the failure of one from the others.
fun main(): Unit =
    runBlocking {
        val scope = CoroutineScope(SupervisorJob())
        scope.launch {
            delay(10)
            throw Error("Some error")
        }
        scope.launch {
            delay(20)
            println("Will be printed")
        }
        delay(30)
    }

package com.abtech.coroutine.examples

import kotlinx.coroutines.*

// Stop breaking my coroutines
// Catching an exception before it breaks a coroutine is helpful
fun main(): Unit =
    runBlocking {
// Don't wrap in a try-catch here. It will be ignored.
        try {
            launch {
                delay(1000)
                throw Error("Some error")
            }
        } catch (e: Throwable) {
            // nope, does not help here
            println("Will not be printed")
        }
    }

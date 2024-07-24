package com.abtech.coroutine.examples

import kotlinx.coroutines.*

object MyNonPropagatingException : CancellationException()

// CancellationException does not propagate to its parent

/**
 * If an exception is a subclass of CancellationException, it will not be propagated to its parent.
 * It will only cause cancellation of the current coroutine.
 * CancellationException is an open class, so it can be extended by our own classes or objects.
 */
suspend fun main(): Unit =
    coroutineScope {
        launch {
            // 1
            launch {
                // 2
                delay(2000)
                println("Will not be printed")
            }
            throw com.abtech.coroutine.examples.MyNonPropagatingException // 3
        }
        launch {
            // 4
            delay(2000)
            println("Will be printed")
        }
    }

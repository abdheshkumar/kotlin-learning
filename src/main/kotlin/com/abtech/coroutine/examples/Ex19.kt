package com.abtech.coroutine.examples

import kotlinx.coroutines.*

suspend fun main(): Unit =
    coroutineScope {
        launch {
            // 1
            launch {
                // 2
                delay(2000)
                println("Will not be printed")
            }
            throw RuntimeException("Unexpected error..") // 3
        }
        launch {
            // 4
            delay(2000)
            println("Will be printed")
        }
    }

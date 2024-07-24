@file:Suppress("ktlint:standard:filename", "ktlint:standard:no-wildcard-imports")

package com.abtech

import kotlinx.coroutines.*

suspend fun main() =
    coroutineScope {
        // this: CoroutineScope
        launch {
            // launch a new coroutine and continue
            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
            println("World!") // print after delay
        }
        println("Hello") // main coroutine continues while a previous one is delayed
    }

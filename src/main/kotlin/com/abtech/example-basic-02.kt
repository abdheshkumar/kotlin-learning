package com.abtech // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports

fun main() = run {
    // this is your first suspending function
    suspend fun doWorld() {
        delay(1000L)
        println("World!")
    }

    runBlocking { // this: CoroutineScope
        launch { doWorld() }
        println("Hello")
    }
}

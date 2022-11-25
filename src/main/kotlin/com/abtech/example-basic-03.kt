package com.abtech // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports

fun main() = run {
    suspend fun doWorldV(cs: CoroutineScope) = coroutineScope { // this: CoroutineScope
        cs.launch {
            delay(500)
            println("Background processing!")
            delay(500)
            println("Background processing finished!")
        }
        println("Doing something")
    }

    runBlocking {
        println("Calling for background processing")
        doWorldV(this)
        println("Calling for background processing triggered")
        delay(2000)
        println("Application stopped")
    }
}

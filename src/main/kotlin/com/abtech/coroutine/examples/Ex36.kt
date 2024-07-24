package com.abtech.coroutine.examples

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() {
    runBlocking {
        println("main starts")
        joinAll(
            async { threadSwitchingCoroutine(1, 500) },
            async { threadSwitchingCoroutine(2, 300) }
        )
        println("main ends")
    }
}

suspend fun threadSwitchingCoroutine(number: Int, delay: Long) {
    println("Coroutine $number starts work on ${Thread.currentThread().name}")
    delay(delay)

    //CoroutineScope()
    withContext(Dispatchers.IO) {
        runOnIOThread() // IO Operation
        println("Coroutine $number has finished on ${Thread.currentThread().name}")
    }

    println("Coroutine $number ends work on ${Thread.currentThread().name}")

    withContext(Dispatchers.Default) {
        // CPU Intensive Operation
        println("Coroutine $number bye ${Thread.currentThread().name}")
    }
}

suspend fun runOnIOThread() {
    delay(1000)
    println("runOnThread ${Thread.currentThread().name}")
}
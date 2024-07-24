package com.abtech.coroutine.examples

import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object Ex41 {
    /*
    The executor still uses a thread, but it is one thread for all coroutines using the delay function.
    This is much better than blocking one thread every time we need to wait for some time.

    This is exactly how delay from the Kotlin Coroutines library used to be implemented.
     */
    private val executor = Executors.newSingleThreadScheduledExecutor {
        Thread(it, "scheduler").apply { isDaemon = true }
    }

    suspend fun delay(timeMillis: Long): Unit = suspendCoroutine { cont ->
        executor.schedule({
            cont.resume(Unit)
        }, timeMillis, TimeUnit.MILLISECONDS)
    }

    suspend fun myFunc() {
        println("Before")
        delay(1000)
        println("After")
    }

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        myFunc()
    }
}
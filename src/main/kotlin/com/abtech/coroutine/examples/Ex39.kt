package com.abtech.coroutine.examples

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis

suspend fun main() {
    println("Before")
    delay(1000)
    println("After")
}

suspend fun delay(seconds: Long) =
    suspendCoroutine<Unit> { continuation ->
        thread {
            Thread.sleep(seconds)
            continuation.resume(Unit)
        }
    }
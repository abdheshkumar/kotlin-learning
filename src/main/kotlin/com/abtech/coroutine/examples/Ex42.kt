package com.abtech.coroutine.examples

import kotlinx.coroutines.*
import kotlin.concurrent.thread

suspend fun createCoroutines(coroutines: Int) = coroutineScope {
    val jobs = List(coroutines) {
        launch(Dispatchers.Default) {
            delay(1000)
        }
    }
    jobs.forEach { it.join() }
    coroutines
}

fun createThreads(threads: Int): Int {
    val threadList = mutableListOf<Thread>()
    repeat(threads) {
        val thread = thread { Thread.sleep(1000) }
        threadList.add(thread)
    }

    threadList.forEach { it.join() }
    return threads
}

fun main() {
    runBlocking {
        val threads = 100_00
        val threadList = mutableListOf<Thread>()
        repeat(threads) {
            val thread = thread { Thread.sleep(1000) }
            threadList.add(thread)
        }

        threadList.forEach { it.join() }
        println("Created $threads threads")
    }
}

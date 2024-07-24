package com.abtech.coroutine.examples

import kotlinx.coroutines.*

suspend fun longTask() =
    coroutineScope {
        launch {
            delay(1000)
            val name = coroutineContext[CoroutineName]?.name
            println("[$name] Finished task 1")
        }
        launch {
            delay(2000)
            val name = coroutineContext[CoroutineName]?.name
            println("[$name] Finished task 2")
        }
    }

fun main() =
    runBlocking(CoroutineName("Parent")) {
        println("Before")
        longTask()
        println("After")
    }
/*
  inherits a context from its parent;
• waits for all its children before it can finish itself;
• cancels all its children when the parent is cancelled.
 */

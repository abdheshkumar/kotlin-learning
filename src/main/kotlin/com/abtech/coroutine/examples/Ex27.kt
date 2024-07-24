package com.abtech.coroutine.examples

import kotlinx.coroutines.*

fun main() =
    runBlocking {
        println("Before")
        withContext(SupervisorJob()) {
            launch {
                delay(1000)
                throw Error()
            }
            launch {
                delay(2000)
                println("Done")
            }
        }
        println("After")
    }
/**
withContext
You might notice that the way coroutineScope { /*...*/ } works very similar to async with immediate await: async { /*...*/ }.await().
Also withContext(context) { /*...*/ } is in a way similar to async(context) { /*...*/ }.await().
The biggest difference is that async requires a scope, where coroutineScope and withContext take the scope from suspension.
In both cases, it’s better to use coroutineScope and withContext, and avoid async with immediate await.

 */

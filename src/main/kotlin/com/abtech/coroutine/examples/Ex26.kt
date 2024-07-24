package com.abtech.coroutine.examples

import kotlinx.coroutines.*

fun CoroutineScope.log(text: String) {
    val name = this.coroutineContext[CoroutineName]?.name
    println("[$name] $text")
}

fun main() =
    runBlocking(CoroutineName("Parent")) {
        log("Before")
        withContext(CoroutineName("Child 1")) {
            delay(1000)
            log("Hello 1")
        }
        withContext(CoroutineName("Child 2")) {
            delay(1000)
            log("Hello 2")
        }
        log("After")
    }
/**
withContext
You might notice that the way coroutineScope { /*...*/ } works very similar to async with immediate await: async { /*...*/ }.await().
Also withContext(context) { /*...*/ } is in a way similar to async(context) { /*...*/ }.await().
The biggest difference is that async requires a scope, where coroutineScope and withContext take the scope from suspension.
In both cases, it’s better to use coroutineScope and withContext, and avoid async with immediate await.

 */


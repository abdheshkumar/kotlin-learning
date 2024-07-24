package com.abtech.coroutine.examples

import kotlinx.coroutines.*

suspend fun main() {
    runBlocking {
        launch {
            delay(10)
            println("Hello, coroutine!")
        }
    }
}


suspend fun parentChildRelationshipVV(): Unit =
    coroutineScope {
        launch {
            delay(20) // Pretend to do some work
            println("Child-1")
            launch {
                delay(20) // Pretend to do some work
                println("Child-1.1")
                launch {
                    delay(20) // Pretend to do some work
                    println("Child-1.1.1")
                }
            }
        }
        launch {
            delay(10) // Pretend to do some work
            println("Child-2")
            launch {
                delay(20) // Pretend to do some work
                println("Child-2.1")
            }
        }
        val result = async {
            delay(20) // Pretend to do some work
            "Response from async"
        }
        launch {
            delay(20) // Pretend to do some work
            println("Child-3")
        }
        val response = result.await()
        println("Result..$response")
    }


suspend fun parentChildRelationshipV(): Unit =
    coroutineScope {
        launch(CoroutineName("child1")) { // Adding extra context to the coroutine
            delay(20)
            println("[${coroutineContext[CoroutineName.Key]?.name}] Text 1")
        }
        launch(CoroutineName("child2")) { // Adding extra context to the coroutine
            delay(10)
            throw Exception("Some error..") // Pretend to fail
        }
        delay(20) // Pretend to do some work
        println("doing some work..")
    }

suspend fun parentChildRelationship(): Unit =
    coroutineScope { // Creating a new scope
        val job = Job()
        launch(job + CoroutineName("child1")) {
            // the new job replaces one from parent
            delay(10)
            println("[${coroutineContext[CoroutineName.Key]?.name}] Text 1")
        }
        launch(job + CoroutineName("child2")) {
            // the new job replaces one from parent
            delay(20)
            println("[${coroutineContext[CoroutineName.Key]?.name}] Text 2")
        }
        // Log the names of all child coroutines
        println("coroutineContext children: ${coroutineContext.job.children.count()}")
        println("Job children: ${job.children.count()}")
    }

suspend fun parentChildRelationshipV2(): Unit {
    val scope = CoroutineScope(CoroutineName("Parent"))

    scope.launch(CoroutineName("child1")) {
        // the new job replaces one from parent
        delay(10)
        println("[${coroutineContext[CoroutineName.Key]?.name}] Text 1")
    }
    scope.launch(CoroutineName("child2")) {
        // the new job replaces one from parent
        delay(20)
        println("[${coroutineContext[CoroutineName.Key]?.name}] Text 2")
    }
    // Log the names of all child coroutines
    println("coroutineContext children: ${scope.coroutineContext.job.children.count()}")
    scope.coroutineContext.job.children.forEach { it.join() }
}
package com.abtech

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class CoroutineLaunch {
    fun launchExample() = runBlocking {
        println("Main program start: ${Thread.currentThread().name}") // Main thread
        val job: Job = launch { // main thread
            println("Work starts: ${Thread.currentThread().name}") // main thread
            delay(1000) // Coroutine is suspended but Thread: main is free(not blocked)
            println("Work finished: ${Thread.currentThread().name}") // Either main thread or some other thread
        }
        // delay(2000) // main thread: wait for coroutine to finish(practically not a right way to wait)
        job.join()
        println("Main program ends: ${Thread.currentThread().name}")
    }

    suspend fun launchWithScope() = coroutineScope {
        // this: CoroutineScope instance
        // coroutineContext: CoroutineContext instance

        /* Without Parameter: CONFINED      [CONFINED DISPATCHER]
            - Inherits CoroutineContext from immediate parent coroutine.
            - Even after delay() or suspending function, it continues to run in the same thread.  */
        launch {
            println("C1: ${Thread.currentThread().name}") // Thread: main
            delay(1000)
            println("C1 after delay: ${Thread.currentThread().name}") // Thread: main
        }

        /* With parameter: Dispatchers.Default [similar to GlobalScope.launch { } ]
            - Gets its own context at Global level. Executes in a separate background thread.
            - After delay() or suspending function execution,
                it continues to run either in the same thread or some other thread.  */
        launch(Dispatchers.Default) {
            println("C2: ${Thread.currentThread().name}") // Thread: T1
            delay(1000)
            println("C2 after delay: ${Thread.currentThread().name}") // Thread: Either T1 or some other thread
        }

        /*  With parameter: Dispatchers.Unconfined      [UNCONFINED DISPATCHER]
            - Inherits CoroutineContext from the immediate parent coroutine.
            - After delay() or suspending function execution, it continues to run in some other thread.  */
        launch(Dispatchers.Unconfined) {
            println("C3: ${Thread.currentThread().name}") // Thread: main
            delay(1000)
            println("C3 after delay: ${Thread.currentThread().name}") // Thread: some other thread T1
        }

        launch(coroutineContext) {
            println("C4: ${Thread.currentThread().name}") // Thread: main
            delay(1000)
            println("C4 after delay: ${Thread.currentThread().name}") // Thread: main
        }

        println("...Main Program...")
    }

    suspend fun testStructuredConcurrency() {
        coroutineScope {
            launch {
                launch {
                    println("child1 is running")
                    delay(100)
                }.invokeOnCompletion { println("coroutine child1 is completed") }

                launch {
                    println("child2 is running")
                    delay(200)
                }.invokeOnCompletion { println("coroutine child2 is completed") }

                println("coroutine parent is waiting its children to complete")
            }.invokeOnCompletion { println("coroutine parent is completed") }
            println("waiting all launched coroutines to complete to end the scope")
        }

        println("all launched coroutines completed, end of scope")
    }

    suspend fun testRunConcurrentTasks() {
        coroutineScope {
            launch(Dispatchers.IO) {
                repeat(5) {
                    println("coroutine A is doing something")
                    delay(100)
                }
            }

            launch(Dispatchers.IO) {
                repeat(5) {
                    println("coroutine B is doing something")
                    delay(100)
                }
            }
        }
    }

    suspend fun testNonBlocking() {
        coroutineScope {
            println("launch a coroutine A")
            val jobA = launch {
                println("coroutine A is doing something")
                delay(100)
            }
            jobA.invokeOnCompletion { println("coroutine A is completed") }

            println("do other things")
        }
    }

    suspend fun testLazyStart() {
        coroutineScope {
            println("launch a coroutine A")
            val jobA = launch(start = CoroutineStart.LAZY) {
                println("coroutine A is doing something")
                delay(1000)
            }
            jobA.invokeOnCompletion { println("coroutine A is completed") }

            println("do other things")
            delay(200)
            jobA.start()
            println("started jobA")
        }
    }

    suspend fun testCustomScope() {
        val scope = CoroutineScope(Dispatchers.Default)
        repeat(3) { index ->
            scope.launch {
                delay((100 * index).toLong())
                println("index: $index doing something")
            }.invokeOnCompletion { cause ->
                if (cause is CancellationException) println("coroutine $index is cancelled")
                else println("coroutine $index is completed")
            }
        }

        scope.launch {
            delay(50)
            println("throw exception")
            throw Exception()
        }
    }

    suspend fun testGlobalScope() {
        repeat(3) { index ->
            GlobalScope.launch {
                delay((100 * index).toLong())
                println("index: $index doing something")
            }.invokeOnCompletion { cause ->
                if (cause is CancellationException) println("coroutine $index is cancelled")
                else println("coroutine $index is completed")
            }
        }

        GlobalScope.launch {
            delay(50)
            println("throwing exception")
            throw Exception()
        }
    }

    suspend fun testFlow() {
        flow {
            repeat(5) { index ->
                delay(100)
                emit(index)
            }
        }
            .filter { item -> item > 1 }
            .map { item -> item * 2 }
            .transform { item ->
                emit(item)
                emit("100")
            }
            .collect { item -> println("collect $item") }
    }

    suspend fun testCancelPropagation(): Job {
        return coroutineScope {
            repeat(3) { index ->
                println(index)
                launch {
                    println("coroutine $index is started")
                    delay((100 * index).toLong())
                    println("coroutine $index is completed")
                }.invokeOnCompletion { cause ->
                    if (cause is CancellationException) println("coroutine $index is cancelled")
                }
            }
            println("hhh")
            launch {
                delay(50)
                println("throwing exception, cancel the coroutine and propagate immediately")
                throw Exception()
            }
        }
    }
    
    suspend fun testCancelPropagation1() {
        coroutineScope{
            val job = launch(CoroutineName("parent")) {
                println("starting parent")

                // launch first coroutine
                launch(CoroutineName("child_1")) {
                    println("starting first coroutine")
                    delay(10)
                    throw RuntimeException("simulating failure")
                }

                // launch second coroutine
                launch(CoroutineName("child_2")) {
                    println("starting second coroutine")
                    delay(1000) // Thread.sleep(1000)
                    println("this will never be printed from second coroutine")
                }

                delay(1000)
                println("this will never be printed from scope")
            }

            job.join()
            println("this will never be printed from main")
        }
    }
}

suspend fun main() {
    val coroutineLaunch = CoroutineLaunch()
    // coroutineLaunch.launchExample()
    // runBlocking { coroutineLaunch.launchWithScope() }
    // runBlocking { coroutineLaunch.testStructuredConcurrency() }
    // runBlocking { coroutineLaunch.testRunConcurrentTasks() }
    // runBlocking { coroutineLaunch.testNonBlocking() }
    // runBlocking { coroutineLaunch.testLazyStart() }
    /*runBlocking {
        coroutineLaunch.testCustomScope()
        Thread.sleep(5000)
    }*/
    /*runBlocking {
        coroutineLaunch.testGlobalScope()
        Thread.sleep(5000)
    }*/
    //  coroutineLaunch.testFlow()
    // coroutineLaunch.testCancelPropagation()
    coroutineLaunch.testCancelPropagation1()
}

package com.abtech

import basic.join
import kotlinx.coroutines.*

object SupervisorCoroutineApp {
    private fun test() {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        scope.launch {
            repeat(2) { i ->
                val result = withContext(Dispatchers.Default) { doWork(i) }
                log("work result: $result")
            }
        }
    }

    fun test1() {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        scope.launch {
            repeat(2) { i ->
                try {
                    val result = async { doWork(i) }.await()
                    log("work result: $result")
                } catch (t: Throwable) {
                    log("caught exception: $t")
                }
            }
        }
    }

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            println("${throwable.message}")
        }

    // https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html#debugging-using-logging
    private fun test2() {
        val scope =
            CoroutineScope(
                CoroutineName("Background-Process-Supervisor-Scope") + SupervisorJob() + Dispatchers.IO + coroutineExceptionHandler,
            )
        scope.launch {
            launch {
                delay(100)
                // throw Exception("failed")
            }
            repeat(2) { i ->
                try {
                    val result = async { doWork(i) }.await()
                    log("work result: $result")
                } catch (t: Throwable) {
                    log("caught exception: $t")
                }
            }
        }
    }

    fun test4() {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        scope.launch {
            repeat(2) { i ->
                try {
                    val result = async(SupervisorJob()) { doWork(i) }.await()
                    log("work result: $result")
                } catch (t: Throwable) {
                    log("caught exception: $t")
                }
            }
        }
    }

    fun test4WithCancellation() {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        val job =
            scope.launch {
                repeat(2) { i ->
                    try {
                        val result = async(SupervisorJob()) { doWork(i) }.await()
                        log("work result: $result")
                    } catch (t: Throwable) {
                        log("caught exception: $t")
                    }
                }
            }
        scope.launch {
            delay(50)
            job.cancel()
        }
    }

    fun test5() {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        scope.launch {
            repeat(2) { i ->
                try {
                    val result = async(SupervisorJob(coroutineContext[Job])) { doWork(i) }.await()
                    log("work result: $result")
                } catch (t: Throwable) {
                    log("caught exception: $t")
                }
            }
        }
    }

    fun test6() {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        scope.launch {
            supervisorScope {
                repeat(2) { i ->
                    try {
                        val result = async { doWork(i) }.await()
                        log("work result: $result")
                    } catch (t: Throwable) {
                        log("caught exception: $t")
                    }
                }
            }
        }
    }

    private suspend fun doWork(iteration: Int): Int {
        delay(100)
        log("working on $iteration")
        return if (iteration == 0) throw WorkException("work failed") else 0
    }

    private fun log(msg: String) {
        println("[${Thread.currentThread()}] $msg")
    }

    private class WorkException(
        msg: String,
    ) : RuntimeException(msg)

    data class Test(
        val a: String,
        val b: Boolean,
    )

    @JvmStatic
    fun main(args: Array<String>) {
        /*val p = Test("", true)
        val (aaa, b) = p

        test2()
        Thread.sleep(2000)*/
        runBlocking {
            // myTest0()
            // myTest4()
            // myTest5()
            // myTest6()
            // myTest7()
            // myTest8()
            // myTest9()

            // myTest3()
            // myTest3_1()
            // myTest3_2()
            // delay(3000)
            myTest11()
            // coroutineContext.job.children.forEach { it.join() }
        }

        println("END....")
    }

    suspend fun myTest0() {
        myTest()
        println("FINISHED....")
    }

    suspend fun myTest() =
        // supervisorScope {
        coroutineScope {
            val str1 =
                async<String> {
                    delay(1000)
                    throw Exception()
                }
            val str2 =
                async {
                    delay(2000)
                    "Text2"
                }

            try {
                println(str1.await())
            } catch (e: Exception) {
                println("STR! $e")
            }
            println(str2.await())
        }

    suspend fun myTest2() {
        val exceptionHandler =
            CoroutineExceptionHandler { _, exception ->
                println("Caught an exception: ${exception.message}")
            }
        supervisorScope {
            val job1 =
                launch(exceptionHandler) {
                    delay(100)
                    println("This coroutine completes successfully.")
                }
            val job2 =
                launch(exceptionHandler) {
                    throw Exception("An exception occurred!")
                }
            listOf(job1, job2).joinAll()
        }
    }

    suspend fun myTest3() {
        val scope = CoroutineScope(SupervisorJob())
        scope.launch {
            // Child 1
            // its parent is a supervisor job
            // new coroutine -> can suspend
            delay(50)
            println("Child 1 Will be printed")
            // scope.launch {
            launch {
                // its parent is not a supervisor job
                // Child 1.1
                delay(100)
                throw Error("Child 1.1 Some error")
            }
            // scope.launch {
            launch {
                // its parent is not a supervisor job
                // Child 1.2
                delay(200)
                println("Child 1.2 Will be printed")
            }
        }
        scope.launch {
            // its parent is a supervisor job
            // Child 2
            delay(200)
            println("Child 2 Will be printed")
        }
    }

    suspend fun myTest3_1() {
        runBlocking {
            val job = SupervisorJob()
            launch(job) {
                delay(10)
                throw Error("Some error")
            }
            launch(job) {
                delay(20)
                println("Will be printed")
            }
            // job.join() //will never finish
            job.children.forEach { it.join() }
        }
    }

    suspend fun myTest3_2() {
        runBlocking {
            // Don't do that, SupervisorJob with one child and no parent works similar to just Job
            val parentJob =
                launch(SupervisorJob()) {
                    println("Parent job: $this, has no parent: ${this.coroutineContext[Job]?.parent}")

                    // 1
                    val childJob1 =
                        launch {
                            // Log the parent of this coroutine
                            println("Child job 1's parent: ${this.coroutineContext[Job]?.parent}")
                            delay(10)
                            throw Error("Some error")
                        }
                    val childJob2 =
                        launch {
                            // Log the parent of this coroutine
                            println("Child job 2's parent: ${this.coroutineContext[Job]?.parent}")
                            delay(20)
                            println("Will not be printed")
                        }
                }

            delay(30)
        }
    }

    suspend fun myTest4() {
        coroutineScope {
            try {
                val deferred =
                    async {
                        throw Exception("Exception from async")
                    }
                deferred.await()
            } catch (e: Exception) {
                // Exception thrown in async WILL be caught here
                println("Caught exception: $e")
            }
        }
    }

    val customExceptionHandler =
        Thread.UncaughtExceptionHandler { thread, exception ->
            println("Custom exception handler caught exception on thread ${thread.name}: ${exception.message}")
        }

    suspend fun myTest5() {
        // Thread.setDefaultUncaughtExceptionHandler(customExceptionHandler)
        supervisorScope {
            // handle in try=catch block
            try {
                launch {
                    throw Exception("Child-1 Coroutine failed")
                }
            } catch (e: Exception) {
                // Exception thrown in launch WILL not be caught here
                println("Caught exception: $e")
            }

            launch {
                delay(1000)
                println("Child 2 This coroutine completes successfully.")
            }
        }
    }

    suspend fun myTest6() {
        val exceptionHandler =
            CoroutineExceptionHandler { _, exception ->
                println("Caught exception: $exception")
            }
        supervisorScope {
            launch(exceptionHandler) {
                throw Exception("Child-1 Coroutine failed")
            }

            launch {
                delay(1000)
                println("Child 2 This coroutine completes successfully.")
            }
        }
    }

    // // If async throws, launch throws without calling .await()
    suspend fun myTest7() {
        supervisorScope {
            launch {
                val deferred =
                    async(/*SupervisorJob()*/) {
                        throw Exception("Child-1 Coroutine failed")
                    }
                println("Child 1 This coroutine completes successfully.")
            }

            launch {
                delay(1000)
                println("Child 2 This coroutine completes successfully.")
            }
        }
    }

    // coroutineScope is designed to propagate exceptions to its caller. It doesn't allow exceptions to be caught by a CoroutineExceptionHandler.
    // When an exception occurs inside a coroutineScope, it cancels all its children and then rethrows the exception to its caller.
    suspend fun myTest8() {
        val exceptionHandler =
            CoroutineExceptionHandler { _, exception ->
                println("Caught exception: $exception")
            }
        coroutineScope {
            launch(exceptionHandler) {
                launch(/*exceptionHandler*/) {
                    throw Exception("Failed coroutine")
                }
                println("Child 1 This coroutine completes successfully.")
            }
        }
    }

    // CoroutineExceptionHandler is meant to catch uncaught exceptions at the top level of a coroutine hierarchy, typically in root coroutines started by launch or async directly on a CoroutineScope
    suspend fun myTest9() {
        val exceptionHandler =
            CoroutineExceptionHandler { _, exception ->
                println("Caught exception: $exception")
            }
        val scope = CoroutineScope(Job() + exceptionHandler)
        scope.launch {
            launch {
                throw Exception("Failed coroutine")
            }
            println("Child 1 This coroutine completes successfully.")
        }
        scope.coroutineContext.job.join() // Wait for all coroutines to complete
    }

    // Use supervisorScope instead of coroutineScope:
    suspend fun myTest10() {
        val exceptionHandler =
            CoroutineExceptionHandler { _, exception ->
                println("Caught exception: $exception")
            }

        supervisorScope {
            launch(exceptionHandler) {
                launch {
                    throw Exception("Failed coroutine")
                }
                println("Child 1 This coroutine completes successfully.")
            }
        }
    }
    /*
    coroutineScope is designed for cases
    where you want to ensure all launched coroutines complete successfully or propagate any failure immediately.
    If you need to handle exceptions separately or allow some coroutines to fail without affecting others,
    supervisorScope or a custom CoroutineScope is more appropriate.
     */
}

//the parent does not wait for its children because it has no relation with them
// This is because the child uses the job from the argument as a parent, so it has no relation to the runBlocking.
suspend fun myTest11() =
    coroutineScope {
        val name = CoroutineName("Some name")
        val job = Job()
        launch(name + job) {
            val childName = coroutineContext[CoroutineName]
            println(childName == name) // true
            val childJob = coroutineContext[Job]
            println(childJob == job) // false
            delay(100)
            println("final.....")
            //println(childJob == job.children.first()) // true
        }
        //job.children.forEach { it.join() }
        //job.complete()
    }

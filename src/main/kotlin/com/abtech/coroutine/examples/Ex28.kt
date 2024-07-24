package com.abtech.coroutine.examples

import kotlinx.coroutines.*

object Ex28 {
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

    // Switching to IO Dispatcher for Blocking IO Work
    fun ex1() =
        runBlocking {
            val result =
                withContext(Dispatchers.IO) {
                    // Simulate blocking IO work
                    Thread.sleep(1000)
                    "IO Result"
                }
            println("Result: $result")
        }

    fun ex2() =
        runBlocking {
            val result =
                withContext(Dispatchers.Default) {
                    // Simulate CPU-intensive work
                    (1..1_000_000).sum()
                }
            println("Result: $result")
        }

    // Adding Coroutine Name for Debugging

    fun ex3() =
        runBlocking {
            withContext(CoroutineName("MyCoroutine")) {
                println("Running in coroutine context: ${coroutineContext[CoroutineName]}")
            }
        }

    fun ex4() =
        runBlocking {
            val job = Job()
            withContext(Dispatchers.Default + job) {
                println("Running with combined context: $coroutineContext")
            }
        }

    // Using withContext in a supervisorScope
    // You can combine withContext with a supervisorScope to manage coroutines and switch contexts for specific tasks.
    fun ioOperation() {
        // Simulate IO work
        Thread.sleep(500)
        println("IO work completed")
    }

    fun cpuOperation() {
        // Simulate CPU work
        Thread.sleep(500)
        println("CPU work completed")
    }

    fun ex5() =
        runBlocking {
            supervisorScope {
                val res1 =
                    async(Dispatchers.IO) {
                        // Simulate IO work
                        com.abtech.coroutine.examples.Ex28.ioOperation()
                    }

                val res2 =
                    async(Dispatchers.Default) {
                        // Simulate CPU work
                        com.abtech.coroutine.examples.Ex28.cpuOperation()
                    }

                // Await results
                val result1 = res1.await()
                val result2 = res2.await()

                println("Results: $result1, $result2")
            }
        }

    fun ex6() =
        runBlocking {
            supervisorScope {
                val res1 =
                    async {
                        // Simulate IO work
                        withContext(Dispatchers.IO) {
                            com.abtech.coroutine.examples.Ex28.ioOperation()
                        }
                    }

                val res2 =
                    async {
                        // Simulate CPU work
                        withContext(Dispatchers.Default) {
                            com.abtech.coroutine.examples.Ex28.cpuOperation()
                        }
                    }

                // Await results
                val result1 = res1.await()
                val result2 = res2.await()

                println("Results: $result1, $result2")
            }
        }
}

/**
withContext is used in Kotlin coroutines to change the context of a coroutine block, typically to switch to a different dispatcher or to add or modify context elements

 */

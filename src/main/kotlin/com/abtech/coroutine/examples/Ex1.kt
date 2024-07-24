package com.abtech.coroutine.examples

import kotlinx.coroutines.*

fun main(): Unit =
    runBlocking {
        // Parent coroutine
        println("1: $this")
        val parentJob =
            this.launch {
                // First child coroutine
                println("2: $this")
                launch {
                    val v = this
                    println("3: $v")
                    try {
                        println("Child coroutine 1 starts")
                        delay(100)
                        throw RuntimeException("Exception in child coroutine 1")
                    } catch (e: Exception) {
                        println("Caught exception in child coroutine 1: $e")
                    }
                }

                // Second child coroutine
                launch {
                    println("4: $this")
                    try {
                        println("Child coroutine 2 starts")
                        delay(200)
                        println("Child coroutine 2 ends")
                    } catch (e: Exception) {
                        println("Caught exception in child coroutine 2: ${e.message}")
                    }
                }

                // Third child coroutine
                launch {
                    println("5: $this")
                    try {
                        println("Child coroutine 3 starts")
                        delay(300)
                        println("Child coroutine 3 ends")
                    } catch (e: Exception) {
                        println("Caught exception in child coroutine 3: ${e.message}")
                    }
                }
            }

        // Wait for the parent job to complete
        parentJob.join()

        println("Parent coroutine ends")
    }

suspend fun createCoroutines() = coroutineScope {
    launch {
        println("launch coroutine")
        delay(10)
        println("launched coroutine Complete")
    }
    println("Completed createCoroutines()")
}

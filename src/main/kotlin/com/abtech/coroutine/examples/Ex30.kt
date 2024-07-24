package com.abtech.coroutine.examples

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.random.Random

object Ex30 {
    // fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

    suspend fun ex1() =
        coroutineScope {
            repeat(10) {
                launch {
                    // or launch(Dispatchers.Default) {
                    // To make it busy
                    List(1000) { Random.nextLong() }.maxOrNull()
                    val threadName = Thread.currentThread().name
                    println("Running on thread: $threadName")
                }
            }
        }


    val myDispatcher: CoroutineDispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()

    // Create a new single-thread dispatcher
    val mySingleThreadDispatcher: CoroutineDispatcher = newSingleThreadContext("MySingleThread")


    private val customDispatcherV = newFixedThreadPoolContext(4, "CustomPool")

    // Create a custom thread pool
    private val customThreadPool =
        Executors.newFixedThreadPool(4) { runnable ->
            Thread(runnable, "CustomPool-${System.currentTimeMillis()}").apply { isDaemon = true }
        }

    // Create a coroutine dispatcher from the custom thread pool
    private val customDispatcher = customThreadPool.asCoroutineDispatcher()

    @JvmStatic
    fun main(args: Array<String>) =
    // runBlocking(Dispatchers.Default) {
        // runBlocking(customDispatcher) {
        runBlocking {
            ex1()
        }
}

suspend fun main() =
    coroutineScope {
        repeat(1000) {
            launch {
                // or launch(Dispatchers.Default) {
                // To make it busy
                List(1000) { Random.nextLong() }.maxOrNull()
                val threadName = Thread.currentThread().name // DefaultDispatcher-worker-3
                println("Running on thread: $threadName")
            }
        }
    }

/*
by default, launch uses Dispatchers.Default. However, when used within runBlocking, launch inherits the context of its parent, which in this case is the runBlocking context, unless explicitly specified otherwise.
 */

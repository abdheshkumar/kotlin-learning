package com.abtech

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

object GlobalScopeApp {

    private fun work(i: Int) {
        Thread.sleep(1000)
        println("Work $i done")
    }

    fun example1() { // Takes 2 seconds
        val time = measureTimeMillis {
            runBlocking {
                for (i in 1..2) {
                    launch {
                        work(i)
                    }
                }
            }
        }
        println("Done in $time ms")
    }

    /*
    The launch(Dispatchers.Default) creates children coroutines in runBlocking scope,
    so runBlocking waits for their completion automatically.
     */
    fun example2() { // Takes 1 second
        val time = measureTimeMillis {
            runBlocking {
                for (i in 1..2) {
                    launch(Dispatchers.Default) {
                        work(i)
                    }
                }
            }
        }
        println("Done in $time ms")
    }

    /*
    GlobalScope.launch creates global coroutines.
    It is now developer’s responsibility to keep track of their lifetime. We can “fix” an approach with GlobalScope by manually keeping track of the launched coroutines and waiting for their completion using join
     */
    private fun example3() {
        val time = measureTimeMillis {
            runBlocking {
                for (i in 1..2) {
                    GlobalScope.launch {
                        work(i)
                    }
                }
            }
        }
        println("Done in $time ms")
    }

    private fun example4() {
        val time = measureTimeMillis {
            runBlocking {
                val jobs = mutableListOf<Job>()
                for (i in 1..2) {
                    jobs += GlobalScope.launch {
                        work(i)
                    }
                }
                jobs.forEach { it.join() }
            }
        }
        println("Done in $time ms")
    }

    /*
    convert blocking/non-suspended function into a suspending function using withContext
    this suspending version of work1 does not block its caller
    https://medium.com/mobile-app-development-publication/kotlin-coroutine-scope-context-and-job-made-simple-5adf89fcfe94
     */
    suspend fun work1(i: Int) = withContext(Dispatchers.Default) {
        Thread.sleep(1000)
        println("Work $i done")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        example3() // Finished without Work XXX done printing
    }
}

package com.abtech.coroutine.examples

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

fun main() {
    //runWithThread()
    runWithCoroutine()
}

fun runWithThread() = runBlocking {
    println("Activate before starting Thread count = ${Thread.activeCount()}")
    val time = measureTimeMillis {
        val jobs = ArrayList<Thread>()
        repeat(100000) {
            jobs += Thread {
                Thread.sleep(1000L)
            }.also { it.start() }
        }
        println("End activation Thread count = ${Thread.activeCount()}")
        jobs.forEach { it.join() }
    }
    println("Time taken = $time")
}

fun runWithCoroutine() = runBlocking {
    println("Activate before starting Thread count = ${Thread.activeCount()}")
    val time = measureTimeMillis {
        val jobs = ArrayList<Job>()
        repeat(100000) {
            jobs += launch(Dispatchers.Default) {
                delay(1000L)
            }
        }
        println("End activation Thread count = ${Thread.activeCount()}")
        jobs.forEach { it.join() }
    }
    println("Time taken = $time")
}
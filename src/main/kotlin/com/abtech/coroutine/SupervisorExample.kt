package com.abtech.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineExceptionHandler
import java.math.BigInteger
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

// Coroutines Failure Propagation
object SupervisorExample {
    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        test()
        // logMessage()
        /*val supervisorJob = SupervisorJob()
        val deferred = async(supervisorJob) {
            TODO("Not implemented yet!")
        }
        // Wait for it to fail
        // delay(2000)
        // deferred.await()
        println(supervisorJob.children.toList())
        val job = launch {
            println("Running..")
            delay(100)
        }
        job.join()
        // Restart the Job
        job.start()
        job.join()*/
        val job = doWork()
        println(job)
        loadData()
        Thread.sleep(1000)
    }

    private suspend fun test() = coroutineScope {
        val timeV1 = measureTimeMillis {
            (1..2).map { async { findBigPrimeV1() } }.awaitAll()
        }

        val timeV2 = measureTimeMillis {
            (1..2).map { async { findBigPrimeV2() } }.awaitAll()
        }
    }

    private fun logMessage() {
        val exceptionHandler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
            println("Job cancelled due to ${throwable.message}")
        }
        val scope = CoroutineScope(exceptionHandler)
        scope.launch {
            TODO("Not implemented yet!")
        }.invokeOnCompletion { cause ->
            cause?.let {
                println("Job completed with ${it.message}")
            }
        }
        scope.launch {
            println(Thread.currentThread().name)
            TODO("Not implemented yet!")
        }.invokeOnCompletion { cause ->
            cause?.let {
                println("Job completed with ${it.message}")
            }
        }
    }
}

class UserSummary

fun logMessage(msg: String) {
    println("[${Thread.currentThread()}] $msg")
}

suspend fun findBigPrimeV1(): BigInteger {
    logMessage("findBigPrimeV1")
    return BigInteger.probablePrime(4096, Random())
}

suspend fun findBigPrimeV2(): BigInteger = withContext(Dispatchers.Default) {
    logMessage("findBigPrimeV2")
    BigInteger.probablePrime(4096, Random())
}

val job = SupervisorJob() // (1)
val scope = CoroutineScope(Dispatchers.Default)

// may throw Exception
fun doWork(): Deferred<String> = scope.async { TODO("Explicit throw") }

fun loadData() = scope.launch {
    try {
        doWork().await()
    } catch (e: Exception) { println(e) }
}

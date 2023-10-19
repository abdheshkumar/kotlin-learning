package com.abtech

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

    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("${throwable.message}")
    }

    // https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html#debugging-using-logging
    private fun test2() {
        val scope =
            CoroutineScope(CoroutineName("Background-Process-Supervisor-Scope") + SupervisorJob() + Dispatchers.IO + coroutineExceptionHandler)
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
        val job = scope.launch {
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

    private class WorkException(msg: String) : RuntimeException(msg)
    data class Test(val a: String, val b: Boolean)

    @JvmStatic
    fun main(args: Array<String>) {
        /*val p = Test("", true)
        val (aaa, b) = p

        test2()
        Thread.sleep(2000)*/
    }
}

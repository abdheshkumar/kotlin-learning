package com.abtech

import arrow.core.Either
import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports

object LaunchApp {
    private fun produceKafkaAsync(): Deferred<String> {
        val completableDeferred = CompletableDeferred<String>()
        Thread.sleep(2000)
        println("Kafka message producing....")
        completableDeferred.completeExceptionally(Exception("FAILED..."))
        println("Finished with error.")
        return completableDeferred
    }

    private suspend fun api(cs: CoroutineScope): Either<Throwable, Int> = coroutineScope {
        Either.catch { 12 }
            .tap { println("Calling APIs....") }
            .tap {
                cs.launch(Dispatchers.IO) {
                    println("Launch on ${Thread.currentThread().name}")
                    produceKafkaAsync()
                }
            }
            .tap { println("Return API response to caller..") }
    }

    private suspend fun withLog(block: suspend () -> Unit) {
        block()
        println("Logging...")
    }

    private suspend fun apiVV(): Either<Throwable, Int> {
        return Either.catch { 12 }
            .tap { println("Calling APIs....") }
            .tap {
                coroutineScope {
                    launch {
                        produceKafkaAsync()
                    }
                }
            }
            .tap { println("Return API response to caller..") }
    }

    private suspend fun apiV(cs: CoroutineScope) = coroutineScope {
        api(cs)
        println("Doing something..")
        Thread.sleep(1000)
        println("Taking more time..")
    }

    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking {
        // apiV()
        // apiVV()
        withLog { apiV(this) }
        delay(5000)
        println("Done")
    }
}

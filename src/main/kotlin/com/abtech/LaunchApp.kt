package com.abtech

import arrow.core.Either
import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports

object LaunchApp {
    private fun log(message: String) = println("Launch on [${Thread.currentThread().name}] $message")
    private fun produceKafkaAsync(): Deferred<String> {
        val completableDeferred = CompletableDeferred<String>()
        Thread.sleep(2000)
        log("Kafka message producing....")
        completableDeferred.completeExceptionally(Exception("FAILED..."))
        log("Finished with error.")
        return completableDeferred
    }

    private suspend fun api(cs: CoroutineScope): Either<Throwable, Int> = coroutineScope {
        Either.catch { 12 }
            .tap { log("Calling APIs....") }
            .tap {
                cs.launch(Dispatchers.IO) {
                    log("Start producing message")
                    produceKafkaAsync()
                }
            }
            .tap { log("Return API response to caller..") }
    }

    private suspend fun withLog(block: suspend () -> Unit) {
        block()
        log("Logging...")
    }

    private suspend fun apiVV(): Either<Throwable, Int> {
        return Either.catch { 12 }
            .tap { log("Calling APIs....") }
            .tap {
                coroutineScope {
                    launch {
                        log("Start producing message")
                        produceKafkaAsync()
                    }
                }
            }
            .tap { log("Return API response to caller..") }
    }

    private suspend fun apiV(cs: CoroutineScope) = coroutineScope {
        api(cs).fold({
            log("Failed..")
        }) {
            log("Doing something..")
            Thread.sleep(1000)
            log("Taking more time..")
        }
    }

    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking {
        // https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-core/common/src/CoroutineDispatcher.kt
        // https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-core/jvm/src/Dispatchers.kt
        // apiV()
        // apiVV()
        withLog { apiV(this) }
        // withLog { apiVV() }
        delay(5000)
        log("Done")
    }
}

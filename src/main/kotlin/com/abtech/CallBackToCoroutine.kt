package com.abtech

import callback.Callback
import kotlinx.coroutines.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object CallBackToCoroutine {
    private val kafka = Kafka()

    private fun publish(): Deferred<RecordMetadata> = CompletableDeferred<RecordMetadata>().apply {
        kafka.send("hello") { metadata, exception ->
            if (exception == null) complete(metadata)
            else completeExceptionally(exception)
            println(exception)
        }
    }

    private suspend fun publishV(): RecordMetadata = suspendCoroutine { continuation ->
        kafka.send("hello") { metadata, exception ->
            if (exception == null) continuation.resume(metadata)
            else continuation.resumeWithException(exception)
            println(exception)
        }
    }
    private suspend fun cancellableTask(): List<String> {
        return suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation { print("Cancelled…") }
            Thread.sleep(150)
            continuation.resume(emptyList())
        }
    }

    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking {
        // val res: RecordMetadata = publish().await()
        // val res1: RecordMetadata = publishV()
        launch {
            print("1 ")
            print("2 ")
            print("3 ")
            suspendCoroutine<Unit> {
                print("....")
                it.resume(Unit)
            }
            print("4 ")
            println("Done!")
        }

        val job = launch(Dispatchers.IO) {
            val tasks = cancellableTask()
            println(tasks)
        }

        delay(100)
        job.cancel()
    }
}

data class RecordMetadata(val offset: Long, val topic: String)

class Kafka {
    fun <T> send(record: T, callback: Callback): Future<RecordMetadata> {
        return CompletableFuture.supplyAsync {
            println("Publishing...$record message")
            val ex = Exception("Failed to publish")
            callback.onCompletion(null, ex)
            throw ex
        }
    }
}

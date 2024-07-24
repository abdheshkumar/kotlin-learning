package com.abtech.coroutine.examples

import CassandraHelper
import kotlinx.coroutines.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlin.coroutines.resumeWithException


// Java world to Kotlin coroutines
object Ex29 {

    val cassandraHelper = CassandraHelper()

    suspend fun writeData(data: String): String {
        withContext(Dispatchers.IO) {
            cassandraHelper.writeIntoCassandra(data)
        }
        delay(1000)
        return data
    }

    // Switching to IO Dispatcher for Blocking IO Work
    fun ex1() =
        runBlocking {

            withContext(Dispatchers.IO) {
                cassandraHelper.writeIntoCassandra("user-data")
            }
            println("Write operation completed")
        }

    val cassandraDispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()

    fun ex2() =
        runBlocking {
            val cassandraHelper = CassandraHelper()
            withContext(cassandraDispatcher) {
                cassandraHelper.writeIntoCassandra("")
            }
            println("Write operation completed")
        }

    // Using launch with Dispatchers.IO
// If you don't need to return a result from the blocking call, you can use launch to run the blocking call on Dispatchers.IO.
    fun ex3() =
        runBlocking {
            val cassandraHelper = CassandraHelper()
            launch(Dispatchers.IO) {
                cassandraHelper.writeIntoCassandra("user-data")
                println("Write operation completed")
            }
        }

    suspend fun writeToCassandra(helper: CassandraHelper) {
        withContext(Dispatchers.IO) {
            suspendCancellableCoroutine<Unit> { cont ->
                try {
                    helper.writeIntoCassandra("")
                    cont.resume(Unit) {}
                } catch (e: Exception) {
                    cont.resumeWithException(e)
                }
            }
        }
    }

    suspend fun CompletableFuture<Void>.await() {
        return suspendCancellableCoroutine { cont ->
            this.whenComplete { _, exception ->
                if (exception != null) {
                    cont.resumeWithException(exception)
                } else {
                    cont.resume(Unit) {}
                }
            }
            cont.invokeOnCancellation {
                this.cancel(false)
            }
        }
    }

    fun ex4() =
        runBlocking {
            val cassandraHelper = CassandraHelper()
            writeToCassandra(cassandraHelper)
            println("Write operation completed")
        }
}

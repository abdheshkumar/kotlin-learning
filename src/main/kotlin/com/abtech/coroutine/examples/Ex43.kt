package com.abtech.coroutine.examples

import kotlinx.coroutines.*
import java.lang.management.ManagementFactory
import kotlin.coroutines.coroutineContext

suspend fun mySuspendingFunction(): String {
    delay(1)
    logMessage("mySuspendingFunction")
    return "Hello, World!"
}

@OptIn(ExperimentalStdlibApi::class)
suspend fun logMessage(message: String) = println("Log: $message - ${coroutineContext[CoroutineDispatcher.Key]}")

suspend fun propagateDispatchers() = coroutineScope {
    //async(Dispatchers.IO)
    launch(Dispatchers.IO) {
        logMessage("Coroutine-1 Job")
        launch {
            logMessage("Coroutine-2 Job")
            launch {
                logMessage("Coroutine-3 Job")
                mySuspendingFunction()
            }
        }
    }
}

suspend fun mySuspendingNameFunction(): String {
    delay(1)
    println("mySuspendingFunction: ${coroutineContext[CoroutineName.Key]}")
    return "Hello, World!"
}

suspend fun propagateCoroutineName() = coroutineScope {
    launch(CoroutineName("Parent-Coroutine")) {
        println("Coroutine-1 Job: ${coroutineContext[CoroutineName.Key]}")
        launch {
            println("Coroutine-2 Job: ${coroutineContext[CoroutineName.Key]}")
            launch {
                println("Coroutine-3 Job: ${coroutineContext[CoroutineName.Key]}")
                mySuspendingNameFunction()
            }
        }
    }
}

val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    println("Caught $throwable in $coroutineContext")
}

suspend fun propagateCoroutineExceptionHandler() = coroutineScope {
    launch(coroutineExceptionHandler) {
        println("Coroutine-1 Job")
        launch(coroutineExceptionHandler) {
            println("Coroutine-2 Job")
            launch(coroutineExceptionHandler) {
                println("Coroutine-3 Job")
                throw Exception("Some error")
            }
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    runBlocking {

        propagateDispatchers()
        //propagateCoroutineName()
        //propagateCoroutineExceptionHandler()

        /*println("Parent Scope: $this")
        launch(CoroutineName("Parent") + SupervisorJob()) {
            println("Child Coroutine 1: $this ---- ${coroutineContext[CoroutineDispatcher.Key]}")
            launch {
                println("Grandchild Coroutine: $this -- ${coroutineContext[CoroutineDispatcher.Key]} -- ${coroutineContext[Job.Key]}")
                launch {
                    //throw Exception("Some error")
                    delay(101)
                    println("Great Grandchild Coroutine: $this -- ${coroutineContext[CoroutineDispatcher.Key]} -- ${coroutineContext[Job.Key]} -- ${coroutineContext[CoroutineName.Key]?.name}")
                    launch(Dispatchers.IO) {
                        println("Great Great Grandchild Coroutine: $this -- ${coroutineContext[CoroutineDispatcher.Key]} -- ${coroutineContext[Job.Key]}")
                        launch {
                            println("Great Great Great Grandchild Coroutine: $this -- ${coroutineContext[CoroutineDispatcher.Key]}")
                        }
                    }.invokeOnCompletion {
                        println("Cancelled: $it")
                    }
                }
                launch {
                    throw Exception("Some error")
                    println("Great Grandchild Coroutine 2: $this -- ${coroutineContext[CoroutineDispatcher.Key]} -- ${coroutineContext[Job.Key]}")
                }
            }
        }

        launch {
            println("Child Coroutine 2: $this -- ${coroutineContext[CoroutineDispatcher.Key]}")
        }*/
    }
    Thread.sleep(2000)

}

package com.abtech.coroutine.examples

import kotlinx.coroutines.*
import java.util.concurrent.Executors

fun main(): Unit =
    runBlocking {
        supervisorScope {
            launch {
                delay(10)
                throw Error("Some error")
            }
            launch {
                delay(20)
                println("Will be printed")
            }
        }
        delay(10)
        println("Done")
    }

data class UserAction(
    val action: String,
)

suspend fun sendAnalytics(action: UserAction) {
    delay(10)
    throw Error("Some error")
    println("Sent $action")
}

suspend fun writeCache(action: UserAction) {
    delay(10)
    println("Sent $action")
}

suspend fun callApi(action: UserAction): UserAction {
    delay(10)
    println("Sent $action")
    return action
}

suspend fun performAction(action: UserAction): UserAction = supervisorScope {
    launch { // Send analytics without blocking the scope
        sendAnalytics(action)
    }
    val result = callApi(action)
    launch { // write into cache blocking the scope
        writeCache(result)
    }
    result
}

//1- val backgroundScope = CoroutineScope(CoroutineName("background-scope"))
//2- val backgroundScope = CoroutineScope(CoroutineName("background-scope") + Job())
//3- val backgroundScope = CoroutineScope(CoroutineName("background-scope") + Job() + Dispatchers.Default)
//4- val backgroundScope = CoroutineScope(CoroutineName("background-scope") + Job() + Dispatchers.IO)
//5- val backgroundScope = CoroutineScope(CoroutineName("background-scope") + SupervisorJob() + Dispatchers.IO)

fun coroutineExceptionHandler() =
    CoroutineExceptionHandler { _, throwable ->
        // Log to Crashlytics
        // Send crash analytics to Kafka
        //FirebaseCrashlytics
        //   .getInstance()
        // .recordException(throwable)
    }

const val NUMBER_OF_THREADS = 4
/*val dispatcher =
    Executors
        .newFixedThreadPool(NUMBER_OF_THREADS)
        .asCoroutineDispatcher()*/

val dispatcherIO = Dispatchers.IO.limitedParallelism(4)

val backgroundScope =
    CoroutineScope(
        CoroutineName("background-scope") +
                SupervisorJob() +
                dispatcherIO +
                coroutineExceptionHandler()
    )

class ActionService(private val backgroundScope: CoroutineScope) {
    suspend fun performAction(action: UserAction): UserAction = coroutineScope {
        backgroundScope.launch { // Send analytics without blocking the scope
            sendAnalytics(action)
        }
        val result = callApi(action)
        backgroundScope.launch { // write into cache blocking the scope
            writeCache(result)
        }
        result
    }
}

suspend fun performActionV(scope: CoroutineScope, action: UserAction): UserAction = coroutineScope {
    scope.launch { // Send analytics without blocking the scope
        sendAnalytics(action)
    }
    val result = callApi(action)
    scope.launch { // write into cache blocking the scope
        writeCache(result)
    }
    result
}


suspend fun notifyAnalytics(actions: List<UserAction>) =
    supervisorScope {
        actions.forEach { action ->
            launch {
                sendAnalytics(action)
            }
        }
    }

suspend fun notifyAnalyticsV(actions: List<UserAction>) =
    coroutineScope {
        actions.forEach { action ->
            launch {
                sendAnalytics(action)
            }
        }
    }

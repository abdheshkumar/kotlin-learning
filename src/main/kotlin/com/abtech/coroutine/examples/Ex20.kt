package com.abtech.coroutine.examples

import kotlinx.coroutines.*

fun main(): Unit =
    runBlocking {
        val handler =
            CoroutineExceptionHandler { ctx, exception ->
                println("Caught $exception")
            }
        val scope = CoroutineScope(SupervisorJob() + handler)

        scope.launch {
            delay(10)
            throw Error("Some error")
        }
        scope.launch {
            delay(20)
            println("Will be printed")
        }
        delay(30)
    }
/*
Creating a Supervisor Scope
val supervisorScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
Using supervisorScope Builder

Using withContext(SupervisorJob()) is not the proper use of SupervisorJob.
SupervisorJob is used to create a SupervisorScope, which is a special kind of CoroutineScope that does not propagate exceptions to its parent.
SupervisorScope is used to create a scope that does not cancel its parent or siblings on an exception.

Use SupervisorJob to create a new CoroutineScope or within a supervisorScope to properly manage child coroutines and their exceptions.
 */
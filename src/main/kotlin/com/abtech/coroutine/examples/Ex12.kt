package com.abtech.coroutine.examples

import kotlinx.coroutines.*

/**
 * A common mistake is to use a SupervisorJob as an argument to a parent coroutine,
 * like in the code below. It won’t help us handle exceptions, because in such a case SupervisorJob has only one direct child,
 * namely the launch defined at 1 that received this SupervisorJob as an argument.
 * So, in such a case, there is no advantage of using SupervisorJob over Job
 */
fun main(): Unit =
    runBlocking {
// Don't do that, SupervisorJob with one child // and no parent works similar to just Job
        launch(SupervisorJob()) {
            // 1
            launch {
                delay(1000)
                throw Error("Some error")
            }
            launch {
                delay(2000)
                println("Will not be printed")
            }
        }
        delay(3000)
    }

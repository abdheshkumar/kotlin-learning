package com.abtech

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import kotlin.coroutines.EmptyCoroutineContext

// https://kt.academy/book/coroutines
object CoroutineContextApp {
    private fun CoroutineScope.log(msg: String) {
        val name = coroutineContext[CoroutineName]?.name
        println("[$name] $msg")
    }

    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking(CoroutineName("main")) {
        log("Started") // [main] Started
        val v1 = async(CoroutineName("c1")) {
            delay(500)
            log("Running async") // [c1] Running async
            42
        }
        launch(CoroutineName("c2")) {
            delay(1000)
            log("Running launch") // [c2] Running launch
        }
        log("The answer is ${v1.await()}")
        // [main] The answer is 42
        CoroutineScope(EmptyCoroutineContext).launch {
            log("Running on EmptyCoroutineContext")
        }
    }
}

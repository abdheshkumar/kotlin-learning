package com.abtech.flowexample // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.flow.* // ktlint-disable no-wildcard-imports
import java.lang.System.currentTimeMillis

fun main() = run {
    fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // wait 500 ms
        emit("$i: Second")
    }
    runBlocking<Unit> {
        val startTime = currentTimeMillis() // remember the start time
        (1..3).asFlow().onEach { delay(100) } // emit a number every 100 ms
            .flatMapConcat { requestFlow(it) }.collect { value -> // collect and print
                println("$value at ${currentTimeMillis() - startTime} ms from start")
            }
    }
}

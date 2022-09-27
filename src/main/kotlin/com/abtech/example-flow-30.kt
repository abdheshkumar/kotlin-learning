package com.abtech // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.flow.* // ktlint-disable no-wildcard-imports

fun main() = run {
    fun simple(): Flow<Int> = flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i)
        }
    }

    runBlocking<Unit> {
        simple()
            .onEach { value ->
                check(value <= 1) { "Collected $value" }
                println(value)
            }
            .catch { e -> println("Caught $e") }
            .collect()
    }
}

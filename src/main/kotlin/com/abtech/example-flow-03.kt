package com.abtech // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports

fun main() = run {
    suspend fun simple(): List<Int> {
        delay(1000) // pretend we are doing something asynchronous here
        return listOf(1, 2, 3)
    }
    runBlocking<Unit> {
        simple().forEach { value -> println(value) }
    }
}

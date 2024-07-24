package com.abtech.flowexample // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.flow.* // ktlint-disable no-wildcard-imports

fun main() = run {
    fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

    fun simple(): Flow<Int> = flow {
        log("Started simple flow")
        for (i in 1..3) {
            emit(i)
        }
    }
    runBlocking<Unit> {
        simple().collect { value -> log("Collected $value") }
    }
}

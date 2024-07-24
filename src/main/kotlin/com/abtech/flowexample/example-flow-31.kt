package com.abtech.flowexample // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.flow.* // ktlint-disable no-wildcard-imports

fun main() = run {
    fun simple(): Flow<Int> = (1..3).asFlow()
    runBlocking<Unit> {
        try {
            simple().collect { value -> println(value) }
        } finally {
            println("Done")
        }
    }
}

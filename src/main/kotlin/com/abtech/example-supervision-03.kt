package com.abtech // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports

fun main() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception -> println("CoroutineExceptionHandler got $exception") }
    supervisorScope {
        val child = launch(handler) {
            println("The child throws an exception")
            throw AssertionError()
        }
        println("The scope is completing")
    }
    println("The scope is completed")
}

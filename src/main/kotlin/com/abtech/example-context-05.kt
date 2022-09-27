package com.abtech // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports

fun main() = runBlocking<Unit> {
    println("My job is ${coroutineContext[Job]}")
}

package com.abtech.flowexample // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.flow.* // ktlint-disable no-wildcard-imports

fun main() = run {
    // Imitate a flow of events
    fun events(): Flow<Int> = (1..3).asFlow().onEach { delay(100) }
    runBlocking<Unit> {
        events()
            .onEach { event -> println("Event: $event") }
            .launchIn(this) // <--- Launching the flow in a separate coroutine
        println("Done")
    }
}

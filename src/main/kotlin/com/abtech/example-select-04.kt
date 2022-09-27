package com.abtech // ktlint-disable filename

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.selects.* // ktlint-disable no-wildcard-imports
import kotlin.random.Random

fun main() = run {
    fun CoroutineScope.asyncStringAsync(time: Int) = async {
        delay(time.toLong())
        "Waited for $time ms"
    }

    fun CoroutineScope.asyncStringsList(): List<Deferred<String>> {
        val random = Random(3)
        return List(12) { asyncStringAsync(random.nextInt(1000)) }
    }
    runBlocking<Unit> {
        val list = asyncStringsList()
        val result = select<String> {
            list.withIndex().forEach { (index, deferred) ->
                deferred.onAwait { answer ->
                    "Deferred $index produced answer '$answer'"
                }
            }
        }
        println(result)
        val countActive = list.count { it.isActive }
        println("$countActive coroutines are still active")
    }
}

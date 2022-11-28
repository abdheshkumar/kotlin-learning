package com.abtech

import kotlinx.coroutines.CompletableDeferred

object PromiseApp {
    fun f1(completableDeferred: CompletableDeferred<Unit>) {
        Thread.sleep(1000)
        completableDeferred.complete(Unit)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Start")
        val c = CompletableDeferred<Unit>()
        f1(c)
        println("Finished")
    }
}

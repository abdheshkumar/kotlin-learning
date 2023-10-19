package com.abtech

import kotlinx.coroutines.*

object CoroutineSupervisor {

    val scope = CoroutineScope(Dispatchers.IO)

    val job = scope.launch {
        val job1 = launch {
            delay(2000)
        }
        val job2 = launch {
            delay(3000)
        }
        val job3 = launch {
            delay(4000)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(scope.coroutineContext[Job]?.children?.count())
        println(job.children.count())
        // job.cancel()
        scope.coroutineContext[Job]?.children?.forEach { job ->
            // launched job
            println(job)

            // grandchildren
            job.children.forEach {
                println(it)
            }
        }
    }
}

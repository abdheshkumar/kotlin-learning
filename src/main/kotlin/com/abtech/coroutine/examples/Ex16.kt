package com.abtech.coroutine.examples

import kotlinx.coroutines.*

object Ex16 {
    class MyException : Throwable()

    suspend fun parallelExecution() = supervisorScope {
        val str1 =
            async<String> {
                delay(10)
                throw MyException()
            }
        val str2 =
            async {
                delay(20)
                "Text2"
            }
        try {
            println(str1.await())
        } catch (e: MyException) {
            println(e)
        }
        println(str2.await())
    }

    suspend fun parallelExecutionV() = coroutineScope {
        val str1 =
            async<String> {
                delay(10)
                throw MyException()
            }
        val str2 =
            async {
                delay(20)
                "Text2"
            }
        try {
            println(str1)
        } catch (e: MyException) {
            println("Caught...$e")
        }
        println(str2.await())
    }

    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            //parallelExecution()
            parallelExecutionV()
        }
    }
}

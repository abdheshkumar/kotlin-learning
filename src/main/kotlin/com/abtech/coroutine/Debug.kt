package com.abtech.coroutine

import kotlinx.coroutines.*

// Tools > Kotlin > Show Kotlin Bytecode
suspend fun printSomeData(data: String) {
    println("Hello")
    aFun()
    println(data)
    bFun()
    println("world")
}

suspend fun aFun() = withContext(Dispatchers.Default) { println("aFun") }

suspend fun bFun() = withContext(Dispatchers.Default) { println("bFun") }

suspend fun printSomeData(data: Any) {
    delay(100)
    println(data)
    val something = GlobalScope.async {
        ""
    }
    something.await()
}
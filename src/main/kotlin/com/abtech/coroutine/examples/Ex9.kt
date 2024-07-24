package com.abtech.coroutine.examples

import kotlinx.coroutines.*

//Exception handling in coroutines
fun main(): Unit =
    runBlocking { //supervisorScope { // supervisorScope is used to handle exceptions in child coroutines}
        launch {
            launch {
                delay(1000)
                throw Error("Some error")
            }
            launch {
                delay(2000)
                println("Will not be printed")
            }
            launch {
                delay(500) // faster than the exception
                println("Will be printed")
            }
        }
        launch {
            delay(2000)
            println("Will not be printed")
        }
    }

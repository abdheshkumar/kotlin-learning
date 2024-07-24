package com.abtech.coroutine.examples

import kotlinx.coroutines.*

import kotlinx.coroutines.*

fun main() {
    runBlocking {
        println("Running in runBlocking with context: ${coroutineContext[Job]}")
        exampleScope()
    }
}

suspend fun exampleScope(): UserProfile = coroutineScope {
    println("Running in exampleScope with context: ${coroutineContext[Job]}")
    exampleScope1()
    exampleScope2()
    launch {
        println()
    }
    exampleScope3()
}

suspend fun exampleScope1(): UserProfile = coroutineScope {
    println("Running in exampleScope1 with context: ${coroutineContext[Job]}")
    println("hello1")
    UserProfile("John", 30)
}

suspend fun exampleScope2(): UserProfile = coroutineScope {
    println("Running in exampleScope2 with context: ${coroutineContext[Job]}")
    println("hello2")
    UserProfile("John", 30)
}

suspend fun exampleScope3(): UserProfile = coroutineScope {
    println("Running in exampleScope3 with context: ${coroutineContext[Job]}")
    println("hello3")
   val v =  UserProfile("John", 30)
    v.updateObject {
        println("Updating object: $this")
        name
    }

    v.updateObjectV {
        println("Updating object: $this")
        it.name
    }

    v
}

data class UserProfile(
    val name: String,
    val age: Int,
)

fun UserProfile.updateObject(userProfile: UserProfile.() -> String): String {
    val v =  this
    println("Updating object: $userProfile")
    v.userProfile()
    return userProfile(v)
}

fun UserProfile.updateObjectV(userProfile: (UserProfile) -> String): String {
    val v =  this
    println("Updating object: $userProfile")
    return userProfile(v)
}


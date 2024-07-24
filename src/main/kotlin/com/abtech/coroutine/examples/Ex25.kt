package com.abtech.coroutine.examples

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class User(
    val profile: String,
    val friends: String,
)

suspend fun getProfile(): String {
    delay(200)
    println("getProfile...")
    return "Profile"
}

suspend fun getFriends(): String {
    delay(100)
    println("getFriends...")
    return "Friends"
}

suspend fun produceCurrentUserSeq(): User {
    val profile = getProfile()
    val friends = getFriends()
    return User(profile, friends)
}

suspend fun produceCurrentUserPar(): User =
    coroutineScope {
        val profile = async { getProfile() }
        val friends = async { getFriends() }
        User(profile.await(), friends.await())
    }

fun main(): Unit =
    runBlocking {
        val seq = measureTimeMillis { produceCurrentUserSeq() }
        println("---------------------------------")
        val par = measureTimeMillis { produceCurrentUserPar() }
        println("Sequential: $seq ms, Parallel: $par ms")
    }
/*
if there is an exception in coroutineScope or any of its children, it cancels all other children and rethrows it.
 */

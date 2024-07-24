package com.abtech.coroutine.examples

import kotlinx.coroutines.*


fun getFollowersNumberV(): Int = throw Error("Service exception")

suspend fun getUserNameV(): String {
    delay(500)
    return "abdhesh"
}

suspend fun getTweetsV(): List<Ex21.Tweet> = listOf(Ex21.Tweet("Hello, world"))

suspend fun getUserDetailsV(): Ex21.Details =
    coroutineScope {
        val userName = async { getUserNameV() }
        val followersNumber = async { getFollowersNumberV() }
        Ex21.Details(userName.await(), followersNumber.await())
    }

fun main() =
    runBlocking {
        val details =
            try {
                getUserDetailsV()
            } catch (e: Error) {
                null
            }
        val tweets = async { getTweetsV() }
        println("User: $details")
        println("Tweets: ${tweets.await()}")
    }


/*
if there is an exception in coroutineScope or any of its children, it cancels all other children and rethrows it.

 */
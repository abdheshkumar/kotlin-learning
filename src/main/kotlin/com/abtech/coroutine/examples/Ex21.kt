package com.abtech.coroutine.examples

import kotlinx.coroutines.*

object Ex21 {
    data class Details(
        val name: String,
        val followers: Int,
    )

    data class Tweet(
        val text: String,
    )


    suspend fun getNameWithFollower(): Details = coroutineScope {
        val userName = async { getUserName() }
        val followersNumber = async { getFollowersNumber() }
        Details(userName.await(), followersNumber.await())
    }

    suspend fun getTweets(): List<Tweet> {
        delay(500)
        return listOf(Tweet("Hello, world"))
    }

    suspend fun getUserDetailsSeq(): Details {
        val userName = getUserName()
        val followersNumber = getFollowersNumber()
        return Details(userName, followersNumber)
    }

    suspend fun getUserDetails(): Details = coroutineScope {
        val userName = async { getUserName() }
        val followersNumber = async { getFollowersNumber() }
        Details(userName.await(), followersNumber.await())
    }

    suspend fun getUserDetailsWithSupervisor(): Details = supervisorScope {
        val userName = async { getUserName() }
        val followersNumber = async { getFollowersNumber() }
        Details(userName.await(), followersNumber.await())
    }

    suspend fun getUserName(): String {
        delay(500)
        return "abdhesh"
    }

    fun getFollowersNumber(): Int = throw Error("Service exception")

    suspend fun getUserDetailsWithSupervisorV(): Details = supervisorScope { // coroutineScope
        val userName = async { getUserName() }
        val followersNumber = async { getFollowersNumber() }
        Details(userName.await(), runCatching { followersNumber.await() }.getOrNull() ?: 0)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            getUserDetailsWithSupervisorV()
        }
    }


    /*
The problem is that this solution requires this scope to be passed from function to function.
Also, such functions can cause unwanted side effects in the scope; for instance, if there is an exception in one async,
the whole scope will be shut down (assuming it is using Job, not SupervisorJob)
 */
    data class UserProfileData(
        val name: String,
        val age: Int,
    )

    // Pass scope to class constructor
    suspend fun getUserProfileV1(scope: CoroutineScope): UserProfileData {
        val userProfileData = scope
            .async {
                delay(1000) // Pretend to be a long operation
                UserProfileData("John Doe", 30)
            }
        println("Doing some work...")
        return userProfileData.await()
    }

    suspend fun CoroutineScope.getUserProfileV2(): UserProfileData {
        val userProfileData = async {
            delay(1000) // Pretend to be a long operation
            UserProfileData("John Doe", 30)
        }
        println("Doing some work...")
        return userProfileData.await()
    }

}





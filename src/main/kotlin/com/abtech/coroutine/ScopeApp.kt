package com.abtech.coroutine

import kotlinx.coroutines.*

object ScopeApp {
    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        // Parallel
        val userOne = async(Dispatchers.IO) { fetchFirstUser() }
        val userTwo = async(Dispatchers.IO) { fetchSecondUser() }
        println("First user: ${userOne.await()}, Second user: ${userTwo.await()}")
    }

    suspend fun fetchUserEmail(): String {
        // return withContext(Dispatchers.IO) { fetchFirstEmail() }
        return coroutineScope {
            val result = async { fetchFirstEmail() }
            result.await()
        }
    }

    fun fetchFirstEmail(): String {
        // Call DB
        // return Email
        return "ab@expediagroup.com"
    }

    suspend fun fetchFirstUser(): User {
        // make network call
        // return user
        return User("test-user")
    }

    suspend fun fetchSecondUser(): User {
        // make network call
        // return user
        return User("test-user")
    }

    data class User(val name: String)
}

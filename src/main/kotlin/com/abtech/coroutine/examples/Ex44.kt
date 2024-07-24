package com.abtech.coroutine.examples

import kotlinx.coroutines.*
import java.lang.management.ManagementFactory
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

data class UserData(val name: String, val age: Int)

suspend fun callApi(userName: String): UserData {
    delay(1000)
    return UserData("John", 30)
}

suspend fun callApiV(userName: String, continuation: Continuation<UserData>): UserData {
    continuation.resume(UserData("John", 30))
    return UserData("John", 30)
}
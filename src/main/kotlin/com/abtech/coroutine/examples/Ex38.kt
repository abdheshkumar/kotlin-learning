package com.abtech.coroutine.examples

import com.abtech.News
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis

suspend fun testFunc(): String = suspendCoroutine<String> { continuation ->
    println("Before too")
    continuation.resume("Hello")
}

suspend fun main() {
    coroutineScope {
        println("Start fetching news")
        val news: Deferred<List<News>> = async { fetchNewsV("1") }
        delay(10) // Pretend to do some other work
        val newsList = news.await()
        println("Log News: $newsList")
        println("End.")
    }
}


suspend fun fetchNewsV(newsId: String): List<News> = suspendCoroutine { cont ->
    // Simulate network request
    cont.resume(listOf(News("Title", "Content")))
}
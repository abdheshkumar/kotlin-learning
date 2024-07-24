package com.abtech

import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/*suspend fun main() =
    coroutineScope {
        System.setProperty("kotlinx.coroutines.debug", "")
        repeat(10) {
            launch {
                // or launch(Dispatchers.Default) {
                // To make it busy
                List(1000) { Random.nextLong() }.maxOrNull()
                val threadName = Thread.currentThread().name
                println("Running on thread: $threadName")
            }
        }
        test()
    }*/

fun main() {
    System.setProperty("kotlinx.coroutines.debug", "on") // Enable debug agent

    runBlocking {
        val job1 =
            launch(CoroutineName("Job1")) {
                delay(100)
                println("Job1 is done")
            }

        val job2 =
            launch(CoroutineName("Job2")) {
                delay(200)
                println("Job2 is done")
            }

        val job3 =
            launch(CoroutineName("Job3")) {
                repeat(5) { i ->
                    delay(50)
                    println("Job3: Iteration $i")
                }
            }

        joinAll(job1, job2, job3)
    }
}

data class Notification(
    val message: String,
)

interface NotificationsClient {
    suspend fun sendNotification(notification: Notification)
}

class NotificationsSender(
    private val client: NotificationsClient,
    private val notificationScope: CoroutineScope,
) {
    // Does not wait for started coroutines
    // Exceptions are handled by the scope
    // Takes context from the scope
    // and builds relationship to the scope

    fun sendNotifications(notifications: List<Notification>) {
        for (n in notifications) {
            notificationScope.launch {
                client.sendNotification(n)
            }
        }
    }
}

class NotificationsSenderV(
    private val client: NotificationsClient,
) {
    // Waits for its coroutines
    // Handles exceptions
    // Takes context and builds relationship to the coroutine that started it
    suspend fun sendNotifications(notifications: List<Notification>) =
        supervisorScope {
            for (n in notifications) {
                launch {
                }
            }
        }
}

data class News(
    val title: String,
    val content: String,
)

data class UserNews(
    val name: String,
    val news: News,
)

interface CallBack<T> {
    fun onSuccess(news: T)
    fun onError(e: Throwable)
}

fun fetchNews(news: CallBack<News>) {
    // Simulate network request
    news.onSuccess(News("Title", "Content"))
}

fun fetchUserName(name: CallBack<String>) {
    // Simulate network request
    name.onSuccess("John Doe")
}

suspend fun fetchNewsV(newsId: String): News = suspendCoroutine { cont ->
    // Simulate network request
    cont.resume(News("Title", "Content"))
}

suspend fun fetchUserName(userId: String): String = suspendCoroutine { cont ->
    // Simulate network request
    cont.resume("John Doe")
}

suspend fun fetchUserNews(userId: String, newsId: String): UserNews {
    val name = fetchUserName(userId)
    val news = fetchNewsV(newsId)
    return UserNews(name, news)
}

suspend fun fetchUserNewsPar(userId: String, newsId: String): UserNews =
    coroutineScope {
        val name = async { fetchUserName(userId) }
        val news = async { fetchNewsV(newsId) }
        UserNews(name.await(), news.await())
    }


// handle asynchronous and Structured Concurrency
// Sequential requests
// Parallel requests
// Error handling in parallel requests
// Error handling in sequential requests
// fire-and-forget operation, ensuring that it runs concurrently without blocking the main sequential flow
// Non-blocking side effects operation
// Cancellation
// Timeout
// Handling Exceptions and Supervise asynchronous operations
// Resource Safety


fun fetchUserNews(userNews: CallBack<UserNews>) {
    fetchUserName(object : CallBack<String> {
        override fun onSuccess(name: String) {
            fetchNews(object : CallBack<News> {
                override fun onSuccess(news: News) {
                    userNews.onSuccess(UserNews(name, news))
                }

                override fun onError(e: Throwable) {
                    userNews.onError(e)
                }
            })
        }

        override fun onError(e: Throwable) {
            userNews.onError(e)
        }
    })
}

// Callback to suspend function
suspend fun requestNews(): News =
    suspendCoroutine<News> { cont ->
        fetchNews(
            object : CallBack<News> {
                override fun onSuccess(news: News) {
                    cont.resume(news)
                }

                override fun onError(e: Throwable) {
                    cont.resumeWithException(e)
                }
            },
        )
    }

// Callback to suspend function
suspend fun requestUser(): String =
    suspendCoroutine<String> { cont ->
        fetchUserName(
            object : CallBack<String> {
                override fun onSuccess(name: String) {
                    cont.resume(name)
                }

                override fun onError(e: Throwable) {
                    cont.resumeWithException(e)
                }
            },
        )
    }

suspend fun requestUserNews(): UserNews {
    val name = requestUser()
    val news = requestNews()
    return UserNews(name, news)
}
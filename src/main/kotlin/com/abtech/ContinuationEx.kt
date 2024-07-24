package com.abtech

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class ContinuationEx {
    suspend fun fetchNewsV(newsId: String): News = suspendCoroutine { cont ->
        // Simulate network request
        cont.resume(News("Title", "Content"))
    }

    suspend fun fetchUserName(userId: String): String = suspendCoroutine { cont ->
        // Simulate network request
        cont.resume("John Doe")
    }

    suspend fun publishNews(news: News) = suspendCoroutine<Unit>{
        // Simulate network request
        it.resume(Unit)
    }
    suspend fun fetchUserNews(userId: String, newsId: String): UserNews {
        val name = fetchUserName(userId)
        val news = fetchNewsV(newsId)
        publishNews(news)
        return UserNews(name, news)
    }
}
package com.abtech.coroutine.examples

import kotlinx.coroutines.*

/*
@Configuration
class CoroutineScopeConfiguration {
    @Bean
    fun coroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO.limitedParallelism(5)

    @Bean
    fun coroutineExceptionHandler() =
        CoroutineExceptionHandler { _, throwable ->
            // Log to Crashlytics
            // Send crash analytics to Kafka
            FirebaseCrashlytics
                .getInstance()
                .recordException(throwable)
        }

    @Bean
    fun coroutineScope(
        coroutineDispatcher: CoroutineDispatcher,
        coroutineExceptionHandler: CoroutineExceptionHandler,
    ) = CoroutineScope(
        SupervisorJob() +
            coroutineDispatcher +
            coroutineExceptionHandler,
    )
}
*/

val analyticsScope = CoroutineScope(SupervisorJob())

/*
All their exceptions will only be shown in logs; so, if you want to send
them to a monitoring system, use CoroutineExceptionHandler.
 *//*

private val exceptionHandler =
    CoroutineExceptionHandler { _, throwable ->
        FirebaseCrashlytics
            .getInstance()
            .recordException(throwable)
    }

val analyticsScope =
    CoroutineScope(
        SupervisorJob() + exceptionHandler,
    )

*/
/*
For instance, use Dispatchers.IO if you might have blocking calls on this scope,
 *//*

val analyticsScope =
    CoroutineScope(
        SupervisorJob() + Dispatchers.IO,
    )
*/

package com.abtech.coroutine.examples

import kotlinx.coroutines.*

/*
Dispatchers.Default is limited by the number of cores in your processor.
The limit of Dispatchers.IO is 64 (or the number of cores if there are more).

both Dispatchers.Default and Dispatchers.IO share the same pool of threads.


To see this more clearly, imagine that you use both Dispatchers.Default and Dispatchers.IO to the maximum.
As a result, your number of active threads will be the sum of their limits.
If you allow 64 threads in Dispatchers.IO and you have 8 cores, you will have 72 active threads in the shared pool.
This means we have efficient thread reuse and both dispatchers have strong independence.

The most typical case where we use Dispatchers.IO is when we need to call blocking functions from libraries.
The best practice is to wrap them with withContext(Dispatchers.IO) to make them suspending functions.
 */

suspend fun main(): Unit =
    coroutineScope {
        launch(Dispatchers.Default) {
            println(Thread.currentThread().name)
            withContext(Dispatchers.IO) {
                println(Thread.currentThread().name)
            }
        }
    }
